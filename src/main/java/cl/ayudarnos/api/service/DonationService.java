package cl.ayudarnos.api.service;

import cl.ayudarnos.api.dto.donation.*;
import cl.ayudarnos.api.entity.Donation;
import cl.ayudarnos.api.entity.DonationItem;
import cl.ayudarnos.api.entity.Need;
import cl.ayudarnos.api.entity.User;
import cl.ayudarnos.api.entity.enums.DonationStatus;
import cl.ayudarnos.api.entity.enums.NeedStatus;
import cl.ayudarnos.api.exception.BadRequestException;
import cl.ayudarnos.api.exception.ResourceNotFoundException;
import cl.ayudarnos.api.repository.DonationRepository;
import cl.ayudarnos.api.repository.NeedRepository;
import cl.ayudarnos.api.repository.UserRepository;
import cl.ayudarnos.api.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DonationService {

    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final DonationRepository donationRepository;
    private final NeedRepository needRepository;
    private final UserRepository userRepository;

    public DonationService(DonationRepository donationRepository,
                           NeedRepository needRepository,
                           UserRepository userRepository) {
        this.donationRepository = donationRepository;
        this.needRepository = needRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public DonationFormTemplateDto getDonationFormTemplate(UUID needId, UserPrincipal principal) {
        Need need = needRepository.findById(needId)
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada con ID: " + needId));

        DonationFormTemplateDto template = new DonationFormTemplateDto();
        template.setNeedId(need.getId());
        template.setNeedTitle(need.getTitle());
        template.setOrganizationName(need.getOrganization().getName());
        template.setNeedRegion(need.getRegion());
        template.setNeedCommune(need.getCommune());
        template.setInstructions(need.getInstructions());
        template.setCategory(need.getCategory());
        template.setReferenceProducts(need.getSuggestedProducts() != null ? need.getSuggestedProducts() : List.of());

        // Campos de productos VACÍOS por defecto para que el donante agregue lo que desea donar
        template.setItems(new ArrayList<>());

        // Solo se rellenan los datos del perfil si hay sesión activa
        if (principal != null) {
            template.setAuthenticated(true);
            User user = userRepository.findById(principal.getId()).orElse(null);
            template.setDonorFullName(user != null ? user.getFullName() : principal.getFullName());
            template.setDonorEmail(user != null ? user.getEmail() : principal.getUsername());
            template.setDonorPhone(user != null && user.getPhone() != null ? user.getPhone() : "");
        } else {
            template.setAuthenticated(false);
            template.setDonorFullName("");
            template.setDonorEmail("");
            template.setDonorPhone("");
        }

        return template;
    }

    @Transactional
    public DonationResponse createDonation(DonationCreateRequest request, UserPrincipal principal) {
        Need need = needRepository.findById(request.getNeedId())
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada con ID: " + request.getNeedId()));

        if (need.getStatus() != NeedStatus.ACTIVE) {
            throw new BadRequestException("Esta necesidad no se encuentra activa para recibir donaciones");
        }

        String donorFullName;
        String donorEmail;
        String donorPhone;
        User donorUser = null;

        if (principal != null) {
            donorUser = userRepository.findById(principal.getId()).orElse(null);
            donorFullName = (request.getDonorFullName() != null && !request.getDonorFullName().isBlank())
                    ? request.getDonorFullName().trim()
                    : (donorUser != null ? donorUser.getFullName() : principal.getFullName());

            donorEmail = (request.getDonorEmail() != null && !request.getDonorEmail().isBlank())
                    ? request.getDonorEmail().trim().toLowerCase()
                    : (donorUser != null ? donorUser.getEmail() : principal.getUsername());

            donorPhone = (request.getDonorPhone() != null && !request.getDonorPhone().isBlank())
                    ? request.getDonorPhone().trim()
                    : (donorUser != null && donorUser.getPhone() != null ? donorUser.getPhone() : "");
        } else {
            if (request.getDonorFullName() == null || request.getDonorFullName().isBlank()) {
                throw new BadRequestException("El nombre completo del donante es obligatorio");
            }
            if (request.getDonorEmail() == null || request.getDonorEmail().isBlank()) {
                throw new BadRequestException("El correo electrónico del donante es obligatorio");
            }
            if (request.getDonorPhone() == null || request.getDonorPhone().isBlank()) {
                throw new BadRequestException("El teléfono de contacto del donante es obligatorio");
            }
            donorFullName = request.getDonorFullName().trim();
            donorEmail = request.getDonorEmail().trim().toLowerCase();
            donorPhone = request.getDonorPhone().trim();

            donorUser = userRepository.findByEmail(donorEmail).orElse(null);
        }

        String trackingCode = generateUniqueTrackingCode();

        Donation donation = new Donation(
                trackingCode,
                need,
                donorUser,
                request.getDeliveryMethod(),
                request.getDonorCommune().trim(),
                request.getAvailability() != null ? request.getAvailability().trim() : null,
                donorFullName,
                donorEmail,
                donorPhone,
                request.isConsentContactSharing()
        );

        int totalDonatedItems = 0;
        for (DonationItemDto itemDto : request.getItems()) {
            DonationItem item = new DonationItem(
                    itemDto.getProductName().trim(),
                    itemDto.getQuantity(),
                    itemDto.getPresentation().trim()
            );
            donation.addItem(item);
            totalDonatedItems += itemDto.getQuantity();
        }

        donation = donationRepository.save(donation);

        // Update progress of the need
        if (need.getTargetQuantity() != null && need.getTargetQuantity() > 0) {
            int newQuantity = need.getCurrentQuantity() + totalDonatedItems;
            need.setCurrentQuantity(newQuantity);
            int newPercentage = Math.min(100, (int) Math.round(((double) newQuantity / need.getTargetQuantity()) * 100));
            need.setProgressPercentage(newPercentage);
            needRepository.save(need);
        }

        return mapToDto(donation);
    }

    @Transactional(readOnly = true)
    public DonationResponse trackByCode(String trackingCode) {
        Donation donation = donationRepository.findByTrackingCode(trackingCode.trim().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró donación con el código de seguimiento: " + trackingCode));
        return mapToDto(donation);
    }

    @Transactional(readOnly = true)
    public List<DonationResponse> getMyDonations(UserPrincipal principal) {
        List<Donation> donations = donationRepository.findByDonorUserIdOrderByCreatedAtDesc(principal.getId());
        return donations.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DonationResponse> getDonationsByNeed(UUID needId, UserPrincipal principal) {
        Need need = needRepository.findById(needId)
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada"));

        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = principal.getOrganizationId() != null && principal.getOrganizationId().equals(need.getOrganization().getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("No tiene permisos para ver las donaciones de esta necesidad");
        }

        return donationRepository.findByNeedIdOrderByCreatedAtDesc(needId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DonationResponse> getDonationsForOrganization(UserPrincipal principal) {
        if (principal.getOrganizationId() == null) {
            throw new BadRequestException("El usuario no pertenece a una organización");
        }
        return donationRepository.findByNeedOrganizationIdOrderByCreatedAtDesc(principal.getOrganizationId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DonationResponse updateStatus(UUID id, DonationStatusUpdateRequest request, UserPrincipal principal) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donación no encontrada"));

        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = principal.getOrganizationId() != null && principal.getOrganizationId().equals(donation.getNeed().getOrganization().getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("No tiene permisos para modificar el estado de esta donación");
        }

        donation.setStatus(request.getStatus());
        if (request.getStatusNotes() != null) {
            donation.setStatusNotes(request.getStatusNotes());
        }

        donation = donationRepository.save(donation);
        return mapToDto(donation);
    }

    private String generateUniqueTrackingCode() {
        int currentYear = Year.now().getValue();
        for (int attempt = 0; attempt < 10; attempt++) {
            StringBuilder sb = new StringBuilder("AYUD-").append(currentYear).append("-");
            for (int i = 0; i < 5; i++) {
                sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
            }
            String code = sb.toString();
            if (donationRepository.findByTrackingCode(code).isEmpty()) {
                return code;
            }
        }
        return "AYUD-" + System.currentTimeMillis();
    }

    public DonationResponse mapToDto(Donation d) {
        DonationResponse res = new DonationResponse();
        res.setId(d.getId());
        res.setTrackingCode(d.getTrackingCode());
        res.setNeedId(d.getNeed().getId());
        res.setNeedTitle(d.getNeed().getTitle());
        res.setOrganizationName(d.getNeed().getOrganization().getName());
        res.setNeedRegion(d.getNeed().getRegion());
        res.setNeedCommune(d.getNeed().getCommune());
        res.setDeliveryMethod(d.getDeliveryMethod());
        res.setDonorCommune(d.getDonorCommune());
        res.setAvailability(d.getAvailability());
        res.setDonorFullName(d.getDonorFullName());
        res.setDonorEmail(d.getDonorEmail());
        res.setDonorPhone(d.getDonorPhone());
        res.setConsentContactSharing(d.isConsentContactSharing());
        res.setStatus(d.getStatus());
        res.setStatusNotes(d.getStatusNotes());
        res.setCreatedAt(d.getCreatedAt());
        res.setUpdatedAt(d.getUpdatedAt());

        List<DonationItemDto> items = d.getItems().stream()
                .map(item -> new DonationItemDto(item.getProductName(), item.getQuantity(), item.getPresentation()))
                .collect(Collectors.toList());
        res.setItems(items);

        return res;
    }
}

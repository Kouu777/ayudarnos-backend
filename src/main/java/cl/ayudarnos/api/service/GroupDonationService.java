package cl.ayudarnos.api.service;

import cl.ayudarnos.api.dto.donation.GroupContributionItemDto;
import cl.ayudarnos.api.dto.donation.GroupDonationCreateRequest;
import cl.ayudarnos.api.dto.donation.GroupDonationResponse;
import cl.ayudarnos.api.entity.GroupContributionItem;
import cl.ayudarnos.api.entity.GroupDonation;
import cl.ayudarnos.api.entity.Need;
import cl.ayudarnos.api.entity.User;
import cl.ayudarnos.api.entity.enums.DonationStatus;
import cl.ayudarnos.api.entity.enums.NeedStatus;
import cl.ayudarnos.api.exception.BadRequestException;
import cl.ayudarnos.api.exception.ResourceNotFoundException;
import cl.ayudarnos.api.repository.GroupDonationRepository;
import cl.ayudarnos.api.repository.NeedRepository;
import cl.ayudarnos.api.repository.UserRepository;
import cl.ayudarnos.api.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupDonationService {

    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final GroupDonationRepository groupDonationRepository;
    private final NeedRepository needRepository;
    private final UserRepository userRepository;

    public GroupDonationService(GroupDonationRepository groupDonationRepository,
                                NeedRepository needRepository,
                                UserRepository userRepository) {
        this.groupDonationRepository = groupDonationRepository;
        this.needRepository = needRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GroupDonationResponse createGroupDonation(GroupDonationCreateRequest request, UserPrincipal principal) {
        Need need = needRepository.findById(request.getNeedId())
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada con ID: " + request.getNeedId()));

        if (need.getStatus() != NeedStatus.ACTIVE) {
            throw new BadRequestException("Esta necesidad no se encuentra activa");
        }

        User coordinatorUser = null;
        String coordinatorName;
        String coordinatorEmail;
        String coordinatorPhone;

        if (principal != null) {
            coordinatorUser = userRepository.findById(principal.getId()).orElse(null);
            coordinatorName = (request.getCoordinatorName() != null && !request.getCoordinatorName().isBlank())
                    ? request.getCoordinatorName().trim()
                    : (coordinatorUser != null ? coordinatorUser.getFullName() : principal.getFullName());

            coordinatorEmail = (request.getCoordinatorEmail() != null && !request.getCoordinatorEmail().isBlank())
                    ? request.getCoordinatorEmail().trim().toLowerCase()
                    : (coordinatorUser != null ? coordinatorUser.getEmail() : principal.getUsername());

            coordinatorPhone = (request.getCoordinatorPhone() != null && !request.getCoordinatorPhone().isBlank())
                    ? request.getCoordinatorPhone().trim()
                    : (coordinatorUser != null && coordinatorUser.getPhone() != null ? coordinatorUser.getPhone() : "");
        } else {
            if (request.getCoordinatorName() == null || request.getCoordinatorName().isBlank()) {
                throw new BadRequestException("El nombre del coordinador del grupo es obligatorio");
            }
            if (request.getCoordinatorEmail() == null || request.getCoordinatorEmail().isBlank()) {
                throw new BadRequestException("El correo del coordinador del grupo es obligatorio");
            }
            if (request.getCoordinatorPhone() == null || request.getCoordinatorPhone().isBlank()) {
                throw new BadRequestException("El teléfono del coordinador del grupo es obligatorio");
            }
            coordinatorName = request.getCoordinatorName().trim();
            coordinatorEmail = request.getCoordinatorEmail().trim().toLowerCase();
            coordinatorPhone = request.getCoordinatorPhone().trim();

            coordinatorUser = userRepository.findByEmail(coordinatorEmail).orElse(null);
        }

        String trackingCode = generateUniqueGroupTrackingCode();

        GroupDonation groupDonation = new GroupDonation(
                trackingCode,
                need,
                request.getGroupName().trim(),
                request.getGroupType(),
                coordinatorUser,
                coordinatorName,
                coordinatorEmail,
                coordinatorPhone,
                request.getTargetDescription(),
                request.getDeliveryMethod(),
                request.getDonorCommune().trim(),
                request.getAvailability() != null ? request.getAvailability().trim() : null
        );

        int totalItems = 0;
        if (request.getInitialContributions() != null) {
            for (GroupContributionItemDto itemDto : request.getInitialContributions()) {
                GroupContributionItem item = new GroupContributionItem(
                        itemDto.getContributorName().trim(),
                        itemDto.getProductName().trim(),
                        itemDto.getQuantity(),
                        itemDto.getPresentation().trim()
                );
                groupDonation.addContribution(item);
                totalItems += itemDto.getQuantity();
            }
        }

        groupDonation = groupDonationRepository.save(groupDonation);

        if (totalItems > 0 && need.getTargetQuantity() != null && need.getTargetQuantity() > 0) {
            int newQuantity = need.getCurrentQuantity() + totalItems;
            need.setCurrentQuantity(newQuantity);
            int newPercentage = Math.min(100, (int) Math.round(((double) newQuantity / need.getTargetQuantity()) * 100));
            need.setProgressPercentage(newPercentage);
            needRepository.save(need);
        }

        return mapToDto(groupDonation);
    }

    @Transactional
    public GroupDonationResponse addContribution(UUID groupDonationId, GroupContributionItemDto dto) {
        GroupDonation group = groupDonationRepository.findById(groupDonationId)
                .orElseThrow(() -> new ResourceNotFoundException("Iniciativa de donación grupal no encontrada"));

        GroupContributionItem item = new GroupContributionItem(
                dto.getContributorName().trim(),
                dto.getProductName().trim(),
                dto.getQuantity(),
                dto.getPresentation().trim()
        );

        group.addContribution(item);
        group = groupDonationRepository.save(group);

        // Actualizar progreso de la necesidad
        Need need = group.getNeed();
        if (need.getTargetQuantity() != null && need.getTargetQuantity() > 0) {
            int newQuantity = need.getCurrentQuantity() + dto.getQuantity();
            need.setCurrentQuantity(newQuantity);
            int newPercentage = Math.min(100, (int) Math.round(((double) newQuantity / need.getTargetQuantity()) * 100));
            need.setProgressPercentage(newPercentage);
            needRepository.save(need);
        }

        return mapToDto(group);
    }

    @Transactional(readOnly = true)
    public GroupDonationResponse trackByCode(String trackingCode) {
        GroupDonation group = groupDonationRepository.findByTrackingCode(trackingCode.trim().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró donación grupal con el código: " + trackingCode));
        return mapToDto(group);
    }

    @Transactional(readOnly = true)
    public List<GroupDonationResponse> getGroupDonationsByNeed(UUID needId, UserPrincipal principal) {
        Need need = needRepository.findById(needId)
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada"));

        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = principal.getOrganizationId() != null && principal.getOrganizationId().equals(need.getOrganization().getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("No tiene permisos para ver las donaciones grupales de esta necesidad");
        }

        return groupDonationRepository.findByNeedIdOrderByCreatedAtDesc(needId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupDonationResponse updateStatus(UUID id, DonationStatus status, String statusNotes, UserPrincipal principal) {
        GroupDonation group = groupDonationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donación grupal no encontrada"));

        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = principal.getOrganizationId() != null && principal.getOrganizationId().equals(group.getNeed().getOrganization().getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("No tiene permisos para modificar esta donación grupal");
        }

        group.setStatus(status);
        if (statusNotes != null) {
            group.setStatusNotes(statusNotes);
        }

        group = groupDonationRepository.save(group);
        return mapToDto(group);
    }

    private String generateUniqueGroupTrackingCode() {
        int currentYear = Year.now().getValue();
        StringBuilder sb = new StringBuilder("AYUD-GRP-").append(currentYear).append("-");
        for (int i = 0; i < 5; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public GroupDonationResponse mapToDto(GroupDonation g) {
        GroupDonationResponse res = new GroupDonationResponse();
        res.setId(g.getId());
        res.setTrackingCode(g.getTrackingCode());
        res.setNeedId(g.getNeed().getId());
        res.setNeedTitle(g.getNeed().getTitle());
        res.setOrganizationName(g.getNeed().getOrganization().getName());
        res.setGroupName(g.getGroupName());
        res.setGroupType(g.getGroupType());
        res.setCoordinatorName(g.getCoordinatorName());
        res.setCoordinatorEmail(g.getCoordinatorEmail());
        res.setCoordinatorPhone(g.getCoordinatorPhone());
        res.setTargetDescription(g.getTargetDescription());
        res.setDeliveryMethod(g.getDeliveryMethod());
        res.setDonorCommune(g.getDonorCommune());
        res.setAvailability(g.getAvailability());
        res.setStatus(g.getStatus());
        res.setStatusNotes(g.getStatusNotes());
        res.setCreatedAt(g.getCreatedAt());
        res.setUpdatedAt(g.getUpdatedAt());

        List<GroupContributionItemDto> items = g.getContributions().stream()
                .map(c -> {
                    GroupContributionItemDto dto = new GroupContributionItemDto(
                            c.getContributorName(),
                            c.getProductName(),
                            c.getQuantity(),
                            c.getPresentation()
                    );
                    dto.setId(c.getId());
                    return dto;
                })
                .collect(Collectors.toList());

        res.setContributions(items);
        res.setTotalItemsCount(items.stream().mapToInt(GroupContributionItemDto::getQuantity).sum());
        return res;
    }
}

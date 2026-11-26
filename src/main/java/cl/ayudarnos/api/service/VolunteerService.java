package cl.ayudarnos.api.service;

import cl.ayudarnos.api.dto.volunteer.*;
import cl.ayudarnos.api.entity.Catastrophe;
import cl.ayudarnos.api.entity.Organization;
import cl.ayudarnos.api.entity.User;
import cl.ayudarnos.api.entity.VolunteerOpportunity;
import cl.ayudarnos.api.entity.VolunteerRegistration;
import cl.ayudarnos.api.entity.enums.RegistrationStatus;
import cl.ayudarnos.api.entity.enums.VolunteerStatus;
import cl.ayudarnos.api.exception.BadRequestException;
import cl.ayudarnos.api.exception.ResourceNotFoundException;
import cl.ayudarnos.api.repository.CatastropheRepository;
import cl.ayudarnos.api.repository.OrganizationRepository;
import cl.ayudarnos.api.repository.UserRepository;
import cl.ayudarnos.api.repository.VolunteerOpportunityRepository;
import cl.ayudarnos.api.repository.VolunteerRegistrationRepository;
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
public class VolunteerService {

    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final VolunteerOpportunityRepository opportunityRepository;
    private final VolunteerRegistrationRepository registrationRepository;
    private final OrganizationRepository organizationRepository;
    private final CatastropheRepository catastropheRepository;
    private final UserRepository userRepository;
    private final OrganizationService organizationService;

    public VolunteerService(VolunteerOpportunityRepository opportunityRepository,
                            VolunteerRegistrationRepository registrationRepository,
                            OrganizationRepository organizationRepository,
                            CatastropheRepository catastropheRepository,
                            UserRepository userRepository,
                            OrganizationService organizationService) {
        this.opportunityRepository = opportunityRepository;
        this.registrationRepository = registrationRepository;
        this.organizationRepository = organizationRepository;
        this.catastropheRepository = catastropheRepository;
        this.userRepository = userRepository;
        this.organizationService = organizationService;
    }

    @Transactional(readOnly = true)
    public List<VolunteerOpportunityResponse> getOpportunities(VolunteerStatus status, String region, String commune) {
        return opportunityRepository.searchOpportunities(status, region, commune).stream()
                .map(this::mapOpportunityToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VolunteerOpportunityResponse getOpportunityById(UUID id) {
        VolunteerOpportunity opp = opportunityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria de voluntariado no encontrada con ID: " + id));
        return mapOpportunityToDto(opp);
    }

    @Transactional
    public VolunteerOpportunityResponse createOpportunity(VolunteerOpportunityCreateRequest request, UserPrincipal principal) {
        if (principal.getOrganizationId() == null) {
            throw new BadRequestException("Solo usuarios de una organización pueden publicar convocatorias de voluntariado");
        }

        Organization org = organizationRepository.findById(principal.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organización no encontrada"));

        Catastrophe catastrophe = null;
        if (request.getCatastropheId() != null) {
            catastrophe = catastropheRepository.findById(request.getCatastropheId())
                    .orElseThrow(() -> new ResourceNotFoundException("Catástrofe no encontrada"));
        }

        VolunteerOpportunity opp = new VolunteerOpportunity(
                org,
                catastrophe,
                request.getTitle().trim(),
                request.getDescription().trim(),
                request.getLocationName().trim(),
                request.getRegion().trim(),
                request.getCommune().trim(),
                request.getAddress(),
                request.getSchedule().trim(),
                request.getRequirements(),
                request.getRequiredVolunteers(),
                request.isUrgent()
        );

        opp = opportunityRepository.save(opp);
        return mapOpportunityToDto(opp);
    }

    @Transactional
    public VolunteerRegistrationResponse registerVolunteer(UUID opportunityId,
                                                           VolunteerRegistrationRequest request,
                                                           UserPrincipal principal) {
        VolunteerOpportunity opp = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada con ID: " + opportunityId));

        if (opp.getStatus() != VolunteerStatus.OPEN) {
            throw new BadRequestException("Esta convocatoria no se encuentra abierta para nuevas inscripciones");
        }

        User user = null;
        String fullName;
        String email;
        String phone;

        if (principal != null) {
            user = userRepository.findById(principal.getId()).orElse(null);
            fullName = (request.getFullName() != null && !request.getFullName().isBlank())
                    ? request.getFullName().trim()
                    : (user != null ? user.getFullName() : principal.getFullName());

            email = (request.getEmail() != null && !request.getEmail().isBlank())
                    ? request.getEmail().trim().toLowerCase()
                    : (user != null ? user.getEmail() : principal.getUsername());

            phone = (request.getPhone() != null && !request.getPhone().isBlank())
                    ? request.getPhone().trim()
                    : (user != null && user.getPhone() != null ? user.getPhone() : "");
        } else {
            if (request.getFullName() == null || request.getFullName().isBlank()) {
                throw new BadRequestException("El nombre del voluntario es obligatorio");
            }
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                throw new BadRequestException("El correo del voluntario es obligatorio");
            }
            if (request.getPhone() == null || request.getPhone().isBlank()) {
                throw new BadRequestException("El teléfono del voluntario es obligatorio");
            }
            fullName = request.getFullName().trim();
            email = request.getEmail().trim().toLowerCase();
            phone = request.getPhone().trim();

            user = userRepository.findByEmail(email).orElse(null);
        }

        String regCode = generateRegistrationCode();

        VolunteerRegistration reg = new VolunteerRegistration(
                opp,
                user,
                regCode,
                fullName,
                email,
                phone,
                request.getAvailabilityNotes()
        );

        reg = registrationRepository.save(reg);

        // Actualizar contador de voluntarios de la convocatoria
        opp.setCurrentVolunteers(opp.getCurrentVolunteers() + 1);
        if (opp.getCurrentVolunteers() >= opp.getRequiredVolunteers()) {
            opp.setStatus(VolunteerStatus.FULL);
        }
        opportunityRepository.save(opp);

        return mapRegistrationToDto(reg);
    }

    @Transactional(readOnly = true)
    public List<VolunteerRegistrationResponse> getVolunteersForOpportunity(UUID opportunityId, UserPrincipal principal) {
        VolunteerOpportunity opp = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new ResourceNotFoundException("Convocatoria no encontrada"));

        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = principal.getOrganizationId() != null && principal.getOrganizationId().equals(opp.getOrganization().getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("No tiene permisos para ver los voluntarios de esta convocatoria");
        }

        return registrationRepository.findByOpportunityIdOrderByCreatedAtDesc(opportunityId).stream()
                .map(this::mapRegistrationToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VolunteerRegistrationResponse> getMyRegistrations(UserPrincipal principal) {
        return registrationRepository.findByUserIdOrderByCreatedAtDesc(principal.getId()).stream()
                .map(this::mapRegistrationToDto)
                .collect(Collectors.toList());
    }

    private String generateRegistrationCode() {
        int currentYear = Year.now().getValue();
        StringBuilder sb = new StringBuilder("VOL-").append(currentYear).append("-");
        for (int i = 0; i < 5; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public VolunteerOpportunityResponse mapOpportunityToDto(VolunteerOpportunity opp) {
        VolunteerOpportunityResponse res = new VolunteerOpportunityResponse();
        res.setId(opp.getId());
        res.setTitle(opp.getTitle());
        res.setDescription(opp.getDescription());
        res.setLocationName(opp.getLocationName());
        res.setRegion(opp.getRegion());
        res.setCommune(opp.getCommune());
        res.setAddress(opp.getAddress());
        res.setSchedule(opp.getSchedule());
        res.setRequirements(opp.getRequirements());
        res.setRequiredVolunteers(opp.getRequiredVolunteers());
        res.setCurrentVolunteers(opp.getCurrentVolunteers());
        res.setStatus(opp.getStatus());
        res.setUrgent(opp.isUrgent());
        res.setOrganization(organizationService.mapToDto(opp.getOrganization()));
        if (opp.getCatastrophe() != null) {
            res.setCatastropheId(opp.getCatastrophe().getId());
            res.setCatastropheName(opp.getCatastrophe().getName());
        }
        res.setCreatedAt(opp.getCreatedAt());
        return res;
    }

    public VolunteerRegistrationResponse mapRegistrationToDto(VolunteerRegistration reg) {
        VolunteerRegistrationResponse res = new VolunteerRegistrationResponse();
        res.setId(reg.getId());
        res.setRegistrationCode(reg.getRegistrationCode());
        res.setOpportunityId(reg.getOpportunity().getId());
        res.setOpportunityTitle(reg.getOpportunity().getTitle());
        res.setLocationName(reg.getOpportunity().getLocationName());
        res.setRegion(reg.getOpportunity().getRegion());
        res.setCommune(reg.getOpportunity().getCommune());
        res.setSchedule(reg.getOpportunity().getSchedule());
        res.setFullName(reg.getFullName());
        res.setEmail(reg.getEmail());
        res.setPhone(reg.getPhone());
        res.setAvailabilityNotes(reg.getAvailabilityNotes());
        res.setStatus(reg.getStatus());
        res.setCreatedAt(reg.getCreatedAt());
        return res;
    }
}

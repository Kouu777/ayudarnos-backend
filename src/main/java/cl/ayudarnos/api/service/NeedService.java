package cl.ayudarnos.api.service;

import cl.ayudarnos.api.dto.need.NeedCreateRequest;
import cl.ayudarnos.api.dto.need.NeedResponse;
import cl.ayudarnos.api.dto.need.NeedUpdateRequest;
import cl.ayudarnos.api.entity.Catastrophe;
import cl.ayudarnos.api.entity.Need;
import cl.ayudarnos.api.entity.Organization;
import cl.ayudarnos.api.entity.enums.NeedStatus;
import cl.ayudarnos.api.exception.BadRequestException;
import cl.ayudarnos.api.exception.ResourceNotFoundException;
import cl.ayudarnos.api.repository.CatastropheRepository;
import cl.ayudarnos.api.repository.NeedRepository;
import cl.ayudarnos.api.repository.OrganizationRepository;
import cl.ayudarnos.api.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NeedService {

    private final NeedRepository needRepository;
    private final OrganizationRepository organizationRepository;
    private final CatastropheRepository catastropheRepository;
    private final OrganizationService organizationService;

    public NeedService(NeedRepository needRepository,
                       OrganizationRepository organizationRepository,
                       CatastropheRepository catastropheRepository,
                       OrganizationService organizationService) {
        this.needRepository = needRepository;
        this.organizationRepository = organizationRepository;
        this.catastropheRepository = catastropheRepository;
        this.organizationService = organizationService;
    }

    @Transactional(readOnly = true)
    public List<NeedResponse> getNeeds(NeedStatus status, String region, String category, UUID catastropheId) {
        NeedStatus queryStatus = status != null ? status : NeedStatus.ACTIVE;
        List<Need> needs = needRepository.searchNeeds(queryStatus, region, category, catastropheId);
        return needs.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NeedResponse getNeedById(UUID id) {
        Need need = needRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada con ID: " + id));
        return mapToDto(need);
    }

    @Transactional
    public NeedResponse createNeed(NeedCreateRequest request, UserPrincipal principal) {
        if (principal.getOrganizationId() == null) {
            throw new BadRequestException("Solo usuarios asociados a una organización pueden publicar necesidades");
        }

        Organization org = organizationRepository.findById(principal.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException("Organización no encontrada"));

        Catastrophe catastrophe = null;
        if (request.getCatastropheId() != null) {
            catastrophe = catastropheRepository.findById(request.getCatastropheId())
                    .orElseThrow(() -> new ResourceNotFoundException("Catástrofe no encontrada"));
        }

        Need need = new Need(
                org,
                catastrophe,
                request.getTitle().trim(),
                request.getDescription(),
                request.getCategory().trim(),
                request.getRegion().trim(),
                request.getCommune().trim(),
                request.getTargetQuantity(),
                0,
                0,
                request.getInstructions(),
                request.isUrgent()
        );

        if (request.getSuggestedProducts() != null) {
            need.setSuggestedProducts(request.getSuggestedProducts());
        }

        need = needRepository.save(need);
        return mapToDto(need);
    }

    @Transactional
    public NeedResponse updateNeed(UUID id, NeedUpdateRequest request, UserPrincipal principal) {
        Need need = needRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada con ID: " + id));

        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = principal.getOrganizationId() != null && principal.getOrganizationId().equals(need.getOrganization().getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("No tiene permisos para modificar esta necesidad");
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) need.setTitle(request.getTitle().trim());
        if (request.getDescription() != null) need.setDescription(request.getDescription());
        if (request.getCategory() != null && !request.getCategory().isBlank()) need.setCategory(request.getCategory().trim());
        if (request.getTargetQuantity() != null) need.setTargetQuantity(request.getTargetQuantity());
        if (request.getCurrentQuantity() != null) need.setCurrentQuantity(request.getCurrentQuantity());
        if (request.getProgressPercentage() != null) need.setProgressPercentage(request.getProgressPercentage());
        if (request.getInstructions() != null) need.setInstructions(request.getInstructions());
        if (request.getSuggestedProducts() != null) need.setSuggestedProducts(request.getSuggestedProducts());
        if (request.getStatus() != null) need.setStatus(request.getStatus());
        if (request.getUrgent() != null) need.setUrgent(request.getUrgent());

        need = needRepository.save(need);
        return mapToDto(need);
    }

    @Transactional
    public void deleteNeed(UUID id, UserPrincipal principal) {
        Need need = needRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Necesidad no encontrada con ID: " + id));

        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = principal.getOrganizationId() != null && principal.getOrganizationId().equals(need.getOrganization().getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("No tiene permisos para eliminar esta necesidad");
        }

        need.setStatus(NeedStatus.CANCELLED);
        needRepository.save(need);
    }

    public NeedResponse mapToDto(Need need) {
        NeedResponse response = new NeedResponse();
        response.setId(need.getId());
        response.setTitle(need.getTitle());
        response.setDescription(need.getDescription());
        response.setCategory(need.getCategory());
        response.setRegion(need.getRegion());
        response.setCommune(need.getCommune());
        response.setTargetQuantity(need.getTargetQuantity());
        response.setCurrentQuantity(need.getCurrentQuantity());
        response.setProgressPercentage(need.getProgressPercentage());
        response.setInstructions(need.getInstructions());
        response.setSuggestedProducts(need.getSuggestedProducts());
        response.setStatus(need.getStatus());
        response.setUrgent(need.isUrgent());
        response.setOrganization(organizationService.mapToDto(need.getOrganization()));
        if (need.getCatastrophe() != null) {
            response.setCatastropheId(need.getCatastrophe().getId());
            response.setCatastropheName(need.getCatastrophe().getName());
        }
        response.setCreatedAt(need.getCreatedAt());
        return response;
    }
}

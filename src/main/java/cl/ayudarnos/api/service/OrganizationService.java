package cl.ayudarnos.api.service;

import cl.ayudarnos.api.dto.organization.OrganizationDto;
import cl.ayudarnos.api.entity.Organization;
import cl.ayudarnos.api.exception.BadRequestException;
import cl.ayudarnos.api.exception.ResourceNotFoundException;
import cl.ayudarnos.api.repository.OrganizationRepository;
import cl.ayudarnos.api.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Transactional(readOnly = true)
    public List<OrganizationDto> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrganizationDto getOrganizationById(UUID id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organización no encontrada con ID: " + id));
        return mapToDto(organization);
    }

    @Transactional(readOnly = true)
    public OrganizationDto getMyOrganization(UserPrincipal principal) {
        if (principal.getOrganizationId() == null) {
            throw new BadRequestException("El usuario autenticado no está asociado a una organización");
        }
        return getOrganizationById(principal.getOrganizationId());
    }

    public OrganizationDto mapToDto(Organization org) {
        return new OrganizationDto(
                org.getId(),
                org.getName(),
                org.getType(),
                org.getRutTaxId(),
                org.getContactPerson(),
                org.getPhone(),
                org.getRegion(),
                org.getCommune(),
                org.getAddress(),
                org.getDescription(),
                org.isVerified()
        );
    }
}

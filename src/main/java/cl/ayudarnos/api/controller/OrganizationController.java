package cl.ayudarnos.api.controller;

import cl.ayudarnos.api.dto.organization.OrganizationDto;
import cl.ayudarnos.api.security.UserPrincipal;
import cl.ayudarnos.api.service.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDto>> getAllOrganizations() {
        List<OrganizationDto> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getOrganizationById(@PathVariable UUID id) {
        OrganizationDto org = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(org);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ORGANIZATION')")
    public ResponseEntity<OrganizationDto> getMyOrganization(@AuthenticationPrincipal UserPrincipal principal) {
        OrganizationDto org = organizationService.getMyOrganization(principal);
        return ResponseEntity.ok(org);
    }
}

package cl.ayudarnos.api.controller;

import cl.ayudarnos.api.dto.volunteer.*;
import cl.ayudarnos.api.entity.enums.VolunteerStatus;
import cl.ayudarnos.api.security.UserPrincipal;
import cl.ayudarnos.api.service.VolunteerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/volunteering")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping
    public ResponseEntity<List<VolunteerOpportunityResponse>> getOpportunities(
            @RequestParam(required = false) VolunteerStatus status,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String commune) {
        List<VolunteerOpportunityResponse> list = volunteerService.getOpportunities(status, region, commune);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolunteerOpportunityResponse> getOpportunityById(@PathVariable UUID id) {
        VolunteerOpportunityResponse res = volunteerService.getOpportunityById(id);
        return ResponseEntity.ok(res);
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<VolunteerOpportunityResponse> createOpportunity(
            @Valid @RequestBody VolunteerOpportunityCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        VolunteerOpportunityResponse res = volunteerService.createOpportunity(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<VolunteerRegistrationResponse> registerVolunteer(
            @PathVariable UUID id,
            @RequestBody VolunteerRegistrationRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        VolunteerRegistrationResponse res = volunteerService.registerVolunteer(id, request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/{id}/volunteers")
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<List<VolunteerRegistrationResponse>> getVolunteers(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        List<VolunteerRegistrationResponse> list = volunteerService.getVolunteersForOpportunity(id, principal);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/my-registrations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VolunteerRegistrationResponse>> getMyRegistrations(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<VolunteerRegistrationResponse> list = volunteerService.getMyRegistrations(principal);
        return ResponseEntity.ok(list);
    }
}

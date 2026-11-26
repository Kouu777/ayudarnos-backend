package cl.ayudarnos.api.controller;

import cl.ayudarnos.api.dto.donation.DonationCreateRequest;
import cl.ayudarnos.api.dto.donation.DonationFormTemplateDto;
import cl.ayudarnos.api.dto.donation.DonationResponse;
import cl.ayudarnos.api.dto.donation.DonationStatusUpdateRequest;
import cl.ayudarnos.api.security.UserPrincipal;
import cl.ayudarnos.api.service.DonationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @GetMapping("/form-template/{needId}")
    public ResponseEntity<DonationFormTemplateDto> getDonationFormTemplate(
            @PathVariable UUID needId,
            @AuthenticationPrincipal UserPrincipal principal) {
        DonationFormTemplateDto template = donationService.getDonationFormTemplate(needId, principal);
        return ResponseEntity.ok(template);
    }

    @PostMapping
    public ResponseEntity<DonationResponse> createDonation(
            @Valid @RequestBody DonationCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        DonationResponse response = donationService.createDonation(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/track/{trackingCode}")
    public ResponseEntity<DonationResponse> trackDonation(@PathVariable String trackingCode) {
        DonationResponse response = donationService.trackByCode(trackingCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-donations")
    @PreAuthorize("hasRole('DONOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DonationResponse>> getMyDonations(@AuthenticationPrincipal UserPrincipal principal) {
        List<DonationResponse> donations = donationService.getMyDonations(principal);
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/need/{needId}")
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<List<DonationResponse>> getDonationsByNeed(
            @PathVariable UUID needId,
            @AuthenticationPrincipal UserPrincipal principal) {
        List<DonationResponse> donations = donationService.getDonationsByNeed(needId, principal);
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/organization/all")
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<List<DonationResponse>> getOrganizationDonations(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<DonationResponse> donations = donationService.getDonationsForOrganization(principal);
        return ResponseEntity.ok(donations);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<DonationResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody DonationStatusUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        DonationResponse response = donationService.updateStatus(id, request, principal);
        return ResponseEntity.ok(response);
    }
}

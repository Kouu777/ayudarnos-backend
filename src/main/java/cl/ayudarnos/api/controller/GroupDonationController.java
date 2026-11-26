package cl.ayudarnos.api.controller;

import cl.ayudarnos.api.dto.donation.DonationStatusUpdateRequest;
import cl.ayudarnos.api.dto.donation.GroupContributionItemDto;
import cl.ayudarnos.api.dto.donation.GroupDonationCreateRequest;
import cl.ayudarnos.api.dto.donation.GroupDonationResponse;
import cl.ayudarnos.api.security.UserPrincipal;
import cl.ayudarnos.api.service.GroupDonationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/donations/group")
public class GroupDonationController {

    private final GroupDonationService groupDonationService;

    public GroupDonationController(GroupDonationService groupDonationService) {
        this.groupDonationService = groupDonationService;
    }

    @PostMapping
    public ResponseEntity<GroupDonationResponse> createGroupDonation(
            @Valid @RequestBody GroupDonationCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        GroupDonationResponse response = groupDonationService.createGroupDonation(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/contributions")
    public ResponseEntity<GroupDonationResponse> addContribution(
            @PathVariable UUID id,
            @Valid @RequestBody GroupContributionItemDto dto) {
        GroupDonationResponse response = groupDonationService.addContribution(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/track/{trackingCode}")
    public ResponseEntity<GroupDonationResponse> trackGroupDonation(@PathVariable String trackingCode) {
        GroupDonationResponse response = groupDonationService.trackByCode(trackingCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/need/{needId}")
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<List<GroupDonationResponse>> getGroupDonationsByNeed(
            @PathVariable UUID needId,
            @AuthenticationPrincipal UserPrincipal principal) {
        List<GroupDonationResponse> list = groupDonationService.getGroupDonationsByNeed(needId, principal);
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<GroupDonationResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody DonationStatusUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        GroupDonationResponse response = groupDonationService.updateStatus(id, request.getStatus(), request.getStatusNotes(), principal);
        return ResponseEntity.ok(response);
    }
}

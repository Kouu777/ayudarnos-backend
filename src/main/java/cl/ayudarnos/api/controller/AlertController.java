package cl.ayudarnos.api.controller;

import cl.ayudarnos.api.dto.alert.*;
import cl.ayudarnos.api.security.UserPrincipal;
import cl.ayudarnos.api.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<UrgentAlertResponse>> getActiveAlerts(@RequestParam(required = false) String region) {
        List<UrgentAlertResponse> alerts = alertService.getActiveAlerts(region);
        return ResponseEntity.ok(alerts);
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<AlertSubscriptionResponse> subscribe(
            @Valid @RequestBody AlertSubscriptionRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        AlertSubscriptionResponse res = alertService.subscribe(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/subscriptions/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AlertSubscriptionResponse>> getMySubscriptions(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<AlertSubscriptionResponse> list = alertService.getMySubscriptions(principal);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/subscriptions/{id}")
    public ResponseEntity<Void> unsubscribe(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        alertService.unsubscribe(id, principal);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<UrgentAlertResponse> publishAlert(
            @Valid @RequestBody UrgentAlertCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        UrgentAlertResponse res = alertService.publishAlert(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}

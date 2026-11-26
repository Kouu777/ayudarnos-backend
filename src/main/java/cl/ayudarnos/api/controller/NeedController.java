package cl.ayudarnos.api.controller;

import cl.ayudarnos.api.dto.need.NeedCreateRequest;
import cl.ayudarnos.api.dto.need.NeedResponse;
import cl.ayudarnos.api.dto.need.NeedUpdateRequest;
import cl.ayudarnos.api.entity.enums.NeedStatus;
import cl.ayudarnos.api.security.UserPrincipal;
import cl.ayudarnos.api.service.NeedService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/needs")
public class NeedController {

    private final NeedService needService;

    public NeedController(NeedService needService) {
        this.needService = needService;
    }

    @GetMapping
    public ResponseEntity<List<NeedResponse>> getNeeds(
            @RequestParam(required = false) NeedStatus status,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) UUID catastropheId) {
        List<NeedResponse> needs = needService.getNeeds(status, region, category, catastropheId);
        return ResponseEntity.ok(needs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NeedResponse> getNeedById(@PathVariable UUID id) {
        NeedResponse need = needService.getNeedById(id);
        return ResponseEntity.ok(need);
    }

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<NeedResponse> createNeed(
            @Valid @RequestBody NeedCreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        NeedResponse response = needService.createNeed(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<NeedResponse> updateNeed(
            @PathVariable UUID id,
            @RequestBody NeedUpdateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        NeedResponse response = needService.updateNeed(id, request, principal);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNeed(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal principal) {
        needService.deleteNeed(id, principal);
        return ResponseEntity.noContent().build();
    }
}

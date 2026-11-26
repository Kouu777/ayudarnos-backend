package cl.ayudarnos.api.controller;

import cl.ayudarnos.api.dto.catastrophe.CatastropheCreateRequest;
import cl.ayudarnos.api.dto.catastrophe.CatastropheDto;
import cl.ayudarnos.api.service.CatastropheService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catastrophes")
public class CatastropheController {

    private final CatastropheService catastropheService;

    public CatastropheController(CatastropheService catastropheService) {
        this.catastropheService = catastropheService;
    }

    @GetMapping
    public ResponseEntity<List<CatastropheDto>> getActiveCatastrophes() {
        List<CatastropheDto> list = catastropheService.getActiveCatastrophes();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatastropheDto> getCatastropheById(@PathVariable UUID id) {
        CatastropheDto dto = catastropheService.getCatastropheById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CatastropheDto> createCatastrophe(@Valid @RequestBody CatastropheCreateRequest request) {
        CatastropheDto dto = catastropheService.createCatastrophe(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}

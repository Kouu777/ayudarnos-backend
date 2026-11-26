package cl.ayudarnos.api.service;

import cl.ayudarnos.api.dto.catastrophe.CatastropheCreateRequest;
import cl.ayudarnos.api.dto.catastrophe.CatastropheDto;
import cl.ayudarnos.api.entity.Catastrophe;
import cl.ayudarnos.api.exception.ResourceNotFoundException;
import cl.ayudarnos.api.repository.CatastropheRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CatastropheService {

    private final CatastropheRepository catastropheRepository;

    public CatastropheService(CatastropheRepository catastropheRepository) {
        this.catastropheRepository = catastropheRepository;
    }

    @Transactional(readOnly = true)
    public List<CatastropheDto> getActiveCatastrophes() {
        return catastropheRepository.findByActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CatastropheDto getCatastropheById(UUID id) {
        Catastrophe c = catastropheRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catástrofe no encontrada con ID: " + id));
        return mapToDto(c);
    }

    @Transactional
    public CatastropheDto createCatastrophe(CatastropheCreateRequest request) {
        Catastrophe catastrophe = new Catastrophe(
                request.getName().trim(),
                request.getDescription(),
                request.getRegion().trim(),
                request.isActive()
        );
        catastrophe = catastropheRepository.save(catastrophe);
        return mapToDto(catastrophe);
    }

    public CatastropheDto mapToDto(Catastrophe c) {
        return new CatastropheDto(
                c.getId(),
                c.getName(),
                c.getDescription(),
                c.getRegion(),
                c.isActive(),
                c.getCreatedAt()
        );
    }
}

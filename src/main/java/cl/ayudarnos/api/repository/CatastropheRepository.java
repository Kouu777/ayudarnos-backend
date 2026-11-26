package cl.ayudarnos.api.repository;

import cl.ayudarnos.api.entity.Catastrophe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatastropheRepository extends JpaRepository<Catastrophe, UUID> {
    List<Catastrophe> findByActiveTrue();
}

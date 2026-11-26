package cl.ayudarnos.api.repository;

import cl.ayudarnos.api.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    Optional<Organization> findByUserId(UUID userId);
    List<Organization> findByVerifiedTrue();
    List<Organization> findByRegionIgnoreCase(String region);
}

package cl.ayudarnos.api.repository;

import cl.ayudarnos.api.entity.UrgentAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UrgentAlertRepository extends JpaRepository<UrgentAlert, UUID> {

    List<UrgentAlert> findByActiveTrueOrderByCreatedAtDesc();

    @Query("SELECT a FROM UrgentAlert a WHERE a.active = true " +
           "AND (:region IS NULL OR LOWER(a.region) LIKE LOWER(CONCAT('%', :region, '%')) OR LOWER(a.region) = 'nacional') " +
           "ORDER BY a.createdAt DESC")
    List<UrgentAlert> findActiveByRegion(@Param("region") String region);
}

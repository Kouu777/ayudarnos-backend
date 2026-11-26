package cl.ayudarnos.api.repository;

import cl.ayudarnos.api.entity.VolunteerOpportunity;
import cl.ayudarnos.api.entity.enums.VolunteerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VolunteerOpportunityRepository extends JpaRepository<VolunteerOpportunity, UUID> {

    List<VolunteerOpportunity> findByOrganizationId(UUID organizationId);

    List<VolunteerOpportunity> findByCatastropheId(UUID catastropheId);

    @Query("SELECT v FROM VolunteerOpportunity v WHERE (:status IS NULL OR v.status = :status) " +
           "AND (:region IS NULL OR LOWER(v.region) LIKE LOWER(CONCAT('%', :region, '%'))) " +
           "AND (:commune IS NULL OR LOWER(v.commune) LIKE LOWER(CONCAT('%', :commune, '%'))) " +
           "ORDER BY v.urgent DESC, v.createdAt DESC")
    List<VolunteerOpportunity> searchOpportunities(
            @Param("status") VolunteerStatus status,
            @Param("region") String region,
            @Param("commune") String commune
    );
}

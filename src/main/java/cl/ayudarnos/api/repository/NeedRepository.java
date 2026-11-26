package cl.ayudarnos.api.repository;

import cl.ayudarnos.api.entity.Need;
import cl.ayudarnos.api.entity.enums.NeedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NeedRepository extends JpaRepository<Need, UUID> {

    List<Need> findByStatus(NeedStatus status);

    List<Need> findByOrganizationId(UUID organizationId);

    List<Need> findByCatastropheId(UUID catastropheId);

    @Query("SELECT n FROM Need n WHERE n.status = :status " +
           "AND (:region IS NULL OR LOWER(n.region) LIKE LOWER(CONCAT('%', :region, '%'))) " +
           "AND (:category IS NULL OR LOWER(n.category) LIKE LOWER(CONCAT('%', :category, '%'))) " +
           "AND (:catastropheId IS NULL OR n.catastrophe.id = :catastropheId) " +
           "ORDER BY n.urgent DESC, n.createdAt DESC")
    List<Need> searchNeeds(
            @Param("status") NeedStatus status,
            @Param("region") String region,
            @Param("category") String category,
            @Param("catastropheId") UUID catastropheId
    );
}

package cl.ayudarnos.api.repository;

import cl.ayudarnos.api.entity.GroupDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupDonationRepository extends JpaRepository<GroupDonation, UUID> {

    Optional<GroupDonation> findByTrackingCode(String trackingCode);

    List<GroupDonation> findByNeedIdOrderByCreatedAtDesc(UUID needId);

    List<GroupDonation> findByCoordinatorUserIdOrderByCreatedAtDesc(UUID coordinatorUserId);

    List<GroupDonation> findByNeedOrganizationIdOrderByCreatedAtDesc(UUID organizationId);
}

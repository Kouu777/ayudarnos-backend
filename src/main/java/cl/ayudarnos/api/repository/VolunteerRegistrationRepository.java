package cl.ayudarnos.api.repository;

import cl.ayudarnos.api.entity.VolunteerRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VolunteerRegistrationRepository extends JpaRepository<VolunteerRegistration, UUID> {

    List<VolunteerRegistration> findByOpportunityIdOrderByCreatedAtDesc(UUID opportunityId);

    List<VolunteerRegistration> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<VolunteerRegistration> findByRegistrationCode(String registrationCode);
}

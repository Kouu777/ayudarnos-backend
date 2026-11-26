package cl.ayudarnos.api.repository;

import cl.ayudarnos.api.entity.AlertSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertSubscriptionRepository extends JpaRepository<AlertSubscription, UUID> {

    List<AlertSubscription> findByActiveTrue();

    List<AlertSubscription> findByUserIdAndActiveTrue(UUID userId);

    List<AlertSubscription> findByEmailAndActiveTrue(String email);
}

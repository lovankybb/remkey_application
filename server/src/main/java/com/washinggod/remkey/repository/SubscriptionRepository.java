package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.Subscription;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  Optional<Subscription> findByUserId(String userId);
}

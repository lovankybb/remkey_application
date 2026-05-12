package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.SubscriptionPackage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<SubscriptionPackage, Long> {

  Optional<SubscriptionPackage> findByName(String name);
}

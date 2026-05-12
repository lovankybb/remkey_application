package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, Long> {}

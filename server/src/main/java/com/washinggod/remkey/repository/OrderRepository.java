package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.Order;
import com.washinggod.remkey.enums.PaymentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Modifying
  @Query(
      """
            SELECT o
            FROM Order o
            WHERE o.userId = :user_id
            AND o.paymentStatus = :status
            """)
  List<Order> getOrderByUserIdAndStatus(
      @Param("user_id") String userId, @Param("status") PaymentStatus status);

  List<Order> getOrderByUserId(String userId);
}

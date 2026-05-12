package com.washinggod.remkey.entity;

import com.washinggod.remkey.enums.PaymentMethod;
import com.washinggod.remkey.enums.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String userId;

  @Column(name = "transaction_date")
  LocalDateTime transactionDate;

  @Column(name = "payment_status")
  @Enumerated(EnumType.STRING)
  PaymentStatus paymentStatus;

  @Column(name = "payment_method")
  @Enumerated(EnumType.STRING)
  PaymentMethod paymentMethod;

  @Column(name = "total_amount")
  BigDecimal totalAmount;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "orders_packages",
      joinColumns = @JoinColumn(name = "order_id"),
      inverseJoinColumns = @JoinColumn(name = "package_id"))
  Set<SubscriptionPackage> packages;
}

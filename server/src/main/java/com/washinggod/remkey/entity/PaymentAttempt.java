package com.washinggod.remkey.entity;

import com.washinggod.remkey.enums.PaymentMethod;
import com.washinggod.remkey.enums.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_attempts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentAttempt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "order_id")
  Long orderId;

  @Column(name = "payment_status")
  @Enumerated(EnumType.STRING)
  PaymentStatus paymentStatus;

  @Column(name = "payment_method")
  @Enumerated(EnumType.STRING)
  PaymentMethod paymentMethod;

  @Column(name = "gate_way_txn_id")
  String gateWayTxnId;

  @Column(name = "response_code")
  String responseCode;

  @Column(name = "response_message")
  String responseMessage;

  @Column(name = "redirect_url")
  String redirectUrl;

  @Column(name = "return_url")
  String returnUrl;

  @Column(name = "client_ip_address")
  String clientIpAddress;

  @Column(name = "request_payload", columnDefinition = "TEXT")
  String requestPayload;

  @Column(name = "response_payload", columnDefinition = "TEXT")
  String responsePayload;

  @Column(name = "amount")
  BigDecimal amount;

  @Column(name = "currency")
  String currency;

  @Column(name = "paid_at")
  LocalDateTime paidAt;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;
}

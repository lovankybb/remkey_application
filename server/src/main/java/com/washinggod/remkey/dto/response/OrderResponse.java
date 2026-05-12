package com.washinggod.remkey.dto.response;

import com.washinggod.remkey.enums.PaymentMethod;
import com.washinggod.remkey.enums.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {

  Long id;
  String userId;
  LocalDateTime transactionDate;

  PaymentStatus paymentStatus;
  PaymentMethod paymentMethod;
  BigDecimal totalAmount;
  Set<String> packages;

  LocalDateTime createdAt;
  LocalDateTime updatedAt;
}

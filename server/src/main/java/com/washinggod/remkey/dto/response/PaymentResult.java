package com.washinggod.remkey.dto.response;

import com.washinggod.remkey.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResult {
  long txnRef;
  long orderId;
  String code;
  String message;
  PaymentStatus paymentStatus;
  PaymentStatus orderPaymentStatus;
}

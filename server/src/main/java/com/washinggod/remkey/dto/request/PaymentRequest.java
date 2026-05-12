package com.washinggod.remkey.dto.request;

import com.washinggod.remkey.enums.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {
  Long orderId;
  PaymentMethod paymentMethod;
}

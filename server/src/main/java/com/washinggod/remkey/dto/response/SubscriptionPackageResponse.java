package com.washinggod.remkey.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionPackageResponse {
  Long id;
  String name;
  BigDecimal price;
  int quota;
  int duration;
  String description;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
}

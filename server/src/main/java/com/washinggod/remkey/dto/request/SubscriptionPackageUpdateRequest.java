package com.washinggod.remkey.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionPackageUpdateRequest {

  String name;
  float price;
  int quota;
  int duration;
  String description;
}

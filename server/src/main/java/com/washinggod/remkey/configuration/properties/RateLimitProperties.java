package com.washinggod.remkey.configuration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class RateLimitProperties {

  @Value("${app.sec.rate-limit.capacity}")
  private Long capacity;

  @Value("${app.sec.rate-limit.refill}")
  private Long refill;
}

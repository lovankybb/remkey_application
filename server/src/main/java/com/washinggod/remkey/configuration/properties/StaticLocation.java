package com.washinggod.remkey.configuration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class StaticLocation {

  @Value("${app.static-path.card.image}")
  private String cardImageLocation;

  @Value("${app.static-path.card.sound}")
  private String cardSoundLocation;
}

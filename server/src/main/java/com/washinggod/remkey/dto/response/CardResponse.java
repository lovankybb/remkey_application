package com.washinggod.remkey.dto.response;

import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardResponse {

  Long id;
  String question;
  String answer;
  String language;
  String topic;
  ImageResponse mainImage;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
}

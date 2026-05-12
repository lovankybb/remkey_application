package com.washinggod.remkey.dto.response;

import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardUserResponse {

  Long id;

  String userId;
  String question;
  String answer;
  String language;
  String topic;
  ImageResponse mainImage;

  Double retrievability;
  Double stability;
  Double difficulty;

  LocalDateTime lastReview;

  LocalDateTime nextReview;

  LocalDateTime createdAt;
  LocalDateTime updatedAt;
}

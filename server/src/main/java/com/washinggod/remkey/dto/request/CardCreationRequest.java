package com.washinggod.remkey.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardCreationRequest {

  String question;
  String answer;

  Long languageId;
  Long topicId;
}

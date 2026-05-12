package com.washinggod.remkey.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardReportResponse {

  Long id;
  Long cardId;
  String message;
  String moreDesc;
  LocalDateTime createdAt;
}

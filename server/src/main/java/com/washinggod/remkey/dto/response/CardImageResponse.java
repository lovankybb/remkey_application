package com.washinggod.remkey.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardImageResponse {

  Long id;
  String url;
  Long cardId;
  Long cardUserId;
}

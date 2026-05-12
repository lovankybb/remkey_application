package com.washinggod.remkey.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

  String id;
  String username;
  String email;
  List<String> roles;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
}

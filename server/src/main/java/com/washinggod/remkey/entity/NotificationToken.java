package com.washinggod.remkey.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "user_id", columnDefinition = "TEXT")
  String userId;

  @Column(columnDefinition = "TEXT")
  String token;
}

package com.washinggod.remkey.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscriptions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "user_id")
  String userId;

  @Column(name = "package_id")
  Long packageId;

  int quota;
  int used;

  @Column(name = "expired_time")
  LocalDateTime expiredTime;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;
}

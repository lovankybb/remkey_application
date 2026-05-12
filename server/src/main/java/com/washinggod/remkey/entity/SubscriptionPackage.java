package com.washinggod.remkey.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "packages")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionPackage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String name;
  BigDecimal price;
  int quota;
  int duration;

  @Column(name = "description", columnDefinition = "TEXT")
  String description;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;
}

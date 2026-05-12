package com.washinggod.remkey.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards_users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "user_id", columnDefinition = "TEXT")
  String userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "language_id")
  Language language;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "topic_id")
  Topic topic;

  String question;
  String answer;

  @OneToMany(mappedBy = "cardUser", cascade = CascadeType.ALL, orphanRemoval = true)
  Set<CardImage> images = new HashSet<>();

  @OneToMany(mappedBy = "cardUser", cascade = CascadeType.ALL)
  Set<Sound> sounds = new HashSet<>();

  Double stability;
  Double retrievability;
  Double difficulty;

  @Column(name = "last_review")
  LocalDateTime lastReview;

  @Column(name = "next_review")
  LocalDateTime nextReview;

  @Column(name = "notification_time")
  LocalDateTime notificationTime;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;
}

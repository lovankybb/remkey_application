package com.washinggod.remkey.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String question;
  String answer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "language_id")
  Language language;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "topic_id")
  Topic topic;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;

  @OneToMany(orphanRemoval = true, mappedBy = "card")
  List<CardImage> cardImages;
}

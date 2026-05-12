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
@Table(name = "sounds")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sound {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String url;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "card_id")
  Card card;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "card_user_id")
  CardUser cardUser;
}

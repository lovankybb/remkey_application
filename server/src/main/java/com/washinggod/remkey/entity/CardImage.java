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
@Table(name = "card_images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String url;

  @Column(name = "public_id")
  String publicId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "card_id")
  Card card;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "card_user_id")
  CardUser cardUser;
}

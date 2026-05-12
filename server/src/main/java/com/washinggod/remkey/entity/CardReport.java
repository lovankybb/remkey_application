package com.washinggod.remkey.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_reports")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardReport {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "card_id")
    Long cardId;

    String message;

    @Column(name = "more_desc")
    String moreDesc;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}

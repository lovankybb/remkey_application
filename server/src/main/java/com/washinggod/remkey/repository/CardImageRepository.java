package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.Card;
import com.washinggod.remkey.entity.CardImage;
import com.washinggod.remkey.entity.CardUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardImageRepository extends JpaRepository<CardImage, Long> {

  public Optional<CardImage> findByCard(Card card);

  public Optional<CardImage> findByCardUser(CardUser cardUser);
}

package com.washinggod.remkey.service;

import com.washinggod.remkey.dto.response.CardImageResponse;
import com.washinggod.remkey.entity.Card;
import com.washinggod.remkey.entity.CardImage;
import com.washinggod.remkey.entity.CardUser;
import com.washinggod.remkey.enums.FileType;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.CardImageMapper;
import com.washinggod.remkey.repository.CardImageRepository;
import com.washinggod.remkey.repository.CardRepository;
import com.washinggod.remkey.repository.CardUserRepository;
import com.washinggod.remkey.util.StorageFiles;
import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CardImageService {

  CardImageRepository cardImageRepository;

  CardRepository cardRepository;

  CardUserRepository cardUserRepository;

  CardImageMapper cardImageMapper;

  StorageFiles storageFiles;

  RedisTemplate<String, String> redisTemplate;

  @Transactional
  public CardImageResponse create(MultipartFile file, Long cardId) {

    Card card = this.getCardById(cardId);

    String url = storageFiles.storageFile(file, FileType.IMAGE);

    CardImage originCardImage = CardImage.builder().url(url).card(card).build();

    originCardImage = cardImageRepository.save(originCardImage);

    //    Get user's card id from redis that was saved in method create of file CardService
    String cardUserKey = "cardUser:" + cardId + ":id:";
    try {
      Long cardUserId = Long.valueOf(redisTemplate.opsForValue().get(cardUserKey));

      CardUser cardUser = this.getCardUserById(cardUserId);
      String newUrl = storageFiles.duplicateFile(url, FileType.IMAGE);

      CardImage cardUserImage = CardImage.builder().url(newUrl).cardUser(cardUser).build();

      cardImageRepository.save(cardUserImage);

      redisTemplate.delete(cardUserKey);
    } catch (Exception e) {
      log.error("ERROR: There's no user's card with key: {}", cardUserKey);
      throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }

    return this.generateCardImageResponse(originCardImage);
  }

  public CardImageResponse getCardImageByCard(Long cardId) {

    Card card = this.getCardById(cardId);
    return this.generateCardImageResponse(this.getCardImageByCard(card));
  }

  public CardImageResponse getCardImageByCardUser(Long cardUserId) {

    CardUser cardUser = this.getCardUserById(cardUserId);

    return this.generateCardImageResponse(this.getCardImageByCardUser(cardUser));
  }

  public CardImageResponse setCardUserImage(Long imageId, Long cardUserId) {

    CardImage originCardImage = this.getCardImageById(imageId);
    String newUrl = storageFiles.duplicateFile(originCardImage.getUrl(), FileType.IMAGE);

    CardUser cardUser = this.getCardUserById(cardUserId);

    CardImage cardImage = CardImage.builder().url(newUrl).cardUser(cardUser).build();

    return this.generateCardImageResponse(cardImageRepository.save(cardImage));
  }

  public CardImageResponse updateImageForCardUser() {
    return null;
  }

  private CardImage getCardImageById(Long id) {

    return this.cardImageRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.CARD_IMAGE_NOT_EXIST);
            });
  }

  private CardImage getCardImageByCardUser(CardUser cardUser) {
    return this.cardImageRepository
        .findByCardUser(cardUser)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.CARD_IMAGE_NOT_EXIST);
            });
  }

  private Card getCardById(Long id) {

    return this.cardRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.CARD_NOT_EXIST);
            });
  }

  private CardImage getCardImageByCard(Card card) {

    return this.cardImageRepository
        .findByCard(card)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.CARD_IMAGE_NOT_EXIST);
            });
  }

  private CardImageResponse generateCardImageResponse(CardImage cardImage) {

    CardImageResponse cardImageResponse = cardImageMapper.toCardImageResponse(cardImage);

    if (!Objects.isNull(cardImage.getCard())) {
      cardImageResponse.setCardId(cardImage.getCard().getId());
    }

    if (!Objects.isNull(cardImage.getCardUser())) {
      cardImageResponse.setCardUserId(cardImage.getCardUser().getId());
    }

    return cardImageResponse;
  }

  private CardUser getCardUserById(Long id) {

    return this.cardUserRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.CARD_NOT_EXIST);
            });
  }
}

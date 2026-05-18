package com.washinggod.remkey.service;

import com.washinggod.remkey.dto.request.UpdateCardUserImageRequest;
import com.washinggod.remkey.dto.request.UploadImageRequest;
import com.washinggod.remkey.dto.response.CardImageResponse;
import com.washinggod.remkey.entity.Card;
import com.washinggod.remkey.entity.CardImage;
import com.washinggod.remkey.entity.CardUser;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.CardImageMapper;
import com.washinggod.remkey.repository.CardImageRepository;
import com.washinggod.remkey.repository.CardRepository;
import com.washinggod.remkey.repository.CardUserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CardImageService {

  CardImageRepository cardImageRepository;

  CardRepository cardRepository;

  CardUserRepository cardUserRepository;

  CardImageMapper cardImageMapper;

  CloudinaryService cloudinaryService;

  RedisTemplate<String, String> redisTemplate;

  @Transactional
  public CardImageResponse create(Long cardId, UploadImageRequest request) {


    log.info("+++====== cardId: {}", cardId);
    log.info("secure_url: {}", request.getUrl());
    log.info("public_id: {}", request.getPublicId());

    Card card = this.getCardById(cardId);


    CardImage originCardImage = CardImage.builder()
            .url(request.getUrl())
            .publicId(request.getPublicId())
            .card(card).build();

    cardImageRepository.save(originCardImage);

    //    Get user's card id from redis that was saved in method create of file CardService
    String cardUserKey = "cardUser:" + cardId + ":id:";
    try {
      Long cardUserId = Long.valueOf(redisTemplate.opsForValue().get(cardUserKey));

      CardUser cardUser = this.getCardUserById(cardUserId);

      Map<String, String> cardUserImageResp  = this.cloudinaryService.duplicateImage(request.getUrl());
      CardImage cardUserImage = CardImage.builder().url(cardUserImageResp.get("secure_url"))
              .publicId(cardUserImageResp.get("public_id"))
              .cardUser(cardUser).build();

      cardImageRepository.save(cardUserImage);

      redisTemplate.delete(cardUserKey);
    } catch (Exception e) {
      e.printStackTrace();
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
    Map<String, String> newImage = cloudinaryService.duplicateImage(originCardImage.getUrl());

    CardUser cardUser = this.getCardUserById(cardUserId);

    CardImage cardImage = CardImage.builder().url(newImage.get("secure_url"))
            .publicId(newImage.get("public_id"))
            .cardUser(cardUser).build();

    return this.generateCardImageResponse(cardImageRepository.save(cardImage));
  }

  public CardImageResponse updateCardUserImage(Long imageId, UpdateCardUserImageRequest request) {

    CardImage image = this.getCardImageById(imageId);

    cloudinaryService.delete(image.getPublicId());

    image.setUrl(request.getUrl());
    image.setPublicId(request.getPublicId());

    return this.generateCardImageResponse(cardImageRepository.save(image));
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

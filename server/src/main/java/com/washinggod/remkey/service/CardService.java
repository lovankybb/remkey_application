package com.washinggod.remkey.service;

import com.washinggod.remkey.dto.request.*;
import com.washinggod.remkey.dto.response.CardResponse;
import com.washinggod.remkey.dto.response.ImageResponse;
import com.washinggod.remkey.entity.*;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.CardMapper;
import com.washinggod.remkey.repository.CardImageRepository;
import com.washinggod.remkey.repository.CardRepository;
import com.washinggod.remkey.repository.LanguageRepository;
import com.washinggod.remkey.repository.TopicRepository;
import com.washinggod.remkey.util.StorageFiles;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CardService {

  CardMapper cardMapper;

  CardRepository cardRepository;

  LanguageRepository languageRepository;

  TopicRepository topicRepository;

  CardUserService cardUserService;

  RedisTemplate<String, String> redisTemplate;

  CardImageRepository cardImageRepository;


  //    Get 10 item at first page
  Pageable pageable = PageRequest.of(0, 10);

  @Transactional
  public CardResponse create(CardCreationRequest request) {

    Card card = cardMapper.toCard(request);

    Language language = this.getLanguage(request.getLanguageId());
    card.setLanguage(language);

    Topic topic = this.getTopic(request.getTopicId());
    card.setTopic(topic);

    card.setCreatedAt(LocalDateTime.now());

    card = this.saveCard(card);
    String userId = this.getCurrUserId();

    InitCardForUserRequest initCardForUserRequest =
        InitCardForUserRequest.builder().cardId(card.getId()).build();

    CardUser cardUser = cardUserService.initCardForUser(initCardForUserRequest);

    //        Save this value to get CardUser when uploading image in the future  "cardUser:id:"
    String cardUserKey = "cardUser:" + card.getId() + ":id:";

    redisTemplate
        .opsForValue()
        .set(cardUserKey, cardUser.getId().toString(), Duration.ofMinutes(5));

    System.out.println("Save cardUser:"+card.getId()+":id:"+cardUser.getId()+ " successfully!");

    return this.generateCardResponse(card);
  }

  public List<CardResponse> getAll() {
    return cardRepository.findAll().stream().map(this::generateCardResponse).toList();
  }

  public CardResponse getCardResponseById(Long id){
    return this.generateCardResponse(this.getCardById(id));
  }

  public Slice<CardResponse> getNewestCards() {
    return this.cardRepository.findNewestCard(pageable).map(this::generateCardResponse);
  }

  public Slice<CardResponse> getNextCardsById(Long latestId) {

    return this.cardRepository.findNextCardById(latestId, pageable).map(this::generateCardResponse);
  }

  public void delete(Long id) {
    Card card = this.getCardById(id);
    cardRepository.delete(card);
  }

  public CardResponse update(Long id, CardUpdateRequest request) {

    Card card = this.getCardById(id);

    cardMapper.updateCard(card, request);

    Language language = this.getLanguage(request.getLanguageId());

    Topic topic = this.getTopic(request.getTopicId());

    card.setLanguage(language);
    card.setTopic(topic);

    return this.generateCardResponse(this.saveCard(card));
  }

  public List<CardResponse> getCardResponseByTopic(FindCardByTopicRequest request) {

    return cardRepository.findByTopic(request.getTopicId()).stream()
        .map(this::generateCardResponse)
        .toList();
  }

  public List<CardResponse> getCardResponseByLanguage(FindCardByLanguageRequest request) {

    return cardRepository.findByLanguage(request.getLanguageId()).stream()
        .map(this::generateCardResponse)
        .toList();
  }

  public List<CardResponse> getCardResponseByNamePattern(FindCardByQuestionPatternRequest request) {

    return cardRepository.findByQuestionPattern(request.getPattern()).stream()
        .map(this::generateCardResponse)
        .toList();
  }

  private CardResponse generateCardResponse(Card card) {

    CardResponse cardResponse = cardMapper.toCardResponse(card);

    cardResponse.setLanguage(card.getLanguage().getName());
    cardResponse.setTopic(card.getTopic().getName());

    Optional<CardImage> cardImage = cardImageRepository.findByCard(card);

    cardImage.ifPresent(
        image -> {
          cardResponse.setMainImage(
              ImageResponse.builder().id(image.getId()).url(image.getUrl()).build());
        });

    return cardResponse;
  }

  private Card getCardById(Long id) {
    return cardRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.CARD_NOT_EXIST);
            });
  }

  private Card saveCard(Card card) {

    card.setUpdatedAt(LocalDateTime.now());
    try {
      return cardRepository.save(card);
    } catch (DataIntegrityViolationException exception) {
      throw new AppException(ErrorCode.CARD_NOT_EXIST);
    }
  }

  private Topic getTopic(Long id) {
    return topicRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.TOPIC_NOT_EXIST);
            });
  }

  private Language getLanguage(Long id) {
    return languageRepository
        .findById(id)
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.LANGUAGE_NOT_EXIST);
            });
  }

  private String getCurrUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getName();
  }
}

package com.washinggod.remkey.service;

import com.washinggod.remkey.dto.request.AddToMyListRequest;
import com.washinggod.remkey.dto.request.CardUserUpdateRequest;
import com.washinggod.remkey.dto.request.HandleCardAfterStudyRequest;
import com.washinggod.remkey.dto.request.InitCardForUserRequest;
import com.washinggod.remkey.dto.response.CardUserResponse;
import com.washinggod.remkey.dto.response.ImageResponse;
import com.washinggod.remkey.entity.*;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.mapper.CardUserMapper;
import com.washinggod.remkey.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CardUserService {

    CardRepository cardRepository;

    CardUserRepository cardUserRepository;

    CardUserMapper cardUserMapper;

    FSRSService fsrsService;

    LanguageRepository languageRepository;

    TopicRepository topicRepository;

    CardImageRepository cardImageRepository;

    CardImageService cardImageService;

    CloudinaryService cloudinaryService;


    public CardUser initCardForUser(InitCardForUserRequest request) {

        Card card = this.getCardById(request.getCardId());

        CardUser cardUser =
                CardUser.builder()
                        .userId(request.getUserId())
                        .language(card.getLanguage())
                        .topic(card.getTopic())
                        .question(card.getQuestion())
                        .answer(card.getAnswer())
                        .build();

        LocalDateTime now = LocalDateTime.now();

        fsrsService.initValue(cardUser, now);

        return this.saveCardUser(cardUser);
    }

    public CardUserResponse handleCardAfterStudy(HandleCardAfterStudyRequest request) {

        CardUser cardUser = this.getCardUserById(request.getCardUserId());

        int rating = request.getRating();

        double difficulty = fsrsService.updateDifficulty(cardUser.getDifficulty(), rating);
        double stability =
                fsrsService.updateStability(
                        rating, cardUser.getStability(), cardUser.getRetrievability(), difficulty);

        double nextReviewDay = fsrsService.calculateNextReview(stability);
        long nextReviewSecond = (long) nextReviewDay * 24 * 60 * 60;

        LocalDateTime nextReview = LocalDateTime.now().plusSeconds(nextReviewSecond);

        LocalDateTime now = LocalDateTime.now();

        double retrievability = fsrsService.updateRetrievability(stability, cardUser.getLastReview(), now);

        cardUser.setDifficulty(difficulty);
        cardUser.setStability(stability);
        cardUser.setNextReview(nextReview);
        cardUser.setRetrievability(retrievability);
        cardUser.setLastReview(LocalDateTime.now());

        return this.generateCardUserResponse(this.saveCardUser(cardUser));
    }


    public CardUserResponse addToMyList(AddToMyListRequest request) {

        String userId = this.getCurrentUserId();
        CardUser cardUser =
                this.initCardForUser(
                        InitCardForUserRequest.builder().userId(userId).cardId(request.getCardId()).build());

        Card card = this.getCardById(request.getCardId());

        Optional<CardImage> cardImage = this.cardImageRepository.findByCard(card);
        cardImage.ifPresent(image -> {
            cardImageService.setCardUserImage(image.getId(), cardUser.getId());

        });

        return this.generateCardUserResponse(cardUser);
    }

    public CardUserResponse update(Long id, CardUserUpdateRequest request) {

        CardUser cardUser = this.getCardUserById(id);

        cardUser.setQuestion(request.getQuestion());
        cardUser.setAnswer(request.getAnswer());

        Language language = this.getLanguageById(request.getLanguageId());
        cardUser.setLanguage(language);
        Topic topic = this.getTopicById(request.getTopicId());
        cardUser.setTopic(topic);

        return this.generateCardUserResponse(this.saveCardUser(cardUser));
    }


    public void delete(Long id) {
        Optional<CardUser> cardUserOptional = cardUserRepository.findById(id);
        cardUserOptional.ifPresent(cardUser -> {
            cardUser.getImages().forEach(image -> {
                cloudinaryService.delete(image.getPublicId());
            });

            cardUserRepository.delete(cardUser);
        });
    }

    public List<CardUserResponse> getAllMyStudyCardsNow() {

        String userId = this.getCurrentUserId();
        return cardUserRepository.findStudyCardNow(LocalDateTime.now(), userId).stream()
                .map(this::generateCardUserResponse)
                .toList();
    }

    public List<CardUserResponse> getAllMyCards() {

        String userId = this.getCurrentUserId();
        return cardUserRepository.findByUserId(userId).stream()
                .map(this::generateCardUserResponse)
                .toList();
    }

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private CardUser saveCardUser(CardUser cardUser) {

        cardUser.setUpdatedAt(LocalDateTime.now());
        try {
            return cardUserRepository.save(cardUser);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.SAVE_CARD_FAILED);
        }
    }

    private CardUserResponse generateCardUserResponse(CardUser cardUser) {

        CardUserResponse cardUserResponse = cardUserMapper.toCardUserResponse(cardUser);
        cardUserResponse.setLanguage(cardUser.getLanguage().getName());
        cardUserResponse.setTopic(cardUser.getTopic().getName());

        Optional<CardImage> cardImage = cardImageRepository.findByCardUser(cardUser);

        cardImage.ifPresent(
                image -> {
                    cardUserResponse.setMainImage(
                            ImageResponse.builder().id(image.getId()).url(image.getUrl()).build());
                });

        return cardUserResponse;
    }

    @PostAuthorize("returnObject.userId == authentication.name")
    private CardUser getCardUserById(Long id) {
        return cardUserRepository
                .findById(id)
                .orElseThrow(
                        () -> {
                            return new AppException(ErrorCode.CARD_NOT_EXIST);
                        });
    }

    private Language getLanguageById(Long id) {
        return languageRepository
                .findById(id)
                .orElseThrow(
                        () -> {
                            return new AppException(ErrorCode.LANGUAGE_NOT_EXIST);
                        });
    }

    private Topic getTopicById(Long id) {
        return topicRepository
                .findById(id)
                .orElseThrow(
                        () -> {
                            return new AppException(ErrorCode.TOPIC_NOT_EXIST);
                        });
    }

    private Card getCardById(Long id) {
        return cardRepository
                .findById(id)
                .orElseThrow(
                        () -> {
                            return new AppException(ErrorCode.CARD_NOT_EXIST);
                        });
    }
}

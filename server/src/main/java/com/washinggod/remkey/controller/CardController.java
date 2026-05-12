package com.washinggod.remkey.controller;

import com.washinggod.remkey.dto.request.*;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.CardImageResponse;
import com.washinggod.remkey.dto.response.CardResponse;
import com.washinggod.remkey.service.CardImageService;
import com.washinggod.remkey.service.CardService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/cards")
public class CardController {

  CardService cardService;

  CardImageService cardImageService;

  @PostMapping
  public ApiResponse<CardResponse> create(@RequestBody CardCreationRequest request) {
    ApiResponse<CardResponse> apiResponse = new ApiResponse<>();
    CardResponse cardResponse = cardService.create(request);
    apiResponse.setBody(cardResponse);
    return apiResponse;
  }

  @GetMapping
  public ApiResponse<List<CardResponse>> getAll() {
    ApiResponse<List<CardResponse>> apiResponse = new ApiResponse<>();
    List<CardResponse> cardResponses = cardService.getAll();
    apiResponse.setBody(cardResponses);
    return apiResponse;
  }


  @GetMapping("/{id}")
  public ApiResponse<CardResponse> findById(@PathVariable Long id) {
    ApiResponse<CardResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(cardService.getCardResponseById(id));
    return apiResponse;
  }

  @GetMapping("/find-by-topic")
  public ApiResponse<List<CardResponse>> findByTopic(@RequestBody FindCardByTopicRequest request) {
    ApiResponse<List<CardResponse>> apiResponse = new ApiResponse<>();
    List<CardResponse> cardResponses = cardService.getCardResponseByTopic(request);
    apiResponse.setBody(cardResponses);
    return apiResponse;
  }

  @GetMapping("/find-by-language")
  public ApiResponse<List<CardResponse>> findByLanguage(
      @RequestBody FindCardByLanguageRequest request) {
    ApiResponse<List<CardResponse>> apiResponse = new ApiResponse<>();
    List<CardResponse> cardResponses = cardService.getCardResponseByLanguage(request);
    apiResponse.setBody(cardResponses);
    return apiResponse;
  }

  @GetMapping("/find-by-pattern")
  public ApiResponse<List<CardResponse>> findByQuestionPattern(
      @RequestBody FindCardByQuestionPatternRequest request) {
    ApiResponse<List<CardResponse>> apiResponse = new ApiResponse<>();
    List<CardResponse> cardResponses = cardService.getCardResponseByNamePattern(request);
    apiResponse.setBody(cardResponses);
    return apiResponse;
  }

  @PutMapping("/{id}")
  public ApiResponse<CardResponse> update(
      @PathVariable Long id, @RequestBody CardUpdateRequest request) {
    ApiResponse<CardResponse> apiResponse = new ApiResponse<>();
    CardResponse cardResponse = cardService.update(id, request);
    apiResponse.setBody(cardResponse);
    return apiResponse;
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {
    cardService.delete(id);
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage("Delete card successfully");
    return apiResponse;
  }

  @GetMapping("/explore")
  public ApiResponse<Slice<CardResponse>> getNewestCards() {
    ApiResponse<Slice<CardResponse>> apiResponse = new ApiResponse<>();
    Slice<CardResponse> cardResponses = cardService.getNewestCards();
    apiResponse.setBody(cardResponses);
    return apiResponse;
  }

  @GetMapping("/explore/{id}")
  public ApiResponse<Slice<CardResponse>> getNextCardsFromId(@PathVariable("id") Long id) {
    ApiResponse<Slice<CardResponse>> apiResponse = new ApiResponse<>();
    Slice<CardResponse> cardResponses = cardService.getNextCardsById(id);
    apiResponse.setBody(cardResponses);
    return apiResponse;
  }

  @PostMapping("/{id}/upload")
  public ApiResponse<CardImageResponse> uploadIllustrativeImage(
      @PathVariable("id") Long id, @RequestParam("file") MultipartFile file) {
    ApiResponse<CardImageResponse> apiResponse = new ApiResponse<>();
    CardImageResponse imageResponse = cardImageService.create(file, id);
    apiResponse.setBody(imageResponse);
    return apiResponse;
  }
}

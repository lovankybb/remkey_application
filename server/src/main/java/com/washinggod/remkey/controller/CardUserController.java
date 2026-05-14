package com.washinggod.remkey.controller;

import com.washinggod.remkey.dto.request.AddToMyListRequest;
import com.washinggod.remkey.dto.request.CardUserUpdateRequest;
import com.washinggod.remkey.dto.request.HandleCardAfterStudyRequest;
import com.washinggod.remkey.dto.request.UpdateCardUserImageRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.CardImageResponse;
import com.washinggod.remkey.dto.response.CardUserResponse;
import com.washinggod.remkey.service.CardImageService;
import com.washinggod.remkey.service.CardUserService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/my-cards")
public class CardUserController {

  CardUserService cardUserService;

  CardImageService cardImageService;

  @GetMapping
  public ApiResponse<List<CardUserResponse>> getMyCards() {

    ApiResponse<List<CardUserResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setBody(cardUserService.getAllMyCards());

    return apiResponse;
  }

  @GetMapping("/study")
  public ApiResponse<List<CardUserResponse>> getMyStudyCardNow() {

    ApiResponse<List<CardUserResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setBody(cardUserService.getAllMyStudyCardsNow());

    return apiResponse;
  }

  @PostMapping("/add")
  public ApiResponse<CardUserResponse> addToMyList(@RequestBody AddToMyListRequest request) {

    ApiResponse<CardUserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(cardUserService.addToMyList(request));

    return apiResponse;
  }

  @PutMapping("/{id}")
  public ApiResponse<CardUserResponse> update(
      @PathVariable Long id, @RequestBody CardUserUpdateRequest request) {

    ApiResponse<CardUserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(cardUserService.update(id, request));

    return apiResponse;
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {

    cardUserService.delete(id);
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage("Delete user's card successfully");

    return apiResponse;
  }

  @PostMapping("/update-next-review")
  public ApiResponse<CardUserResponse> updateNextReview(
      @RequestBody HandleCardAfterStudyRequest request) {

    ApiResponse<CardUserResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(cardUserService.handleCardAfterStudy(request));

    return apiResponse;
  }

  @PostMapping("/upload/{cardUserId}/{imageId}")
  public ApiResponse<CardImageResponse> uploadCardImageForCardUser(
      @PathVariable("cardUserId") Long cardUserId, @PathVariable("imageId") Long imageId) {

    ApiResponse<CardImageResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(cardImageService.setCardUserImage(imageId, cardUserId));
    return apiResponse;
  }


  @PutMapping("/upload/{imageId}/update-image")
  public ApiResponse<CardImageResponse> updateCardUserImage(
          @PathVariable("imageId") Long imageId, @RequestBody UpdateCardUserImageRequest request) {
    ApiResponse<CardImageResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(cardImageService.updateCardUserImage(imageId, request));
    return apiResponse;
  }
}


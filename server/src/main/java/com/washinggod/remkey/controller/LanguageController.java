package com.washinggod.remkey.controller;

import com.washinggod.remkey.dto.request.FindLanguageByNameRequest;
import com.washinggod.remkey.dto.request.LanguageCreationRequest;
import com.washinggod.remkey.dto.request.LanguageUpdateRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.LanguageResponse;
import com.washinggod.remkey.service.LanguageService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/languages")
public class LanguageController {

  LanguageService languageService;

  @GetMapping
  public ApiResponse<List<LanguageResponse>> getAll() {
    ApiResponse<List<LanguageResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setBody(languageService.getAll());
    return apiResponse;
  }

  @PostMapping
  public ApiResponse<LanguageResponse> create(@RequestBody LanguageCreationRequest request) {

    ApiResponse<LanguageResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(languageService.create(request));
    return apiResponse;
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {

    languageService.delete(id);
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage("Delete language successfully");
    return apiResponse;
  }

  @GetMapping("/{id}")
  public ApiResponse<LanguageResponse> getRoleById(@PathVariable Long id) {

    ApiResponse<LanguageResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(languageService.geLanguageResponseById(id));
    return apiResponse;
  }

  @PutMapping("/{id}")
  public ApiResponse<LanguageResponse> update(
      @PathVariable Long id, @RequestBody LanguageUpdateRequest request) {

    ApiResponse<LanguageResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(languageService.update(id, request));
    return apiResponse;
  }

  @PostMapping("/find-by-name")
  public ApiResponse<LanguageResponse> getLanguageByName(
      @RequestBody FindLanguageByNameRequest request) {

    ApiResponse<LanguageResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(languageService.getLanguageResponseByName(request));
    return apiResponse;
  }
}

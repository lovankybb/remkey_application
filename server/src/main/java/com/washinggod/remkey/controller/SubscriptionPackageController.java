package com.washinggod.remkey.controller;

import com.washinggod.remkey.dto.request.FindPackageByNameRequest;
import com.washinggod.remkey.dto.request.SubscriptionPackageCreationRequest;
import com.washinggod.remkey.dto.request.SubscriptionPackageUpdateRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.SubscriptionPackageResponse;
import com.washinggod.remkey.service.SubscriptionPackageService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/packages")
public class SubscriptionPackageController {

  SubscriptionPackageService packageService;

  @GetMapping
  public ApiResponse<List<SubscriptionPackageResponse>> getAll() {
    ApiResponse<List<SubscriptionPackageResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setBody(packageService.getAll());
    return apiResponse;
  }

  @GetMapping("/{id}")
  public ApiResponse<SubscriptionPackageResponse> findById(@PathVariable Long id) {
    ApiResponse<SubscriptionPackageResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(packageService.getSubscriptionPackageResponseById(id));
    return apiResponse;
  }

  @GetMapping("/find-by-name")
  public ApiResponse<SubscriptionPackageResponse> findByName(
      @RequestBody FindPackageByNameRequest request) {
    ApiResponse<SubscriptionPackageResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(packageService.getSubscriptionPackageResponseByName(request));
    return apiResponse;
  }

  @PostMapping
  public ApiResponse<SubscriptionPackageResponse> create(
      @RequestBody SubscriptionPackageCreationRequest request) {
    ApiResponse<SubscriptionPackageResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(packageService.create(request));
    return apiResponse;
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {
    packageService.delete(id);
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage("Delete package success");
    return apiResponse;
  }

  @PutMapping("/{id}")
  public ApiResponse<SubscriptionPackageResponse> update(
      @PathVariable Long id, @RequestBody SubscriptionPackageUpdateRequest request) {
    ApiResponse<SubscriptionPackageResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(packageService.update(id, request));
    return apiResponse;
  }
}

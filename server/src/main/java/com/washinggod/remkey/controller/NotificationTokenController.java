package com.washinggod.remkey.controller;

import com.washinggod.remkey.dto.request.NotificationTokenCreationRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.service.FmcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fire-base")
public class NotificationTokenController {

  @Autowired FmcService fmcService;

  @PostMapping
  public ApiResponse<Void> createNotificationToken(
      @RequestBody NotificationTokenCreationRequest request) {
    fmcService.createNotificationToken(request);
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage("Create notification token successfully.");
    return apiResponse;
  }
}

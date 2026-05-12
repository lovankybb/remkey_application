package com.washinggod.remkey.controller;

import com.washinggod.remkey.dto.request.FindTopicByNameRequest;
import com.washinggod.remkey.dto.request.TopicCreationRequest;
import com.washinggod.remkey.dto.request.TopicUpdateRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.TopicResponse;
import com.washinggod.remkey.service.TopicService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/topics")
public class TopicController {

  TopicService topicService;

  @GetMapping
  public ApiResponse<List<TopicResponse>> getAll() {
    ApiResponse<List<TopicResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setBody(topicService.getAll());
    return apiResponse;
  }

  @PostMapping
  public ApiResponse<TopicResponse> create(@RequestBody TopicCreationRequest request) {

    ApiResponse<TopicResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(topicService.create(request));
    return apiResponse;
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {

    topicService.delete(id);
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage("Delete topic successfully");
    return apiResponse;
  }

  @GetMapping("/{id}")
  public ApiResponse<TopicResponse> getRoleById(@PathVariable Long id) {

    ApiResponse<TopicResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(topicService.getTopicResponseById(id));
    return apiResponse;
  }

  @PutMapping("/{id}")
  public ApiResponse<TopicResponse> update(
      @PathVariable Long id, @RequestBody TopicUpdateRequest request) {

    ApiResponse<TopicResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(topicService.update(id, request));
    return apiResponse;
  }

  @PostMapping("/find-by-name")
  public ApiResponse<TopicResponse> getTopicByName(@RequestBody FindTopicByNameRequest request) {

    ApiResponse<TopicResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(topicService.getTopicResponseByName(request));
    return apiResponse;
  }
}

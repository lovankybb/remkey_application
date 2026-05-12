package com.washinggod.remkey.controller;

import com.washinggod.remkey.dto.request.PermissionCreationRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.PermissionResponse;
import com.washinggod.remkey.service.PermissionService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/permissions")
public class PermissionController {

  PermissionService permissionService;

  @GetMapping
  public ApiResponse<List<PermissionResponse>> getAll() {

    ApiResponse<List<PermissionResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setBody(permissionService.getAll());
    return apiResponse;
  }

  @PostMapping
  public ApiResponse<PermissionResponse> create(@RequestBody PermissionCreationRequest request) {

    ApiResponse<PermissionResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(permissionService.create(request));
    return apiResponse;
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {

    permissionService.delete(id);
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage("Delete permission successfully");
    return apiResponse;
  }
}

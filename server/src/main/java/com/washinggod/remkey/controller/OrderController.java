package com.washinggod.remkey.controller;

import com.washinggod.remkey.dto.request.FindOrderByPaymentStatusRequest;
import com.washinggod.remkey.dto.request.OrderCreationRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.OrderResponse;
import com.washinggod.remkey.service.OrderService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/orders")
public class OrderController {

  OrderService orderService;

  @GetMapping
  public ApiResponse<List<OrderResponse>> getAll() {
    ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setBody(orderService.getAll());
    return apiResponse;
  }

  @GetMapping("/{id}")
  public ApiResponse<OrderResponse> getOrderById(@PathVariable Long id) {
    ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(orderService.getOrderResponseById(id));
    return apiResponse;
  }

  @PostMapping
  public ApiResponse<OrderResponse> create(@RequestBody OrderCreationRequest request) {
    ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(orderService.create(request));
    return apiResponse;
  }

  @GetMapping("/my-orders")
  public ApiResponse<List<OrderResponse>> getMyOrders() {
    ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setBody(orderService.getOrderResponsesByUserId());
    return apiResponse;
  }

  @GetMapping("/my-orders/status")
  public ApiResponse<List<OrderResponse>> getMyOrdersByStatus(
      @RequestBody FindOrderByPaymentStatusRequest request) {
    ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
    apiResponse.setBody(orderService.getOrderByPaymentStatus(request));
    return apiResponse;
  }

  @PostMapping("/{id}/cancel")
  public ApiResponse<OrderResponse> getMyOrdersByStatus(@PathVariable Long id) {
    ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(orderService.cancel(id));
    return apiResponse;
  }

  @DeleteMapping("/{id}")
  public ApiResponse<Void> delete(@PathVariable Long id) {
    orderService.delete(id);
    ApiResponse<Void> apiResponse = new ApiResponse<>();
    apiResponse.setMessage("Delete order successfully");
    return apiResponse;
  }
}

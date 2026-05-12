package com.washinggod.remkey.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.washinggod.remkey.dto.request.PaymentRequest;
import com.washinggod.remkey.dto.response.ApiResponse;
import com.washinggod.remkey.dto.response.PaymentResponse;
import com.washinggod.remkey.dto.response.PaymentResult;
import com.washinggod.remkey.enums.PaymentMethod;
import com.washinggod.remkey.service.OrderService;
import com.washinggod.remkey.service.impl.MomoService;
import com.washinggod.remkey.service.impl.VNPAYService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/payments")
public class PaymentController {

  OrderService orderService;

  MomoService momoService;

  VNPAYService vnpayService;

  @PostMapping
  public ApiResponse<PaymentResponse> pay(
      @RequestBody PaymentRequest paymentRequest, HttpServletRequest servletRequest) {

    ApiResponse<PaymentResponse> apiResponse = new ApiResponse<>();
    apiResponse.setBody(orderService.checkout(paymentRequest, servletRequest));
    return apiResponse;
  }

  @GetMapping("/vnpay-ipn")
  public ResponseEntity<Map<String, String>> vnpayIpn(@RequestParam Map<String, String> params)
      throws JsonProcessingException {

    PaymentResult paymentResult = vnpayService.ipnHandle(params);
    Map<String, String> result =
        orderService.handlePaymentResult(paymentResult, PaymentMethod.VNPAY);
    return ResponseEntity.ok(result);
  }

  @PostMapping("/momo-ipn")
  public ResponseEntity<Map<String, String>> momoIpn(@RequestBody Map<String, String> params)
      throws JsonProcessingException {
    log.info("INFO: MOMO SENT IPN REQUEST");
    PaymentResult paymentResult = momoService.ipnHandle(params);
    Map<String, String> result =
        orderService.handlePaymentResult(paymentResult, PaymentMethod.MOMO);
    return ResponseEntity.ok(result);
  }
}

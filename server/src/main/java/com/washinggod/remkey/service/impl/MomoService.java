package com.washinggod.remkey.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.washinggod.remkey.configuration.properties.MomoProperties;
import com.washinggod.remkey.dto.response.PaymentResponse;
import com.washinggod.remkey.dto.response.PaymentResult;
import com.washinggod.remkey.entity.Order;
import com.washinggod.remkey.entity.PaymentAttempt;
import com.washinggod.remkey.enums.MomoResponseCode;
import com.washinggod.remkey.enums.PaymentMethod;
import com.washinggod.remkey.enums.PaymentStatus;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.repository.PaymentAttemptRepository;
import com.washinggod.remkey.service.PaymentService;
import com.washinggod.remkey.util.SignatureService;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class MomoService implements PaymentService {

  final PaymentAttemptRepository paymentAttemptRepository;

  final SignatureService signatureService;

  final RestTemplate restTemplate;

  final MomoProperties momoProperties;

  final ObjectMapper objectMapper;

  @Override
  public PaymentResponse pay(Order order, String ipAddress) throws JsonProcessingException {

    PaymentAttempt paymentAttempt =
        PaymentAttempt.builder()
            .orderId(order.getId())
            .paymentMethod(this.getMethod())
            .amount(order.getTotalAmount())
            .currency("VND")
            .paymentStatus(PaymentStatus.PENDING)
            .clientIpAddress(ipAddress)
            .returnUrl(momoProperties.getReturnUrl())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    paymentAttempt = paymentAttemptRepository.save(paymentAttempt);
    log.info("INFO: CREATE PAYMENT ATTEMPT SUCCESS");

    String orderId = "REMKEY_" + System.currentTimeMillis() + "_" + String.valueOf(order.getId());
    String extraData = "";
    String requestType = "payWithMethod";
    String requestId = String.valueOf(paymentAttempt.getId());
    String orderInfo = "Pay order " + orderId;
    long amount = order.getTotalAmount().longValue();
    String accessKey = momoProperties.getAccessKey();
    String notifyUrl = momoProperties.getNotifyUrl();
    String partnerCode = momoProperties.getPartnerCode();
    String returnUrl = momoProperties.getReturnUrl();
    String secretKey = momoProperties.getSecretKey();
    String apiUrl = momoProperties.getApiUrl();

    String rawSignature =
        "accessKey="
            + accessKey
            + "&amount="
            + amount
            + "&extraData="
            + extraData
            + "&ipnUrl="
            + notifyUrl
            + "&orderId="
            + orderId
            + "&orderInfo="
            + orderInfo
            + "&partnerCode="
            + partnerCode
            + "&redirectUrl="
            + returnUrl
            + "&requestId="
            + requestId
            + "&requestType="
            + requestType;

    String signature = signatureService.hmacSHA256(secretKey.trim(), rawSignature);
    log.info("INFO: BUILD SIGNATURE SUCCESS");
    log.info("INFO: RAW_SIGNATURE: {}", rawSignature);
    log.info("INFO: SECRET_KEY: {}", secretKey);
    log.info("INFO: SIGNATURE: {}", signature);

    Map<String, Object> body = new HashMap<>();
    body.put("accessKey", accessKey);
    body.put("amount", amount);
    body.put("extraData", extraData);
    body.put("ipnUrl", notifyUrl);
    body.put("orderId", orderId);
    body.put("orderInfo", orderInfo);
    body.put("partnerCode", partnerCode);
    body.put("redirectUrl", returnUrl);
    body.put("requestId", requestId);
    body.put("requestType", requestType);
    body.put("signature", signature);

    log.info("INFO: BUILD BODY SUCCESS");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
    log.info("INFO: " + objectMapper.writeValueAsString(entity));

    ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

    log.info("INFO: REQUEST TO MOMO API SUCCESS");
    log.info("INFO: MOMO_RESPONSE_BODY: {}", response.getBody());
    String payUrl = (String) Objects.requireNonNull(response.getBody()).get("payUrl");

    paymentAttempt.setRedirectUrl(payUrl);
    paymentAttempt.setRequestPayload(objectMapper.writeValueAsString(entity));
    paymentAttempt.setUpdatedAt(LocalDateTime.now());
    paymentAttemptRepository.save(paymentAttempt);

    return new PaymentResponse(payUrl, requestId);
  }

  @Override
  public PaymentMethod getMethod() {
    return PaymentMethod.MOMO;
  }

  @Override
  public PaymentResult ipnHandle(Map<String, String> params) throws JsonProcessingException {

    //        check data integration
    String momoSignature = params.get("signature");
    String requestId = params.get("requestId");
    String momoAmount = params.get("amount");
    String resultCode = params.get("resultCode");
    String message = params.get("message");
    String transId = params.get("transId");
    String responseTime = params.get("responseTime");
    String accessKey = momoProperties.getAccessKey();
    String secretKey = momoProperties.getSecretKey();

    String raw =
        "accessKey="
            + accessKey
            + "&amount="
            + momoAmount
            + "&extraData="
            + params.get("extraData")
            + "&message="
            + message
            + "&orderId="
            + params.get("orderId")
            + "&orderInfo="
            + params.get("orderInfo")
            + "&orderType="
            + params.get("orderType")
            + "&partnerCode="
            + params.get("partnerCode")
            + "&payType="
            + params.get("payType")
            + "&requestId="
            + requestId
            + "&responseTime="
            + responseTime
            + "&resultCode="
            + resultCode
            + "&transId="
            + transId;

    log.info("INFO: MOMO_SIGNATURE: {}", momoSignature);
    log.info("INFO: MOMO_REQUEST_OBJECT: {}", objectMapper.writeValueAsString(params));
    String serverSignature = signatureService.hmacSHA256(secretKey.trim(), raw);
    log.info("INFO: SERVER_SIGNATURE: {}", serverSignature);
    if (!serverSignature.equals(momoSignature)) {
      log.error("ERROR: SIGNATURE INVALID!");
      throw new AppException(ErrorCode.SIGNATURE_INVALID);
    }

    PaymentAttempt paymentAttempt =
        paymentAttemptRepository
            .findById(Long.parseLong(requestId))
            .orElseThrow(
                () -> {
                  log.error("ERROR: PAYMENT ATTEMPT DOESN'T EXIST!");
                  return new AppException(ErrorCode.PAYMENT_ATTEMPT_NOT_EXIST);
                });

    //        check amount match
    BigDecimal dbAmount = paymentAttempt.getAmount();
    if (!(dbAmount.compareTo(BigDecimal.valueOf(Double.parseDouble(momoAmount))) == 0)) {
      log.error("ERROR: AMOUNT MISMATCH!");
      throw new AppException(ErrorCode.AMOUNT_MISMATCH);
    }

    MomoResponseCode momoResponseCode = MomoResponseCode.from(resultCode);
    PaymentStatus paymentStatus = momoResponseCode.getPaymentStatus();
    PaymentStatus orderPaymentStatus = momoResponseCode.getOrderPaymentStatus();
    log.info("INFO: ORDER_PAYMENT_STATUS: {}", orderPaymentStatus);
    //        Build response payload

    String responsePayload = objectMapper.writeValueAsString(params);

    paymentAttempt.setResponsePayload(responsePayload);
    paymentAttempt.setGateWayTxnId(transId);
    paymentAttempt.setUpdatedAt(LocalDateTime.now());
    paymentAttempt.setPaymentStatus(paymentStatus);
    paymentAttempt.setResponseCode(resultCode);
    paymentAttempt.setResponseMessage(message);

    //        Build paidAt
    if (paymentStatus == PaymentStatus.SUCCESS) {
      LocalDateTime paidAt =
          LocalDateTime.ofInstant(
              Instant.ofEpochMilli(Long.parseLong(responseTime)), ZoneId.systemDefault());
      paymentAttempt.setPaidAt(paidAt);
    }

    paymentAttemptRepository.save(paymentAttempt);
    log.info("INFO: UPDATE PAYMENT ATTEMPT SUCCESS");
    return PaymentResult.builder()
        .orderId(paymentAttempt.getOrderId())
        .code(resultCode)
        .message(message)
        .paymentStatus(paymentStatus)
        .orderPaymentStatus(orderPaymentStatus)
        .build();
  }

  @Override
  public Map<String, String> handlePaymentResult(PaymentResult paymentResult) {
    Map<String, String> map = new HashMap<>();
    map.put("resultCode:", paymentResult.getCode());
    map.put("message:", paymentResult.getMessage());
    return map;
  }
}

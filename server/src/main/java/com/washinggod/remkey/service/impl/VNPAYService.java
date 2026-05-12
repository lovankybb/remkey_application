package com.washinggod.remkey.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.washinggod.remkey.configuration.properties.VnpayProperties;
import com.washinggod.remkey.dto.response.PaymentResponse;
import com.washinggod.remkey.dto.response.PaymentResult;
import com.washinggod.remkey.entity.Order;
import com.washinggod.remkey.entity.PaymentAttempt;
import com.washinggod.remkey.enums.PaymentMethod;
import com.washinggod.remkey.enums.PaymentStatus;
import com.washinggod.remkey.enums.VNPayResponseCode;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import com.washinggod.remkey.repository.PaymentAttemptRepository;
import com.washinggod.remkey.service.PaymentService;
import com.washinggod.remkey.util.SignatureService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class VNPAYService implements PaymentService {

  final VnpayProperties vnpayProperties;

  final SignatureService signatureService;

  final PaymentAttemptRepository paymentAttemptRepository;

  final ObjectMapper objectMapper;

  final String DEFAULT_CURRENCY = "VND";

  @Override
  public PaymentResponse pay(Order order, String ipAddress) throws JsonProcessingException {

    //        parse ipv6 to ipv4
    if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
      ipAddress = "127.0.0.1";
    }

    String returnUrl = vnpayProperties.getReturnUrl();
    int expireTimeConfig = vnpayProperties.getExpireTimeConfig();
    String tmnCode = vnpayProperties.getTmnCode();
    String secretKey = vnpayProperties.getSecretKey();
    String payUrl = vnpayProperties.getPayUrl();

    PaymentAttempt paymentAttempt =
        PaymentAttempt.builder()
            .orderId(order.getId())
            .paymentMethod(this.getMethod())
            .amount(order.getTotalAmount())
            .currency(DEFAULT_CURRENCY)
            .paymentStatus(PaymentStatus.PENDING)
            .clientIpAddress(ipAddress)
            .returnUrl(returnUrl)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    paymentAttempt = paymentAttemptRepository.save(paymentAttempt);

    String txnRef =
        "REMKEY_" + System.currentTimeMillis() + "_" + String.valueOf(paymentAttempt.getId());
    String amount =
        order.getTotalAmount().multiply(BigDecimal.valueOf(100)).toBigInteger().toString();

    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String createDate = formatter.format(calendar.getTime());
    calendar.add(Calendar.MINUTE, expireTimeConfig);
    String expireDate = formatter.format(calendar.getTime());

    //       add params
    Map<String, String> params = new TreeMap<>();
    params.put("vnp_Version", "2.1.0");
    params.put("vnp_Command", "pay");
    params.put("vnp_TmnCode", tmnCode);
    params.put("vnp_Amount", amount);
    params.put("vnp_CurrCode", "VND");
    params.put("vnp_IpAddr", ipAddress);
    params.put("vnp_TxnRef", txnRef);
    params.put("vnp_Locale", "vn");
    params.put("vnp_OrderInfo", "Thanh toan don hang: " + order.getId());
    params.put("vnp_OrderType", "other");
    params.put("vnp_ReturnUrl", returnUrl);
    params.put("vnp_CreateDate", createDate);
    params.put("vnp_ExpireDate", expireDate);

    //        Build query and secureHash
    String query = signatureService.buildQuery(params);
    String hashData = signatureService.buildHashData(params);
    String secureHash = signatureService.hmacSHA512(secretKey.trim(), hashData);

    String paymentUrl = payUrl + "?" + query + "&vnp_SecureHash=" + secureHash;

    paymentAttempt.setRedirectUrl(paymentUrl);
    paymentAttempt.setRequestPayload(objectMapper.writeValueAsString(params));
    paymentAttempt.setUpdatedAt(LocalDateTime.now());
    paymentAttemptRepository.save(paymentAttempt);

    return new PaymentResponse(paymentUrl, txnRef);
  }

  @Override
  public PaymentMethod getMethod() {
    return PaymentMethod.VNPAY;
  }

  @Override
  public PaymentResult ipnHandle(Map<String, String> params) throws JsonProcessingException {

    //        check data integration
    Map<String, String> data = new TreeMap<>(params);
    String vnpaySecureHash = data.get("vnp_SecureHash");
    data.remove("vnp_SecureHashType");
    data.remove("vnp_SecureHash");
    String secretKey = vnpayProperties.getSecretKey();
    String rawData = signatureService.buildHashData(data);
    String hashData = signatureService.hmacSHA512(secretKey.trim(), rawData);

    if (!hashData.equals(vnpaySecureHash)) {
      log.error("ERROR: SIGNATURE INVALID!");
      throw new AppException(ErrorCode.SIGNATURE_INVALID);
    }

    String txnRef = data.get("vnp_TxnRef");
    String[] txnRefSplit = txnRef.split("_");
    long paymentAttemptId = Long.parseLong(txnRefSplit[txnRefSplit.length - 1]);

    PaymentAttempt paymentAttempt =
        paymentAttemptRepository
            .findById(paymentAttemptId)
            .orElseThrow(
                () -> {
                  return new AppException(ErrorCode.PAYMENT_ATTEMPT_NOT_EXIST);
                });

    //        check amount
    BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(data.get("vnp_Amount")));
    amount = amount.divide(BigDecimal.valueOf(100)); // get raw value
    System.out.println("Amount was sent from vnPay: " + amount);
    if (!(amount.compareTo(paymentAttempt.getAmount()) == 0)) {
      log.error("ERROR: AMOUNT MISMATCH!");
      throw new AppException(ErrorCode.AMOUNT_MISMATCH);
    }

    //        build payment status
    String responseCode = data.get("vnp_ResponseCode");
    String transactionNo = data.get("vnp_TransactionNo");
    System.out.println("vnp_ResponseCode: " + responseCode);

    //        handle vnpay code
    VNPayResponseCode vnPayResponseCode = VNPayResponseCode.from(responseCode);
    String vnpayRspCode = vnPayResponseCode.getCode();
    String vnpayMessage = vnPayResponseCode.getMessage();
    PaymentStatus paymentStatus = vnPayResponseCode.getPaymentStatus();
    PaymentStatus orderPaymentStatus = vnPayResponseCode.getOrderPaymentStatus();
    System.out.println(
        "Message: " + vnpayMessage + "vnpayResponseCode: " + vnPayResponseCode.name());

    //        Build response payload
    String responsePayload = objectMapper.writeValueAsString(params);

    //        Build paidAt
    if (paymentStatus == PaymentStatus.SUCCESS) {
      String vnpPayDate = data.get("vnp_PayDate");
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
      LocalDateTime paidAt = LocalDateTime.parse(vnpPayDate, formatter);
      paymentAttempt.setPaidAt(paidAt);
    }

    paymentAttempt.setResponsePayload(responsePayload);
    paymentAttempt.setGateWayTxnId(transactionNo);
    paymentAttempt.setUpdatedAt(LocalDateTime.now());
    paymentAttempt.setPaymentStatus(paymentStatus);
    paymentAttempt.setResponseCode(vnpayRspCode);
    paymentAttempt.setResponseMessage(vnpayMessage);
    paymentAttemptRepository.save(paymentAttempt);

    return PaymentResult.builder()
        .txnRef(paymentAttempt.getId())
        .orderId(paymentAttempt.getOrderId())
        .code(vnpayRspCode)
        .message(vnpayMessage)
        .orderPaymentStatus(orderPaymentStatus)
        .paymentStatus(paymentStatus)
        .build();
  }

  @Transactional
  public Map<String, String> handlePaymentResult(PaymentResult paymentResult) {

    Map<String, String> map = new HashMap<>();
    map.put("RspCode", paymentResult.getCode());
    map.put("Message", paymentResult.getMessage());
    return map;
  }
}

package com.washinggod.remkey.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.washinggod.remkey.dto.response.PaymentResponse;
import com.washinggod.remkey.dto.response.PaymentResult;
import com.washinggod.remkey.entity.Order;
import com.washinggod.remkey.enums.PaymentMethod;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface PaymentService {

  public PaymentResponse pay(Order order, String ipAddress)
      throws JsonProcessingException, UnsupportedEncodingException;

  public PaymentResult ipnHandle(Map<String, String> params) throws JsonProcessingException;

  public Map<String, String> handlePaymentResult(PaymentResult paymentResult);

  public PaymentMethod getMethod();
}

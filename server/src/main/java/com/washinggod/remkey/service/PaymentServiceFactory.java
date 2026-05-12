package com.washinggod.remkey.service;

import com.washinggod.remkey.enums.PaymentMethod;
import com.washinggod.remkey.exception.AppException;
import com.washinggod.remkey.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceFactory {

  private final List<PaymentService> paymentServices;

  public PaymentService getPaymentService(PaymentMethod method) {
    return paymentServices.stream()
        .filter(
            service -> {
              return service.getMethod().equals(method);
            })
        .findFirst()
        .orElseThrow(
            () -> {
              return new AppException(ErrorCode.PAYMENT_METHOD_INVALID);
            });
  }
}

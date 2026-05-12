package com.washinggod.remkey.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum VNPayResponseCode {
  SUCCESS("00", PaymentStatus.SUCCESS, PaymentStatus.SUCCESS, "Confirm Success"),

  INVALID_ORDER("01", PaymentStatus.FAILED, PaymentStatus.FAILED, "Invalid order"),
  INVALID_AMOUNT("02", PaymentStatus.FAILED, PaymentStatus.FAILED, "Invalid amount"),
  INVALID_SIGNATURE("03", PaymentStatus.FAILED, PaymentStatus.FAILED, "Invalid signature"),
  PAYMENT_TIMEOUT("04", PaymentStatus.FAILED, PaymentStatus.FAILED, "Payment timeout"),
  PAYMENT_CANCELLED("05", PaymentStatus.CANCELED, PaymentStatus.CANCELED, "Payment cancelled"),

  UNKNOWN("99", PaymentStatus.FAILED, PaymentStatus.FAILED, "Unknown error");

  private final String code;
  private final PaymentStatus paymentStatus;
  private final PaymentStatus orderPaymentStatus;
  private final String message;

  VNPayResponseCode(
      String code, PaymentStatus paymentStatus, PaymentStatus orderPaymentStatus, String message) {
    this.code = code;
    this.paymentStatus = paymentStatus;
    this.orderPaymentStatus = orderPaymentStatus;
    this.message = message;
  }

  public static VNPayResponseCode from(String code) {
    return Arrays.stream(values()).filter(v -> v.code.equals(code)).findFirst().orElse(UNKNOWN);
  }
}

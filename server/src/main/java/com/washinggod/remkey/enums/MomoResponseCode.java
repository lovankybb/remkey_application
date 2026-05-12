package com.washinggod.remkey.enums;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum MomoResponseCode {
  SUCCESS("0", "Thành công", PaymentStatus.SUCCESS, PaymentStatus.SUCCESS),

  ACCOUNT_NOT_EXIST(
      "3", "Thông tin tài khoản không tồn tại", PaymentStatus.FAILED, PaymentStatus.FAILED),
  PERMISSION_NOT_ACCEPT("7", "Không có quyền truy cập", PaymentStatus.FAILED, PaymentStatus.FAILED),
  BALANCE_NOT_SATISFY(
      "1001", "Tài khoản không đủ tiền giao dịch", PaymentStatus.FAILED, PaymentStatus.FAILED),
  NOT_SUPPORT("68", "Không được hỗ trợ (refund)", PaymentStatus.FAILED, PaymentStatus.FAILED),
  DECRYPT_HASH_FAILED("153", "Giải mã hash thất bại", PaymentStatus.FAILED, PaymentStatus.FAILED),
  ACCOUNT_LOCKED("1012", "Tài khoản đã bị khóa", PaymentStatus.FAILED, PaymentStatus.FAILED),
  VERIFY_EXPIRED("1013", "Xác thực hết hạn", PaymentStatus.FAILED, PaymentStatus.FAILED),
  SIGNATURE_MISMATCH(
      "2129", "Chữ ký (signature) không khớp", PaymentStatus.FAILED, PaymentStatus.FAILED),

  UNKNOWN("9999", "Không xác định", PaymentStatus.FAILED, PaymentStatus.FAILED);

  private final String code;
  private final PaymentStatus paymentStatus;
  private final PaymentStatus orderPaymentStatus;
  private final String message;

  MomoResponseCode(
      String code, String message, PaymentStatus paymentStatus, PaymentStatus orderPaymentStatus) {
    this.code = code;
    this.message = message;
    this.paymentStatus = paymentStatus;
    this.orderPaymentStatus = orderPaymentStatus;
  }

  public static MomoResponseCode from(String code) {
    return Arrays.stream(values()).filter(v -> v.code.equals(code)).findFirst().orElse(UNKNOWN);
  }
}

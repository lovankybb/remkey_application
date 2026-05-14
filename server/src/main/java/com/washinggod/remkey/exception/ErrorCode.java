package com.washinggod.remkey.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  EMAIL_ALREADY_EXISTED(1001L, "Email has been registered with another user", HttpStatus.CONFLICT),
  USERNAME_ALREADY_EXISTED(1001L, "Username already existed", HttpStatus.CONFLICT),
  PERMISSION_NOT_EXIST(1002L, "Permission does not exist", HttpStatus.BAD_REQUEST),
  SAVE_USER_FAILED(1003L, "Save user failed", HttpStatus.CONFLICT),
  USER_NOT_EXIST(1004L, "User not exist", HttpStatus.BAD_REQUEST),
  ROLE_NOT_EXIST(1005L, "Role does not exist", HttpStatus.BAD_REQUEST),
  PASSWORD_NOT_MATCH(1006L, "Password is incorrect", HttpStatus.BAD_REQUEST),
  OTP_NOT_AVAILABLE(1007L, "Please wait for some seconds", HttpStatus.BAD_REQUEST),
  OTP_REQUEST_LIMIT(
      1008L, "Your email has been blocked, please try again tomorrow", HttpStatus.BAD_REQUEST),
  OTP_INVALID(1009L, "Your otp code is invalid", HttpStatus.BAD_REQUEST),
  GENERATE_TOKEN_FAILED(1010L, "Generate token failed", HttpStatus.CONFLICT),
  TOKEN_INVALID(1011L, "Token is invalid", HttpStatus.BAD_REQUEST),
  USERNAME_INVALID(
      1012L,
      "Please enter a username with at least 4 characters. Special characters are not allowed.",
      HttpStatus.BAD_REQUEST),
  PASSWORD_INVALID(
      1013L,
      "Password requires at least 8 characters, including uppercase, lowercase, numeric, and special characters.",
      HttpStatus.BAD_REQUEST),
  EMAIL_INVALID(1014L, "Incorrect email format ", HttpStatus.BAD_REQUEST),
  TOO_MANY_REQUEST(
      1015L, "Please calm down, you're requesting too  fast.", HttpStatus.TOO_MANY_REQUESTS),
  SIGNATURE_INVALID(1016L, "Signature is invalid", HttpStatus.BAD_REQUEST),
  PAYMENT_METHOD_INVALID(1017L, "Payment method is invalid", HttpStatus.BAD_REQUEST),
  PAYMENT_ATTEMPT_NOT_EXIST(1018L, "Payment attempt doesn't exist", HttpStatus.BAD_REQUEST),
  AMOUNT_MISMATCH(1019L, "Amount doesn't match", HttpStatus.BAD_REQUEST),
  SAVE_PACKAGE_FAILED(1020L, "Save package failed", HttpStatus.INTERNAL_SERVER_ERROR),
  PACKAGE_NOT_EXIST(1021L, "Package doesn't exits", HttpStatus.BAD_REQUEST),
  SAVE_ORDER_FAILED(1022L, "Save order failed", HttpStatus.INTERNAL_SERVER_ERROR),
  ORDER_NOT_EXIST(1023L, "Order not exist", HttpStatus.BAD_REQUEST),
  PAYMENT_REQUEST_FAILED(1024L, "Payment request failed", HttpStatus.BAD_REQUEST),
  ORDER_ALREADY_PAID(1025L, "Order already paid", HttpStatus.BAD_REQUEST),
  SAVE_SUBSCRIPTION_FAILED(1026L, "Save subscription failed", HttpStatus.BAD_REQUEST),
  SUBSCRIPTION_NOT_EXIST(1026L, "Subscription doesn't exist", HttpStatus.BAD_REQUEST),
  SAVE_LANGUAGE_FAILED(1027L, "Fail in saving language", HttpStatus.INTERNAL_SERVER_ERROR),
  LANGUAGE_NOT_EXIST(1028L, "Language doesn't exist", HttpStatus.BAD_REQUEST),
  SAVE_TOPIC_FAILED(1029L, "Fail in saving topic", HttpStatus.INTERNAL_SERVER_ERROR),
  TOPIC_NOT_EXIST(1030L, "Topic doesn't exist", HttpStatus.BAD_REQUEST),
  CARD_NOT_EXIST(1030L, "Card doesn't exist", HttpStatus.BAD_REQUEST),
  SAVE_CARD_FAILED(1030L, "Save card failed", HttpStatus.CONFLICT),
  SAVE_FILE_FAILED(1031L, "Fail in saving file", HttpStatus.BAD_REQUEST),
  SEND_EMAIL_FAILED(1032L, "Fail in sending email", HttpStatus.BAD_REQUEST),
  SAVE_NOTIFICATION_TOKEN_FAILED(
      1033L, "Fail in saving notification token", HttpStatus.INTERNAL_SERVER_ERROR),
  FILE_NOT_FOUND(1033L, "Cannot find file in storage", HttpStatus.INTERNAL_SERVER_ERROR),
  CARD_IMAGE_NOT_EXIST(1033L, "Card image does not exist", HttpStatus.BAD_REQUEST),
  REPORT_NOT_EXIST(1034L, "Report does not exist", HttpStatus.BAD_REQUEST),
  CLOUDINARY_ERROR(1035L, "There is some problem with cloudinary.", HttpStatus.INTERNAL_SERVER_ERROR),

  UNCATEGORIZED_EXCEPTION(9999L, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR);

  private final Long code;
  private final String message;
  private final HttpStatus httpStatus;

  ErrorCode(Long code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }
}

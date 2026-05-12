package com.washinggod.remkey.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
  PENDING,
  SUCCESS,
  FAILED,
  CANCELED;
}

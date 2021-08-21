package com.kaching.logging.util;

import lombok.Getter;

@Getter
public enum LoggingConstant {
  SUCCESS("SUCCESS"),
  FAILURE("FAILURE"),
  REQUEST_LOG("API_REQUEST_LOG: {}"),
  RESPONSE_LOG("API_RESPONSE_LOG: {}");

  private String message;

  LoggingConstant(String message) {
    this.message = message;
  }
}

package com.sum.exception;

public class WireMockFeignClientException extends RuntimeException {

  public WireMockFeignClientException(String message, Throwable cause) {
    super(message, cause);
  }
}

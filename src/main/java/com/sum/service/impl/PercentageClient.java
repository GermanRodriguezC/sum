package com.sum.service.impl;

import com.sum.exception.WireMockFeignClientException;
import com.sum.feign.WireMockFeignClient;
import com.sum.service.IPercentageClient;
import feign.RetryableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class PercentageClient implements IPercentageClient {

  private final WireMockFeignClient wireMockClient;

  @Retryable(
      maxAttemptsExpression = "#{3}",
      backoff = @Backoff(delay = 5000, multiplier = 2, maxDelay = 15000))
  @Override
  public Integer getPercentage() {
    try {
      log.info("get percentage from feign client");
      Integer percentageResponseDto =
          this.wireMockClient.getPercentageFromWireMock().getPercentage();
      log.info("get percentage successful");
      return percentageResponseDto;
    } catch (RetryableException ex) {
      log.error("could not get percentage");
      throw new WireMockFeignClientException("Error consuming wiremock feign client", ex);
    }
  }
}

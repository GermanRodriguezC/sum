package com.sum.feign;

import com.sum.dto.PercentageResponseDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "wiremock-client", url = "http://wiremock:8080")
public interface WireMockFeignClient {
  @Cacheable(cacheNames = "percentageCache", key = "#root.methodName")
  @GetMapping("/percentage")
  PercentageResponseDto getPercentageFromWireMock();
}

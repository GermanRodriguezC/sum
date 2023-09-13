package com.sum.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

  @Bean
  public Bucket createBucket() {
    return Bucket.builder().addLimit(Bandwidth.simple(3, Duration.ofMinutes(1))).build();
  }
}

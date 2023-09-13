package com.sum.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sum.dto.PercentageResponseDto;
import com.sum.exception.WireMockFeignClientException;
import com.sum.feign.WireMockFeignClient;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.retry.annotation.EnableRetry;

@ExtendWith(MockitoExtension.class)
@EnableRetry
class PercentageClientTest {

  @InjectMocks private PercentageClient percentageClient;

  @Mock private WireMockFeignClient wireMockFeignClient;

  @BeforeEach
  void setUp() {
    when(wireMockFeignClient.getPercentageFromWireMock())
        .thenAnswer(
            (Answer<PercentageResponseDto>)
                invocation -> {
                  PercentageResponseDto response = new PercentageResponseDto();
                  response.setPercentage(50);
                  return response;
                });
  }

  @Test
  void testGetPercentage_Success() {
    PercentageResponseDto responseDto = new PercentageResponseDto();
    responseDto.setPercentage(50);

    when(wireMockFeignClient.getPercentageFromWireMock()).thenReturn(responseDto);

    Integer percentage = percentageClient.getPercentage();

    assertEquals(50, percentage);

    verify(wireMockFeignClient).getPercentageFromWireMock();
  }

  @Test
  void testGetPercentageWithRetryableException() {
    when(wireMockFeignClient.getPercentageFromWireMock()).thenThrow(RetryableException.class);

    var exception =
        assertThrows(WireMockFeignClientException.class, () -> percentageClient.getPercentage());

    assertEquals("Error consuming wiremock feign client", exception.getMessage());
    verify(wireMockFeignClient, times(1)).getPercentageFromWireMock();
  }
}

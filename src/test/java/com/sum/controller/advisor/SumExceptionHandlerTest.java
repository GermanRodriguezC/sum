package com.sum.controller.advisor;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sum.controller.SumController;
import com.sum.feign.WireMockFeignClient;
import com.sum.filter.RateLimiterFilter;
import com.sum.service.impl.PercentageClient;
import com.sum.service.impl.SumService;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.RetryException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class SumExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;
  @InjectMocks private SumController sumController;
  @Mock private SumService sumService;
  @Mock private PercentageClient percentageClient;
  @Mock private WireMockFeignClient wireMockClient;
  @Mock private Bucket bucket;

  @Test
  @DisplayName("Test should get ConstraintViolationException when param is negative")
  void testNegativeParamBadRequest() throws Exception {
    mockMvc
        .perform(
            get("/sum")
                .param("addendOne", "-2")
                .param("addendTwo", "9")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
        .andExpect(
            jsonPath("$.message")
                .value("[getSumWithPercentage.addendOne: must be greater than or equal to 0]"));
  }

  @Test
  @DisplayName("Test should throw RetryException after 3 attempts")
  void testWiremockClientRetryAndExceptionHandling() throws Exception {
    // Given
    String addendOne = "2";
    String addendTwo = "9";
    when(sumService.getSumWithPercentage(Integer.valueOf(addendOne), Integer.valueOf(addendTwo)))
        .thenCallRealMethod();
    when(percentageClient.getPercentage()).thenCallRealMethod();
    when(wireMockClient.getPercentageFromWireMock())
        .thenThrow(new RetryException("Error consuming wiremock feign client"));

    // When
    mockMvc
        .perform(
            get("/sum")
                .param("addendOne", addendOne)
                .param("addendTwo", addendTwo)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value(HttpStatus.INTERNAL_SERVER_ERROR.name()))
        .andExpect(jsonPath("$.message").value("Error consuming wiremock feign client"));

    // Then
    verify(wireMockClient, never()).getPercentageFromWireMock();
  }

  @RepeatedTest(4)
  @DisplayName("Test should get too_many_requests when called multiple times")
  void testTooManyRequests(RepetitionInfo repetitionInfo) throws Exception {
    // Given
    mockMvc =
        MockMvcBuilders.standaloneSetup(sumController)
            .addFilters(new RateLimiterFilter(bucket))
            .build();
    when(bucket.tryConsume(1)).thenReturn(repetitionInfo.getCurrentRepetition() <= 3);

    // When
    var resultActions =
        mockMvc.perform(
            get("/sum")
                .param("addendOne", "5")
                .param("addendTwo", "6")
                .contentType(MediaType.APPLICATION_JSON));

    // Then
    if (repetitionInfo.getCurrentRepetition() == 4) {
      resultActions
          .andExpect(status().isTooManyRequests())
          .andExpect(jsonPath("$.error").value(HttpStatus.TOO_MANY_REQUESTS.name()))
          .andExpect(jsonPath("$.message").value("Exceeded the number of requests per minute"));
    }
  }
}

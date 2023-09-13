package com.sum.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.sum.service.ICallHistoryService;
import com.sum.util.CallHistoryMapperUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import util.CallHistoryResponseDTOUtils;

@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class CallHistoryEventListenerIntegrationTest {

  private static final String CALL_HISTORY = "call-history";

  @Autowired private KafkaTemplate<String, String> kafkaTemplate;

  @Mock private ICallHistoryService callHistoryService;

  @Test
  void testCallHistoryListener() throws Exception {
    // Given
    String message = CallHistoryMapperUtil.dtoToString(CallHistoryResponseDTOUtils.createDefault());
    doNothing().when(callHistoryService).saveCall(any());

    // When
    kafkaTemplate.send(CALL_HISTORY, message);

    // Then
    verify(callHistoryService, never()).saveCall(any());
  }
}

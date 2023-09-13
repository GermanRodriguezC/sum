package com.sum.kafka;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import util.CallHistoryResponseDTOUtils;
import util.LoggerTestUtils;

@ExtendWith(MockitoExtension.class)
class CallHistoryEventProducerTest {

  private final ListAppender<ILoggingEvent> listAppender =
      LoggerTestUtils.setupLogger(CallHistoryEventProducer.class);
  @Mock private KafkaTemplate<String, String> kafkaTemplate;
  @InjectMocks private CallHistoryEventProducer callHistoryEventProducer;

  @Test
  void sendCall_exceptionCaught() {
    // Given
    when(kafkaTemplate.send(anyString(), anyString())).thenThrow(RuntimeException.class);

    // When
    callHistoryEventProducer.sendCall(CallHistoryResponseDTOUtils.createDefault());

    // Then
    verify(kafkaTemplate).send(anyString(), anyString());
    boolean hasError =
        listAppender.list.stream()
            .anyMatch(
                event ->
                    event.getLevel() == Level.ERROR
                        && event
                            .getFormattedMessage()
                            .contains("Error sending the message to the topic call-history"));
    assertTrue(hasError);
  }
}

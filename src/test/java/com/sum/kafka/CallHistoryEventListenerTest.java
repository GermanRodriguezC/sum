package com.sum.kafka;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sum.dto.CallHistoryResponseDTO;
import com.sum.service.ICallHistoryService;
import com.sum.util.CallHistoryMapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.CallHistoryResponseDTOUtils;
import util.LoggerTestUtils;

@ExtendWith(MockitoExtension.class)
class CallHistoryEventListenerTest {

  private final ListAppender<ILoggingEvent> listAppender =
      LoggerTestUtils.setupLogger(CallHistoryEventListener.class);
  @Mock private ICallHistoryService callHistoryService;
  @InjectMocks private CallHistoryEventListener callHistoryEventListener;

  @Test
  void callHistoryListener_exceptionCaught() throws JsonProcessingException {
    // Given
    doThrow(new RuntimeException())
        .when(callHistoryService)
        .saveCall(any(CallHistoryResponseDTO.class));

    // When
    callHistoryEventListener.callHistoryListener(
        CallHistoryMapperUtil.dtoToString(CallHistoryResponseDTOUtils.createDefault()));

    // Then
    verify(callHistoryService).saveCall(any(CallHistoryResponseDTO.class));
    boolean hasError =
        listAppender.list.stream()
            .anyMatch(
                event ->
                    event.getLevel() == Level.ERROR
                        && event
                            .getFormattedMessage()
                            .contains("Error processing the message in the topic call-history"));
    assertTrue(hasError);
  }
}

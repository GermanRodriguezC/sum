package com.sum.kafka;

import com.sum.service.ICallHistoryService;
import com.sum.util.CallHistoryMapperUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CallHistoryEventListener {

  private static final String CALL_HISTORY = "call-history";

  private final ICallHistoryService callHistoryService;

  @KafkaListener(topics = CALL_HISTORY, groupId = "sum")
  public void callHistoryListener(@Payload String message) {
    try {
      log.info("The message was received: {} in the topic: {}", message, CALL_HISTORY);
      var callHistoryResponseDTO = CallHistoryMapperUtil.stringToDto(message);
      callHistoryService.saveCall(callHistoryResponseDTO);
    } catch (Exception e) {
      log.error("Error processing the message in the topic {}", CALL_HISTORY, e);
    }
  }
}

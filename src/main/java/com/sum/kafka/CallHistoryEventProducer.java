package com.sum.kafka;

import com.sum.dto.CallHistoryResponseDTO;
import com.sum.util.CallHistoryMapperUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CallHistoryEventProducer {

  private static final String CALL_HISTORY = "call-history";

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendCall(CallHistoryResponseDTO callHistoryResponseDTO) {
    try {
      var topicMessage = CallHistoryMapperUtil.dtoToString(callHistoryResponseDTO);
      log.info("The message {} will be sent to the topic {}", topicMessage, CALL_HISTORY);
      kafkaTemplate.send(CALL_HISTORY, topicMessage);
      log.info("Successful message sent");
    } catch (Exception e) {
      log.error("Error sending the message to the topic {}", CALL_HISTORY, e);
    }
  }
}

package com.sum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sum.kafka.CallHistoryEventProducer;
import com.sum.util.CallHistoryMapperUtil;
import java.time.Duration;
import java.util.Collections;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import util.CallHistoryResponseDTOUtils;

@SpringBootTest
@EmbeddedKafka(
    partitions = 1,
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class CallHistoryEventProducerIntegrationTest {

  public static final String CALL_HISTORY = "call-history";
  @Autowired private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Autowired private CallHistoryEventProducer callHistoryEventProducer;

  private KafkaConsumer<String, String> consumer;

  @BeforeEach
  public void setUp() {
    consumer =
        new KafkaConsumer<>(KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker));
    consumer.subscribe(Collections.singletonList(CALL_HISTORY));
  }

  @Test
  void testSendCall() throws JsonProcessingException {
    // Given
    var callHistoryResponseDTO = CallHistoryResponseDTOUtils.createDefault();

    // When
    callHistoryEventProducer.sendCall(callHistoryResponseDTO);
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
    ConsumerRecord<String, String> received = records.iterator().next();

    // Then
    var expectedMessage = CallHistoryMapperUtil.dtoToString(callHistoryResponseDTO);
    assertEquals(expectedMessage, received.value());
  }
}

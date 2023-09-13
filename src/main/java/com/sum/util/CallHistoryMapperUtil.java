package com.sum.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sum.domain.CallHistory;
import com.sum.dto.CallHistoryResponseDTO;

public class CallHistoryMapperUtil {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.registerModule(new JavaTimeModule());
  }

  private CallHistoryMapperUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static CallHistoryResponseDTO entityToResponseDTO(CallHistory entity) {
    return objectMapper.convertValue(entity, CallHistoryResponseDTO.class);
  }

  public static CallHistory responseDTOToEntity(CallHistoryResponseDTO callHistoryResponseDTO) {
    return objectMapper.convertValue(callHistoryResponseDTO, CallHistory.class);
  }

  public static String dtoToString(CallHistoryResponseDTO dto) throws JsonProcessingException {
    return objectMapper.writeValueAsString(dto);
  }

  public static CallHistoryResponseDTO stringToDto(String jsonString)
      throws JsonProcessingException {
    return objectMapper.readValue(jsonString, CallHistoryResponseDTO.class);
  }
}

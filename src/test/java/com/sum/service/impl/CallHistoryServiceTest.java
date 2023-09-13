package com.sum.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sum.domain.CallHistory;
import com.sum.dto.CallHistoryResponseDTO;
import com.sum.exception.CallHistoryException;
import com.sum.exception.RepositoryException;
import com.sum.repository.CallHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import util.CallHistoryResponseDTOUtils;

@ExtendWith(MockitoExtension.class)
class CallHistoryServiceTest {

  @Mock private CallHistoryRepository callHistoryRepository;

  @InjectMocks private CallHistoryServiceImpl callHistoryService;

  @Test
  @DisplayName("Test Get Call History")
  void testGetCallHistory() {
    // Given
    when(callHistoryRepository.findAll(Pageable.unpaged())).thenReturn(Page.empty());

    // When
    Page<CallHistoryResponseDTO> result = callHistoryService.getCallHistory(Pageable.unpaged());

    // Then
    assertNotNull(result);
    verify(callHistoryRepository).findAll(Pageable.unpaged());
  }

  @Test
  @DisplayName("Test Get Call History With Exception")
  void testGetCallHistoryWithThrow() {
    // Given
    var anyPageable = any(Pageable.class);
    when(callHistoryRepository.findAll(anyPageable))
        .thenThrow(new RuntimeException("Simulated exception"));

    // When
    assertThatThrownBy(() -> callHistoryService.getCallHistory(anyPageable))
        .isInstanceOf(CallHistoryException.class)
        .hasMessageContaining("Error find all call history");

    // Then
    verify(callHistoryRepository).findAll(anyPageable);
  }

  @Test
  @DisplayName("Save Call Successful")
  void testSaveCallSuccessful() {
    // Given
    when(callHistoryRepository.save(any(CallHistory.class))).thenReturn(new CallHistory());

    // When
    callHistoryService.saveCall(CallHistoryResponseDTOUtils.createDefault());

    // Then
    verify(callHistoryRepository).save(any(CallHistory.class));
  }

  @Test
  @DisplayName("Test saveCall method with exception")
  void testSaveCallWithException() {
    // Given
    when(callHistoryRepository.save(any())).thenThrow(new RuntimeException("Simulated exception"));

    // When
    assertThatThrownBy(
            () -> callHistoryService.saveCall(CallHistoryResponseDTOUtils.createDefault()))
        .isInstanceOf(RepositoryException.class)
        .hasMessageContaining("Error saving call history");

    // Then
    verify(callHistoryRepository).save(any());
  }
}

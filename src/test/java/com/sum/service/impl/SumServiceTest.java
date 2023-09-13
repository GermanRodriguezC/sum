package com.sum.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.sum.dto.SumResponseDto;
import com.sum.service.IPercentageClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class SumServiceTest {

  @Mock private IPercentageClient percentageClient;
  @InjectMocks private SumService sumService;

  @Test
  void testGetSumWithPercentage() {
    // Given
    int addendOne = 5;
    int addendTwo = 10;
    int percentage = 20;
    when(percentageClient.getPercentage()).thenReturn(percentage);

    // When
    SumResponseDto result = sumService.getSumWithPercentage(addendOne, addendTwo);

    // Then
    verify(percentageClient).getPercentage();
    assertEquals(
        (float) ((addendOne + addendTwo) * (1 + percentage / 100.0)), result.getResult(), 0.01);
  }
}

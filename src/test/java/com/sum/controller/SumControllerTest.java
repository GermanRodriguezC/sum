package com.sum.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sum.dto.SumResponseDto;
import com.sum.service.ISumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class SumControllerTest {

  private static final int ADDEND_ONE = 5;
  private static final int ADDEND_TWO = 6;
  private static final float EXPECTED_SUM = 11.3f;

  @Autowired private MockMvc mockMvc;

  @InjectMocks private SumController sumController;

  @Mock private ISumService sumService;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(sumController).build();
  }

  @Test
  @DisplayName("Test should get the sum with percentage and return OK")
  void getSumWithPercentage() throws Exception {
    // Given
    when(sumService.getSumWithPercentage(ADDEND_ONE, ADDEND_TWO))
        .thenReturn(SumResponseDto.builder().result(EXPECTED_SUM).build());

    // When
    mockMvc
        .perform(
            get("/sum")
                .param("addendOne", String.valueOf(ADDEND_ONE))
                .param("addendTwo", String.valueOf(ADDEND_TWO))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value(EXPECTED_SUM));

    // Then
    verify(sumService).getSumWithPercentage(ADDEND_ONE, ADDEND_TWO);
  }
}

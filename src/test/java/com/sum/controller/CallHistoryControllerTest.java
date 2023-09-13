package com.sum.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sum.service.ICallHistoryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import util.CallHistoryResponseDTOUtils;

@ExtendWith(MockitoExtension.class)
class CallHistoryControllerTest {

  @Autowired private MockMvc mockMvc;

  @InjectMocks private CallHistoryController callHistoryController;

  @Mock private ICallHistoryService callHistoryService;

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(callHistoryController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
  }

  @Test
  @DisplayName("Test should get the call history and return OK")
  void getCallHistory() throws Exception {
    // Given
    var pageable = PageRequest.of(0, 10);
    var mockPage =
        new PageImpl<>(List.of(CallHistoryResponseDTOUtils.createDefault()), pageable, 1);
    when(callHistoryService.getCallHistory(pageable)).thenReturn(mockPage);

    // When
    mockMvc
        .perform(
            get("/call-history")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(11)))
        .andExpect(
            jsonPath("$.content[0].methodName").value(mockPage.getContent().get(0).getMethodName()))
        .andExpect(
            jsonPath("$.content[0].requestMethod")
                .value(mockPage.getContent().get(0).getRequestMethod()))
        .andExpect(
            jsonPath("$.content[0].requestPath")
                .value(mockPage.getContent().get(0).getRequestPath()))
        .andExpect(
            jsonPath("$.content[0].responseStatus")
                .value(mockPage.getContent().get(0).getResponseStatus()))
        .andExpect(
            jsonPath("$.content[0].responseBody")
                .value(mockPage.getContent().get(0).getResponseBody()))
        .andExpect(jsonPath("$.pageable.offset").value((int) pageable.getOffset()))
        .andExpect(jsonPath("$.pageable.pageSize").value(pageable.getPageSize()))
        .andExpect(jsonPath("$.totalElements").value(mockPage.getTotalElements()))
        .andExpect(jsonPath("$.size").value(pageable.getPageSize()));
    // Then
    verify(callHistoryService).getCallHistory(pageable);
  }
}

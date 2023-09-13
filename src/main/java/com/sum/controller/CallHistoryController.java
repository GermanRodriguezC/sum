package com.sum.controller;

import com.sum.dto.CallHistoryResponseDTO;
import com.sum.service.ICallHistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/call-history")
@AllArgsConstructor
@Slf4j
public class CallHistoryController {

  private final ICallHistoryService callHistoryService;

  @GetMapping
  public ResponseEntity<Page<CallHistoryResponseDTO>> getCallHistory(Pageable pageable) {
    log.info("retrieving queries from endpoint calls");
    return ResponseEntity.ok(this.callHistoryService.getCallHistory(pageable));
  }
}

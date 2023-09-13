package com.sum.controller;

import com.sum.dto.SumResponseDto;
import com.sum.service.ISumService;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sum")
@Validated
@Slf4j
@AllArgsConstructor
public class SumController {

  private final ISumService sumService;

  @GetMapping
  public ResponseEntity<SumResponseDto> getSumWithPercentage(
      @PositiveOrZero @RequestParam Integer addendOne,
      @PositiveOrZero @RequestParam Integer addendTwo) {

    log.info("Retrieving addends {} y {}", addendOne, addendTwo);

    return ResponseEntity.ok(this.sumService.getSumWithPercentage(addendOne, addendTwo));
  }
}

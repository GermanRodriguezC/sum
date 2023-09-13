package com.sum.service.impl;

import com.sum.dto.SumResponseDto;
import com.sum.service.IPercentageClient;
import com.sum.service.ISumService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SumService implements ISumService {

  private final IPercentageClient percentageClient;

  @Override
  public SumResponseDto getSumWithPercentage(Integer addendOne, Integer addendTwo) {
    Integer percentage = percentageClient.getPercentage();
    log.info(
        "performing addition with a percentage: ({} + {})*{} ", addendOne, addendTwo, percentage);
    return SumResponseDto.builder()
        .result((float) ((addendOne + addendTwo) * (1 + percentage / 100.0)))
        .build();
  }
}

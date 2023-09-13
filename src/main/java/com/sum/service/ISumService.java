package com.sum.service;

import com.sum.dto.SumResponseDto;

public interface ISumService {

  SumResponseDto getSumWithPercentage(Integer addendOne, Integer addendTwo);
}

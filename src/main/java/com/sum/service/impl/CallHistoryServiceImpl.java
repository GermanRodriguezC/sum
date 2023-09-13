package com.sum.service.impl;

import com.sum.dto.CallHistoryResponseDTO;
import com.sum.exception.CallHistoryException;
import com.sum.exception.RepositoryException;
import com.sum.repository.CallHistoryRepository;
import com.sum.service.ICallHistoryService;
import com.sum.util.CallHistoryMapperUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CallHistoryServiceImpl implements ICallHistoryService {

  private final CallHistoryRepository callHistoryRepository;

  @Override
  public Page<CallHistoryResponseDTO> getCallHistory(Pageable pageable) {
    log.info("Request received to get call history.");
    try {
      Page<CallHistoryResponseDTO> callHistory =
          this.callHistoryRepository
              .findAll(pageable)
              .map(CallHistoryMapperUtil::entityToResponseDTO);
      log.info("Call history retrieval successful.");
      return callHistory;
    } catch (Exception e) {
      log.error("Error occurred while fetching call history.", e);
      throw new CallHistoryException("Error find all call history", e);
    }
  }

  @Override
  public void saveCall(CallHistoryResponseDTO callHistoryResponseDTO) {
    log.info("Saving call history entry.");
    try {
      callHistoryRepository.save(CallHistoryMapperUtil.responseDTOToEntity(callHistoryResponseDTO));
      log.info("Call history entry saved successfully.");
    } catch (Exception e) {
      log.error("Error occurred while saving call history entry.", e);
      throw new RepositoryException("Error saving call history", e);
    }
  }
}

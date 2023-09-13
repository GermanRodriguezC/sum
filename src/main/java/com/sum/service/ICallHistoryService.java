package com.sum.service;

import com.sum.dto.CallHistoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICallHistoryService {

  Page<CallHistoryResponseDTO> getCallHistory(Pageable pageable);

  void saveCall(CallHistoryResponseDTO callHistoryResponseDTO);
}

package com.sum.repository;

import com.sum.domain.CallHistory;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {

  @NotNull
  Page<CallHistory> findAll(@NotNull Pageable pageable);
}

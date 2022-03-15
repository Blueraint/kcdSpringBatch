package com.kcd.KCDSpringBatch.service;

import com.kcd.KCDSpringBatch.creditinfo.domain.Firm;
import com.kcd.KCDSpringBatch.dto.BatchJobExecutionDto;
import com.kcd.KCDSpringBatch.dto.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BatchJobService {
    List<BatchJobExecutionDto> findBatchStatus(SearchParam searchParam);
    Page<BatchJobExecutionDto> findAllStatus(Pageable pageable);
    List<String> findBatchList();
    List<BatchJobExecutionDto> findBatchStatusSearchByStatusAndExitCode(SearchParam searchParam);
}

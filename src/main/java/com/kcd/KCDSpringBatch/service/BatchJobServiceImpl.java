package com.kcd.KCDSpringBatch.service;

import com.kcd.KCDSpringBatch.dto.BatchJobExecutionDto;
import com.kcd.KCDSpringBatch.dto.SearchParam;
import com.kcd.KCDSpringBatch.mapper.BatchJobExecutionMapper;
import com.kcd.KCDSpringBatch.springbatch.domain.BatchJobExecution;
import com.kcd.KCDSpringBatch.springbatch.repository.BatchJobExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchJobServiceImpl implements BatchJobService {
    private final BatchJobExecutionRepository batchJobExecutionRepository;
    private final JobExplorer jobExplorer;

    @Override
    public List<BatchJobExecutionDto> findBatchStatus(SearchParam searchParam) {
        List<BatchJobExecution> batchJobExecutionList = new ArrayList<>();
        List<BatchJobExecutionDto> batchJobExecutionDtoList = new ArrayList<>();

        if(searchParam.getStartDate() != null) {
            if(searchParam.getEndDate() != null) batchJobExecutionList =
                    batchJobExecutionRepository.findByCreateTimeBetweenAndBatchJobExecutionParamsListKeyNameAndBatchJobExecutionParamsListStringVal(searchParam.getStartDate(), searchParam.getEndDate(), "firmCode", searchParam.getFirmCode());
            else batchJobExecutionList = batchJobExecutionRepository.findByCreateTimeGreaterThanEqualAndBatchJobExecutionParamsListKeyNameAndBatchJobExecutionParamsListStringVal(searchParam.getStartDate(), "firmCode", searchParam.getFirmCode());
        }
        else {
            batchJobExecutionList = batchJobExecutionRepository.findByBatchJobExecutionParamsListKeyNameAndBatchJobExecutionParamsListStringVal("firmCode",searchParam.getFirmCode());
        }

        batchJobExecutionList.forEach(i ->
                batchJobExecutionDtoList.add(BatchJobExecutionMapper.INSTANCE.entityToDto(i))
        );

        return batchJobExecutionDtoList;
    }

    @Override
    public Page<BatchJobExecutionDto> findAllStatus(Pageable pageable) {
        Page<BatchJobExecution> page = batchJobExecutionRepository.findAll(pageable);
        Page<BatchJobExecutionDto> dtoPage = page.map(batchJobExecution -> BatchJobExecutionMapper.INSTANCE.entityToDto(batchJobExecution));

        return dtoPage;
    }

    // jobExplorer : Job 의 여러 상태들을 조회
    @Override
    public List<String> findBatchList() {
        return jobExplorer.getJobNames();
    }

    @Override
    public List<BatchJobExecutionDto> findBatchStatusSearchByStatusAndExitCode(SearchParam searchParam) {
        List<BatchJobExecutionDto> batchJobExecutionDtoList = new ArrayList<>();
        List<BatchJobExecution> batchJobExecutionList =
                batchJobExecutionRepository.findByStatusAndExitCodeAndBatchJobExecutionParamsListStringVal(searchParam.getStatus(), searchParam.getExitCode(), "firmCode", searchParam.getFirmCode());

        batchJobExecutionList.forEach(i -> batchJobExecutionDtoList.add(BatchJobExecutionMapper.INSTANCE.entityToDto(i)));

        return batchJobExecutionDtoList;
    }
}

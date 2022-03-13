package com.kcd.KCDSpringBatch.springbatch.repository;

import com.kcd.KCDSpringBatch.springbatch.domain.BatchJobExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Batch Metadata Repository(Database)
 * Springbatch JobRepository use JdbcJobExecutionDao(Old JDBC Connection)
 * We will use Spring Data JPA
 */

@Transactional(readOnly = true)
public interface BatchJobExecutionRepository extends ReadOnlyRepository<BatchJobExecution, Long>
{
    @EntityGraph(attributePaths = {"batchJobInstance"})
    List<BatchJobExecution> findAll();

    @EntityGraph(attributePaths = {"batchJobInstance"})
    Page<BatchJobExecution> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"batchJobInstance", "batchJobExecutionParamsList"})
    List<BatchJobExecution> findByBatchJobExecutionParamsListKeyNameAndBatchJobExecutionParamsListStringVal(String keyName, String stringVal);

    @EntityGraph(attributePaths = {"batchJobInstance", "batchJobExecutionParamsList"})
    List<BatchJobExecution> findByCreateTimeGreaterThanEqualAndBatchJobExecutionParamsListKeyNameAndBatchJobExecutionParamsListStringVal(LocalDateTime createTime, String keyName, String stringVal);

    @EntityGraph(attributePaths = {"batchJobInstance", "batchJobExecutionParamsList"})
    List<BatchJobExecution> findByCreateTimeBetweenAndBatchJobExecutionParamsListKeyNameAndBatchJobExecutionParamsListStringVal(LocalDateTime start, LocalDateTime end, String keyName, String stringVal);

}

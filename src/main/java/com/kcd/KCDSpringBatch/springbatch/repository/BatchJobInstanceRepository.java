package com.kcd.KCDSpringBatch.springbatch.repository;

import com.kcd.KCDSpringBatch.springbatch.domain.BatchJobExecution;
import org.springframework.transaction.annotation.Transactional;

/**
 * Batch Metadata Repository(Database)
 * Springbatch JobRepository use JdbcJobExecutionDao(Old JDBC Connection)
 * We will use Spring Data JPA
 */

@Transactional(readOnly = true)
public interface BatchJobInstanceRepository extends ReadOnlyRepository<BatchJobExecution, Long> {

}

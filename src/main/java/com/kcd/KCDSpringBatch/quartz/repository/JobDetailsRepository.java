package com.kcd.KCDSpringBatch.quartz.repository;

import com.kcd.KCDSpringBatch.quartz.domain.JobDetails;
import com.kcd.KCDSpringBatch.quartz.domain.JobKeys;
import com.kcd.KCDSpringBatch.springbatch.repository.ReadOnlyRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface JobDetailsRepository extends ReadOnlyRepository<JobDetails, Long> {
    @EntityGraph(attributePaths = {"triggers", "triggers.cronTriggers"})
    JobDetails findByJobKeysJobNameAndJobKeysJobGroup(String jobName, String jobGroup);

    @EntityGraph(attributePaths = {"triggers", "triggers.cronTriggers"})
    List<JobDetails> findByJobKeysJobNameStartsWith(String jobName);
}

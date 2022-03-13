package com.kcd.KCDSpringBatch.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.kcd.KCDSpringBatch.dto.BatchParam;
import com.kcd.KCDSpringBatch.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * General Batch Service
 * Call Batch Job for JobName(@Bean)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralBatchLaunchService {
    private final JobLocator jobLocator;
    private final BasicBatchConfigurer basicBatchConfigurer;
    private final ApplicationContext ctx;
    private final ObjectMapper objectMapper;


    public void runJob(String jobName, BatchParam batchParam) {
        /* Get batchJob Configuration(Bean) */
        Job currentJob = ctx.getBean(jobName, Job.class);

        /* Get batchJob Configuration(JobLocator) */
        /*
        Job currentJob = null;
        try {
            currentJob = jobLocator.getJob(jobName);
        } catch (NoSuchJobException e) {
            e.printStackTrace();
        }
        */

        SimpleJobLauncher jobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());

        try {
            jobLauncher.run(currentJob, getJobParameters(batchParam));
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }

    }

    /*
     * create JobParam
     */
    public JobParameters getJobParameters(BatchParam batchParam) {
        JobParametersBuilder jobParametersBuilder
                = new JobParametersBuilder()
                .addString("firmCode",batchParam.getFirmCode())
                .addString("fileType", batchParam.getFileType())
                .addDate("executeDate", DateUtil.getCurrentDate())
//                .addDate("executeDate", Timestamp.valueOf(LocalDateTime.now()))
                ;

        if(batchParam.getStartDate() != null) jobParametersBuilder.addDate("startDate", batchParam.getStartDate());
        if(batchParam.getEndDate() != null) jobParametersBuilder.addDate("endDate", batchParam.getEndDate());

        return jobParametersBuilder.toJobParameters();
    }
}

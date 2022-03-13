package com.kcd.KCDSpringBatch.quartz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcd.KCDSpringBatch.dto.QuartzParam;
import com.kcd.KCDSpringBatch.service.GeneralBatchLaunchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleBatchJob implements Job {
    private final GeneralBatchLaunchService generalBatchLaunchService;
    private final ObjectMapper objectMapper;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("Scheduled job execute");

        JobDataMap jobDataMap = context.getMergedJobDataMap();

        QuartzParam scheduleQuartzParam = objectMapper.convertValue(jobDataMap, QuartzParam.class);

        generalBatchLaunchService.runJob(jobDataMap.getString("jobName"), scheduleQuartzParam);
    }

    public JobDetail buildJobDetail(String quartzType, String jobName, QuartzParam quartzParam) {
        JobDataMap jobDataMap = new JobDataMap();
        // will be change... Map stream <-> serialized instance
        HashMap<String, Object> quartzParamMap =  objectMapper.convertValue(quartzParam, HashMap.class);
        quartzParamMap.forEach((k,v) -> jobDataMap.put((String)k,v));

        //input jobName param
        jobDataMap.put("jobName", jobName);
        jobDataMap.remove("holidayExclude");


        /*
        jobKey : withIdentity(keyname, keygroup)
        Strategy : trigger use jobkey... then group is defined by batchjob
         */
        return JobBuilder.newJob(ScheduleBatchJob.class)
                .withIdentity(getJobName(quartzType,jobName,quartzParam), getGroupName(quartzType, "Job"))
                .withDescription("Data Pull Quartz Batch Job")
                .usingJobData(jobDataMap)
                .storeDurably() // store job and persist(save) into db
                .build();
    }

    // in the global service, use TimeZone
    // Fire batch trigger
    public Trigger buildTrigger(String quartzType, JobDetail jobDetail, LocalDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "ScheduleBatchTrigger")
                .withDescription(quartzType + " Batch Trigger")
                .startAt(Timestamp.valueOf(startAt)) // trigger will be start
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()) // execute scheduled job once.(fire!)
                .build();
    }

    // in the global service, use TimeZone
    // Cron(Daily, Eronexpression) batch trigger
    public Trigger buildTrigger(String quartzType, JobDetail jobDetail, ScheduleBuilder<CronTrigger> scheduleBuilder, boolean holidayExclude) {
        if(holidayExclude)
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName(), getGroupName(quartzType, "Trigger"))
                    .withDescription(quartzType + " Batch Trigger(holidayExclude)")
                    .withSchedule(scheduleBuilder) // execute scheduled with cron expression
                    .modifiedByCalendar("holidayCalendar")
                    .build();

        else
            return TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName(), getGroupName(quartzType, "Trigger"))
                    .withDescription(quartzType + " Batch Trigger(holidayInclude)")
                    .withSchedule(scheduleBuilder) // execute scheduled with cron expression
                    .build();
    }

    @NotNull
    private String getJobName(String quartzType, String jobName, QuartzParam quartzParam) {
        /*
        Create Job Name
        JobName Rule : jobName_firmCode
         */
        StringBuilder jobNameBuilder = new StringBuilder().append(quartzParam.getFirmCode()).append("_")
                .append(jobName);

        if("fire".equals(quartzType)) jobNameBuilder.append("_").append(quartzParam.getFireTime());
//        else if("daily".equals(quartzType)) jobNameBuilder.append("_").append(quartzParam.getHour()).append(":").append(quartzParam.getMinute());
//        else if("cron".equals(quartzType)) jobNameBuilder.append("_").append(quartzParam.getCronExpression());

        return jobNameBuilder.toString();
    }

    @NotNull
    private String getGroupName(String quartzType, String type) {
        return quartzType + "_" + "Batch" + type;
    }
}

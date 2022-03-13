package com.kcd.KCDSpringBatch.service;

import com.kcd.KCDSpringBatch.dto.JobDetailsDto;
import com.kcd.KCDSpringBatch.dto.QuartzParam;
import com.kcd.KCDSpringBatch.mapper.JobDetailsMapper;
import com.kcd.KCDSpringBatch.quartz.ScheduleBatchJob;
import com.kcd.KCDSpringBatch.quartz.domain.JobDetails;
import com.kcd.KCDSpringBatch.quartz.repository.JobDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzService {
    private final Scheduler scheduler;
    private final ScheduleBatchJob scheduleBatchJob;
    private final HolidayCalendar holidayCalendar;
    private final JobDetailsRepository jobDetailsRepository;

    public HashMap<String, Object> scheduleFireJob(String jobName, QuartzParam quartzParam) throws SchedulerException {
        /* Scheduling Job */
        JobDetail jobDetail = scheduleBatchJob.buildJobDetail("fire",jobName, quartzParam);
        Trigger trigger = scheduleBatchJob.buildTrigger("fire", jobDetail, quartzParam.getFireTime());

        scheduler.scheduleJob(jobDetail, trigger);

        HashMap<String, Object> keyMap = new HashMap<>();
        keyMap.put("keyName", jobDetail.getKey().getName());
        keyMap.put("keyGroup", jobDetail.getKey().getGroup());

        log.info("## Keyname : {}, KeyGroup : {}", jobDetail.getKey().getName(), jobDetail.getKey().getGroup());

        return keyMap;
    }

    public HashMap<String, Object> scheduleDailyJob(String jobName, QuartzParam quartzParam) throws SchedulerException {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.
                    dailyAtHourAndMinute(Integer.valueOf(quartzParam.getHour()), Integer.valueOf(quartzParam.getMinute()));

        /* Scheduling Job */
        scheduler.addCalendar("holidayCalendar",holidayCalendar, true, true);

        JobDetail jobDetail = scheduleBatchJob.buildJobDetail("daily",jobName, quartzParam);
        Trigger trigger = scheduleBatchJob.buildTrigger("daily", jobDetail, scheduleBuilder,quartzParam.isHolidayExclude());

        scheduler.scheduleJob(jobDetail, trigger);

        HashMap<String, Object> keyMap = new HashMap<>();
        keyMap.put("keyName", jobDetail.getKey().getName());
        keyMap.put("keyGroup", jobDetail.getKey().getGroup());

        log.info("## Keyname : {}, KeyGroup : {}", jobDetail.getKey().getName(), jobDetail.getKey().getGroup());

        return keyMap;
    }

    public HashMap<String, Object> scheduleCronJob(String jobName, QuartzParam quartzParam) throws SchedulerException {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzParam.getCronExpression());

        /* Scheduling Job */
        scheduler.addCalendar("holidayCalendar",holidayCalendar, true, true);

        JobDetail jobDetail = scheduleBatchJob.buildJobDetail("cron",jobName, quartzParam);
        Trigger trigger = scheduleBatchJob.buildTrigger("cron", jobDetail, scheduleBuilder,quartzParam.isHolidayExclude());

        scheduler.scheduleJob(jobDetail, trigger);

        HashMap<String, Object> keyMap = new HashMap<>();
        keyMap.put("keyName", jobDetail.getKey().getName());
        keyMap.put("keyGroup", jobDetail.getKey().getGroup());

        log.info("## Keyname : {}, KeyGroup : {}", jobDetail.getKey().getName(), jobDetail.getKey().getGroup());

        return keyMap;
    }

    /* Delete Scheduled Job and Trigger */
    public void deleteJob(String jobName, String groupName) throws SchedulerException {
        scheduler.deleteJob(new JobKey(jobName, groupName));
    }


    //If you want to step each instance of Job(executing...), use scheduler.interrupt
    public void interruptJob(String jobName, String groupName) throws UnableToInterruptJobException {
        scheduler.interrupt(new JobKey(jobName, groupName));
    }

    public JobDetailsDto findJobDetailByJobKeys(String jobName, String jobGroup) {
        JobDetails jobDetails = jobDetailsRepository.findByJobKeysJobNameAndJobKeysJobGroup(jobName, jobGroup);

        return JobDetailsMapper.INSTANCE.entityToDto(jobDetails);
    }

    public List<JobDetailsDto> findJobDetailByFirmCode(String firmCode) {
        List<JobDetails> jobDetailsList = jobDetailsRepository.findByJobKeysJobNameStartsWith(firmCode);
        List<JobDetailsDto> jobDetailsDtoList = new ArrayList<>();

        jobDetailsList.forEach(jobDetails -> jobDetailsDtoList.add(JobDetailsMapper.INSTANCE.entityToDto(jobDetails)));


        return jobDetailsDtoList;

    }
}

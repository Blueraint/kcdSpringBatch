package com.kcd.KCDSpringBatch;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.expression.Weekdays;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcd.KCDSpringBatch.dto.QuartzParam;
import com.kcd.KCDSpringBatch.quartz.domain.JobKeys;
import com.kcd.KCDSpringBatch.quartz.repository.JobDetailsRepository;
import com.kcd.KCDSpringBatch.service.GeneralBatchLaunchService;
import com.kcd.KCDSpringBatch.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.TimeZone;

import static com.cronutils.model.field.expression.FieldExpressionFactory.*;

@SpringBootTest
public class quartzTest {
    @Autowired
    private GeneralBatchLaunchService generalBatchLaunchService;
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    JobDetailsRepository jobDetailsRepository;

    @Test
    public void mapperTest() {
        LocalDateTime dateTime = LocalDateTime.now();

        QuartzParam quartzParam = new QuartzParam();
        quartzParam.setFileType("csv");
        quartzParam.setFirmCode("0001");
        quartzParam.setFireTime(dateTime);

        HashMap<String, Object> batchParamMap =  mapper.convertValue(quartzParam, HashMap.class);

        batchParamMap.forEach((s, o) -> System.out.println("##### Key : "+ s + ", value : " + o));
        System.out.println(batchParamMap.get("fireTime").getClass().getTypeName());

//        Assert.assertEquals(dateTime, batchParamMap.get("startAt"));

        Cron cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ))
                .withYear(always())
                .withDoM(questionMark())
                .withMonth(always())
                .withDoW(between(Weekdays.FRIDAY.getWeekday(), Weekdays.MONDAY.getWeekday()))
                .withHour(always())
                .withMinute(always())
                .withSecond(on(0))
                .instance();


        System.out.println("Cron :  "+ cron.asString());
    }

    @Test
    public void findTest() {
        JobKeys jobKeys = new JobKeys("0001_cardDetailPullBatchJob","daily_BatchJob");

        System.out.println("###Output : " + jobDetailsRepository.findByJobKeysJobNameAndJobKeysJobGroup(jobKeys.getJobName(), jobKeys.getJobGroup()).toString());
    }

    @Test
    public void dateTest() {
        String str = "0";
        Long time = Long.parseLong(str);
        System.out.println("###Date : " + LocalDateTime.ofInstant(Instant.ofEpochMilli(time), TimeZone.getDefault().toZoneId()));
    }
}

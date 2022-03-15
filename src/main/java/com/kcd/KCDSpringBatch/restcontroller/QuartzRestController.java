package com.kcd.KCDSpringBatch.restcontroller;

import com.kcd.KCDSpringBatch.dto.*;
import com.kcd.KCDSpringBatch.mapper.QuartzParamMapper;
import com.kcd.KCDSpringBatch.service.QuartzService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class QuartzRestController {
    private final QuartzService quartzService;


    @ApiOperation(value = "단건 배치 스케줄링 처리 요청 API", notes = "단건실행예정시간(fireTime) 설정을 통한 지정된 시간의 단건 처리를 위한 스케줄링 처리")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API가 정상적으로 Batch Scheduling을 처리함."),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @PostMapping("/fire/{jobName}")
    public ResponseEntity<GlobalMessageDto> scheduleFireBatch(@PathVariable("jobName") String jobName, @Valid @RequestBody QuartzFireParam quartzFireParam) {
        try {
            if(quartzFireParam.getFireTime() == null) {
                log.debug("schedule job must set fireTime param");
                GlobalMessageDto messageDto = new GlobalMessageDto(false, "fireTime must set.");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
            }

            // scheduling time is before now!!(Gone..)
            if(quartzFireParam.getFireTime().isBefore(LocalDateTime.now())) {
                log.debug("Scheduling time is before now.. ");
                GlobalMessageDto messageDto = new GlobalMessageDto(false, "fireTime must be after current time.");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
            }

            HashMap<String, Object> keyMap = quartzService.scheduleFireJob(jobName, QuartzParamMapper.INSTANCE.fireParamToCommon(quartzFireParam));

            return ResponseEntity.ok(new GlobalMessageDto(true, keyMap));
        } catch (SchedulerException e) {
            log.error("Error while Scheduling batch : ", e);
            GlobalMessageDto messageDto = new GlobalMessageDto(false, "Error while scheduling batch. try again/later.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageDto);
        }
    }


    @ApiOperation(value = "간편 일별 배치 스케줄링 처리 요청 API", notes = "일별로 정해진 시간(시(hour)/분(minute))을 지정하여 주기적으로 파일을 가져갈 수 있게 설정. 설정을 통한 지정된 시간의 단건 처리를 위한 스케줄링 처리. Cron Scheduling 의 일종")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API가 정상적으로 Batch Scheduling을 처리함."),
            @ApiResponse(code = 400, message = "Parameter 오류"),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @PostMapping("/daily/{jobName}")
    public ResponseEntity<GlobalMessageDto> scheduleDailyBatch(@PathVariable("jobName") String jobName, @Valid @RequestBody QuartzDailyParam quartzDailyParam) {
        try {
            if(quartzDailyParam.getHour() == null || Integer.parseInt(quartzDailyParam.getHour()) < 0 || Integer.parseInt(quartzDailyParam.getHour()) > 23 ) {
                log.debug("schedule job must set hour param");
                GlobalMessageDto messageDto = new GlobalMessageDto(false, "hour must set.");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
            }

            if(quartzDailyParam.getMinute() == null || Integer.parseInt(quartzDailyParam.getMinute()) < 0 || Integer.parseInt(quartzDailyParam.getMinute()) > 59 ) {
                log.debug("schedule job must set minute param");
                GlobalMessageDto messageDto = new GlobalMessageDto(false, "minute must set.");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
            }

            HashMap<String, Object> keyMap = quartzService.scheduleDailyJob(jobName, QuartzParamMapper.INSTANCE.dailyParamToCommon(quartzDailyParam));

            return ResponseEntity.ok(new GlobalMessageDto(true, keyMap));
        } catch (SchedulerException e) {
            log.error("Error while Scheduling batch : ", e);
            GlobalMessageDto messageDto = new GlobalMessageDto(false, "Error while scheduling batch. try again/later.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageDto);
        }
    }


    @ApiOperation(value = "Cron 배치 스케줄링 처리 요청 API", notes = "Cron Expression을 지정하여 주기적으로 파일을 가져갈 수 있게 설정.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API가 정상적으로 Batch Scheduling을 처리함."),
            @ApiResponse(code = 400, message = "Parameter 오류"),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @PostMapping("/cron/{jobName}")
    public ResponseEntity<GlobalMessageDto> scheduleCronBatch(@PathVariable("jobName") String jobName, @Valid @RequestBody QuartzCronParam quartzCronParam) {
        try {
            // input cron validation
            if(quartzCronParam.getCronExpression().isEmpty()) {
                log.debug(" cron expression param is empty.");
                GlobalMessageDto messageDto = new GlobalMessageDto(false, "cron expression param must not be empty.");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
            }

            if(!CronExpression.isValidExpression(quartzCronParam.getCronExpression())) {
                log.debug("wrong cron expression param");
                GlobalMessageDto messageDto = new GlobalMessageDto(false, "wrong cron expression param.");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
            }

            HashMap<String, Object> keyMap = quartzService.scheduleCronJob(jobName, QuartzParamMapper.INSTANCE.cronParamToCommon(quartzCronParam));

            return ResponseEntity.ok(new GlobalMessageDto(true, keyMap));
        } catch (SchedulerException e) {
            log.error("Error while Scheduling batch : ", e);
            GlobalMessageDto messageDto = new GlobalMessageDto(false, "Error while scheduling batch. try again/later.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageDto);
        }
    }


    @ApiOperation(value = "스케줄링 단건 조회 API", notes = "Key(jobName, jobGroup) 기반으로 스케줄링 대상 단건을 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 스케줄링을 조회함."),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @PostMapping("/findSchedule")
    public JobDetailsDto findSchedule(@RequestBody QuartzSearchParam quartzSearchParam) {
        return quartzService.findJobDetailByJobKeys(quartzSearchParam.getJobName(), quartzSearchParam.getJobGroup());
    }

    @ApiOperation(value = "스케줄링 List 조회 API", notes = "Parameter 기반(firmCode 등)으로 스케줄링 대상 리스트를 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 스케줄링 리스트를 조회함."),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @GetMapping("/findScheduleList")
    public List<JobDetailsDto> findScheduleList(@Parameter(name = "기업고유코드", required = true) @RequestParam("firmCode") @NotBlank String firmCode) {
        return quartzService.findJobDetailByFirmCode(firmCode);
    }


    @ApiOperation(value = "스케줄링 단건 삭제 API", notes = "Key(jobName, jobGroup) 기반으로 스케줄링되어있는 대상 단건을 삭제함")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 스케줄링을 삭제함."),
            @ApiResponse(code = 400, message = "Parameter 오류"),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @PostMapping("/delete")
    public ResponseEntity<GlobalMessageDto> deleteSchedule(@Valid @RequestBody QuartzSearchParam quartzSearchParam) {
        try {
            if(quartzSearchParam.getJobGroup() == null || "".equals(quartzSearchParam.getJobGroup())) {
                log.debug("groupName is empty");
                GlobalMessageDto messageDto = new GlobalMessageDto(false, "groupName must not empty.");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
            }
            if(quartzSearchParam.getJobName() == null || "".equals(quartzSearchParam.getJobName())) {
                log.debug("jobName is empty");
                GlobalMessageDto messageDto = new GlobalMessageDto(false, "jobName must not empty.");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
            }

            quartzService.deleteJob(quartzSearchParam.getJobName(), quartzSearchParam.getJobGroup());

            return ResponseEntity.ok(new GlobalMessageDto(true, "deleted Job successfully"));
        } catch (SchedulerException e) {
            log.error("Error while Delete scheduler : " + e);
            GlobalMessageDto messageDto = new GlobalMessageDto(false, "Error while delete scheduler. try again/later.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageDto);
        }
    }


    @ApiOperation(value = "스케줄링 단건 Interrupt API", notes = "Key(jobName, jobGroup) 기반으로 스케줄링되어있는 대상 단건을 동작시 Interrupt시킴.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 스케줄링 Interrupt 처리함."),
            @ApiResponse(code = 400, message = "Parameter 오류"),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @PostMapping("/interrupt")
    public ResponseEntity<GlobalMessageDto> interruptSchedule(@Valid @RequestBody QuartzSearchParam quartzSearchParam) {
        try {
            if(quartzSearchParam.getJobGroup() == null || "".equals(quartzSearchParam.getJobGroup())) {
                log.debug("groupName is empty");
                GlobalMessageDto messageDto = new GlobalMessageDto(false, "groupName must not empty.");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
            }
            if(quartzSearchParam.getJobName() == null || "".equals(quartzSearchParam.getJobName())) {
                log.debug("jobName is empty");
                GlobalMessageDto messageDto = new GlobalMessageDto(false, "jobName must not empty.");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
            }

            quartzService.interruptJob(quartzSearchParam.getJobName(), quartzSearchParam.getJobGroup());

            return ResponseEntity.ok(new GlobalMessageDto(true, "interrupt Job successfully"));
        } catch (SchedulerException e) {
            log.error("Error while Delete scheduler : " + e);
            GlobalMessageDto messageDto = new GlobalMessageDto(false, "Error while delete scheduler. try again/later.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageDto);
        }
    }
}

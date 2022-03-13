package com.kcd.KCDSpringBatch.dto;

import com.kcd.KCDSpringBatch.quartz.domain.JobKeys;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDetailsDto {
    @ApiModelProperty(value = "스케줄링 잡 이름", dataType = "String")
    private String jobName;

    @ApiModelProperty(value = "스케줄링 잡 그룹", dataType = "String")
    private String jobGroup;

//    private String schedName;

//    private String jobDetailDescription;

//    private String triggerName;

//    private String triggerGroup;

//    private String triggerDescription;

    @ApiModelProperty(value = "스케줄링 잡의 다음 실행 예정시간", dataType = "LocalDateTime")
    private LocalDateTime nextFireTime;

    @ApiModelProperty(value = "스케줄링 잡의 이전 실행시간", dataType = "LocalDateTime")
    private LocalDateTime prevFireTime;

//    private Integer priority;

    @ApiModelProperty(value = "스케줄링 잡의 트리거 상태", dataType = "String")
    private String triggerState;

    @ApiModelProperty(value = "스케줄링 잡의 트리거 타입", dataType = "String")
    private String triggerType;

    @ApiModelProperty(value = "스케줄링 잡의 최초 생성시간", dataType = "LocalDateTime")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "스케줄링 잡의 최종 종료시간", dataType = "LocalDateTime")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "스케줄링 잡의 휴일 달력 적용여부", dataType = "boolean")
    private boolean setCalendar;

//    private String misfireInstr;

//    private String jobData;

    @ApiModelProperty(value = "스케줄링 잡의 Cron Expression", dataType = "String")
    private String cronExpression;

//    private String timeZoneId;
}

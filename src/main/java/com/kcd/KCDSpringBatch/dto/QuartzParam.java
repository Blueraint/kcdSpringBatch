package com.kcd.KCDSpringBatch.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Param Object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuartzParam extends BatchParam {
    @ApiModelProperty(value = "단건 배치 시작일자", dataType = "Date", example = "2020-01-01T19:00:00")
    private LocalDateTime fireTime;

    @ApiModelProperty(value = "Cron 표현식", dataType = "Date", example = "0 30 19 ? * *")
    private String cronExpression;

    @ApiModelProperty(value = "일별배치 설정시간", dataType = "String", example = "19")
    private String hour;

    @ApiModelProperty(value = "일별배치 설정분", dataType = "String", example = "30")
    private String minute;

    @ApiModelProperty(value = "휴일 설정 여부", dataType = "boolean", example = "false")
    private boolean holidayExclude = false;

    @ApiModelProperty(value = "스케줄러 잡 그룹", dataType = "String", example = "sampleGroup")
    private String jobGroup;
    @ApiModelProperty(value = "스케줄러 잡 이름", dataType = "String", example = "sampleJob")
    private String jobName;
}

package com.kcd.KCDSpringBatch.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Param Object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuartzCronParam extends BatchParam {
    @ApiModelProperty(value = "Cron 표현식", dataType = "Date", example = "0 30 19 ? * *")
    private String cronExpression;

    @ApiModelProperty(value = "휴일 설정 여부", dataType = "boolean", example = "false")
    private boolean holidayExclude = false;
}

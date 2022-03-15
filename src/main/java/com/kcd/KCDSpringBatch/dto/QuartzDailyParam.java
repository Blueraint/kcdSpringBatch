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
public class QuartzDailyParam extends BatchParam {
    @ApiModelProperty(value = "일별배치 설정시간", dataType = "String", example = "19")
    private String hour;

    @ApiModelProperty(value = "일별배치 설정분", dataType = "String", example = "30")
    private String minute;

    @ApiModelProperty(value = "휴일 설정 여부", dataType = "boolean", example = "false")
    private boolean holidayExclude = false;
}

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
public class QuartzSearchParam {
    @ApiModelProperty(value = "스케줄러 잡 그룹", dataType = "String", example = "sampleGroup")
    private String jobGroup;
    @ApiModelProperty(value = "스케줄러 잡 이름", dataType = "String", example = "sampleJob")
    private String jobName;
}

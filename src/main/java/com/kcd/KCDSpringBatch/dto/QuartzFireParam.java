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
public class QuartzFireParam extends BatchParam {
    @ApiModelProperty(value = "단건 배치 시작일자", dataType = "Date", example = "2020-01-01T19:00:00")
    private LocalDateTime fireTime;

    @ApiModelProperty(value = "휴일 설정 여부", dataType = "boolean", example = "false")
    private boolean holidayExclude = false;
}

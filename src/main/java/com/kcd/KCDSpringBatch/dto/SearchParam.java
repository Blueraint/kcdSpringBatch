package com.kcd.KCDSpringBatch.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchParam {
    @NotBlank
    @ApiModelProperty(value = "기업고유번호", dataType = "String", example = "0001", required = true)
    private String firmCode;

    @ApiModelProperty(value = "추출타입", dataType = "csv/fixed/json", example = "csv",required = true)
    private String fileType;

    @ApiModelProperty(value = "시작일자", dataType = "Date", example = "2020-01-01T19:00:00")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료일자", dataType = "Date", example = "2020-01-01T19:00:00")
    private LocalDateTime endDate;
}

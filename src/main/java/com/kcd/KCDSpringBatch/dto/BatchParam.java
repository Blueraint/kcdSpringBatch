package com.kcd.KCDSpringBatch.dto;

import io.micrometer.core.lang.Nullable;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class BatchParam implements Serializable {
    @NotBlank(message = "firmCode not found")
    @ApiModelProperty(value = "기업고유번호", dataType = "String", example = "0001",required = true)
    private String firmCode;

    @Pattern(regexp = "csv|fixed|json", message = "Wrong fileType.")
    @NotBlank(message = "fileType not found")
    @ApiModelProperty(value = "추출타입", dataType = "csv/fixed/json", example = "csv", required = true)
    private String fileType;

    @ApiModelProperty(value = "시작일자", dataType = "Date", example = "2020-01-01T19:00:00")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료일자", dataType = "Date", example = "2020-01-01T19:00:00")
    private LocalDateTime endDate;


    public Date getStartDate() {
        return (this.startDate != null) ? Timestamp.valueOf(startDate) : null;
    }

    public Date getEndDate() {
        return (this.endDate != null) ? Timestamp.valueOf(endDate) : null;
    }

}

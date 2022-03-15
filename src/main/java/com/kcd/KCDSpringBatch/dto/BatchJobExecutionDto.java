package com.kcd.KCDSpringBatch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BatchJobExecutionDto {
    private Long id;
    private String jobName;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String exitCode;
    private BatchParam batchParam;
}

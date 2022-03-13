package com.kcd.KCDSpringBatch.restcontroller;

import com.kcd.KCDSpringBatch.dto.BatchParam;
import com.kcd.KCDSpringBatch.dto.GlobalMessageDto;
import com.kcd.KCDSpringBatch.service.GeneralBatchLaunchService;
import io.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class BatchJobLauncherRestController {
    private final GeneralBatchLaunchService generalBatchLaunchService;
    private final JobExplorer jobExplorer;

    /* Temporary initialization api*/
    @ApiOperation(value = "테스트를 위한 배치 잡 실행 API", notes = "배치 기본 자료의 입력 및 Spring Batch Job, Datasource 및 ItemReader/Writer 동작 여부를 확인하기 위한 샘플 API")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API가 정상적으로 Batch Job을 Call 하였음. 이후 상태는 Batch Metadata에서 확인 가능."),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @PostMapping("/initialization")
    public GlobalMessageDto initialization() {

        generalBatchLaunchService.runJob("initializationJob",new BatchParam());

        return new GlobalMessageDto(true, "initializationJob called");
    }

    /*
     * 표준화된 배치를 실행하기 위해서 Controller 를 표준화함
     * Server to Server 통신 표준화
     */
    @ApiOperation(value = "배치 잡 실행을 위한 공통 API", notes = "JOB 기반 배치를 확인하기 위한 API. Bean 상태로 등록된 배치 잡만 돌릴 수 있으며, batchJob Package 에서 확인 가능")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API가 정상적으로 Batch Job을 Call 하였음. 이후 상태는 Batch Metadata에서 확인 가능."),
            @ApiResponse(code = 500, message = "Server Error.")
    })

    @PostMapping("/run/{jobName}")
    public GlobalMessageDto batchCaller(@PathVariable("jobName") String jobName, @Valid @RequestBody BatchParam batchParam) {
        // Validation Job
        if(!jobExplorer.getJobNames().contains(jobName)) return new GlobalMessageDto(false, "JOB_NAME[" + jobName + "] Not Found.");

        generalBatchLaunchService.runJob(jobName,batchParam);

        return new GlobalMessageDto(true, jobName + " called");
    }
}

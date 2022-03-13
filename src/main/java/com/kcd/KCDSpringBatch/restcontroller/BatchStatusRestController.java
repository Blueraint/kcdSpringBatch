package com.kcd.KCDSpringBatch.restcontroller;

import com.kcd.KCDSpringBatch.dto.BatchJobExecutionDto;
import com.kcd.KCDSpringBatch.dto.SearchParam;
import com.kcd.KCDSpringBatch.service.BatchJobService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchStatusRestController {
    private final BatchJobService batchJobService;

    @ApiOperation(value = "작동 가능 Batch 조회 API", notes = "Bean 으로 등록되어 있는 Batch API List 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API가 정상적으로 Batch List를 조회함."),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @GetMapping("/findBatchList")
    public List<String> findBatchList() {
        return batchJobService.findBatchList();
    }


    @ApiOperation(value = "Parameter 기반 Server 내 Batch 상태 조회 API", notes = "Bean 으로 등록되어 있는 Batch의 상태를 조회(기업코드, 파일종류, 시작시간, 종료시간 등)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API가 정상적으로 Batch 상태를 조회함."),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @GetMapping("/findBatchStatus")
    public List<BatchJobExecutionDto> findBatchStatus(SearchParam searchParam) {
        return batchJobService.findBatchStatus(searchParam);
    }


    @ApiOperation(value = "Server 내 Batch 상태 전건 조회 API", notes = "Bean 으로 등록되어 있는 Batch 전건의 상태를 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "API가 정상적으로 Batch 상태를 조회함."),
            @ApiResponse(code = 500, message = "Server Error.")
    })
    @GetMapping("/findAllStatus")
    public Page<BatchJobExecutionDto> findAllStatus(Pageable pageable) {
        return batchJobService.findAllStatus(pageable);
    }
}

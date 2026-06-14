package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.summary.MonthlySummaryResponse;
import com.dondoc.service.RecordSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/records")
@Tag(name = "Records Summary", description = "거래 통계 조회 API")
public class RecordSummaryController {

    private final RecordSummaryService recordSummaryService;

    public RecordSummaryController(RecordSummaryService recordSummaryService) {
        this.recordSummaryService = recordSummaryService;
    }

    @GetMapping("/summary")
    @Operation(summary = "월별 요약 통계 조회", description = "사용자의 월별 수입, 지출, 예산 사용률, 카테고리별 상세 통계를 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "월별 요약 통계 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "올바르지 않은 월 형식", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ApiResponse<MonthlySummaryResponse> getMonthlySummary(
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1", in = ParameterIn.HEADER)
            @RequestHeader(value = "userId", required = false) String userId,
            @Parameter(name = "month", description = "조회할 월", required = true, example = "2026-04", in = ParameterIn.QUERY)
            @RequestParam(value = "month", required = false) String month
    ) {
        MonthlySummaryResponse response = recordSummaryService.getMonthlySummary(userId, month);
        return ApiResponse.ok(response, "월별 요약 통계 조회 성공");
    }
}

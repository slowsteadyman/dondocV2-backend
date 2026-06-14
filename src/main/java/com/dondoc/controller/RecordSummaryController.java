package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.Records.MonthlySummaryResponse;
import com.dondoc.service.RecordSummaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/records")
public class RecordSummaryController {

    private final RecordSummaryService recordSummaryService;

    public RecordSummaryController(RecordSummaryService recordSummaryService) {
        this.recordSummaryService = recordSummaryService;
    }

    @GetMapping("/summary")
    public ApiResponse<MonthlySummaryResponse> getMonthlySummary(
            @RequestHeader(value = "userId", required = false) String userId,
            @RequestParam(value = "month", required = false) String month
    ) {
        MonthlySummaryResponse response = recordSummaryService.getMonthlySummary(userId, month);
        return ApiResponse.ok(response, "월별 요약 통계 조회 성공");
    }
}

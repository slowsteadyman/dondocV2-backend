package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.Records.MonthlySettlementResponse;
import com.dondoc.service.RecordSettlementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/records")
public class RecordSettlementController {

    private final RecordSettlementService recordSettlementService;

    public RecordSettlementController(RecordSettlementService recordSettlementService) {
        this.recordSettlementService = recordSettlementService;
    }

    @GetMapping("/settlement")
    public ApiResponse<MonthlySettlementResponse> getMonthlySettlement(
            @RequestHeader(value = "userId", required = false) String userId,
            @RequestParam(value = "month", required = false) String month
    ) {
        MonthlySettlementResponse response = recordSettlementService.getMonthlySettlement(userId, month);
        return ApiResponse.ok(response, "월간 결산 통계 조회 성공");
    }
}

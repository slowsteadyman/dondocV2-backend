package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.Records;
import com.dondoc.dto.Records.DailySummaryResponse;
import com.dondoc.dto.Records.MonthlySummaryResponse;
import com.dondoc.dto.Records.MonthlySettlementResponse;
import com.dondoc.dto.Records.RecordUpdateRequest;
import com.dondoc.dto.Records.RecordUpdateResponse;
import com.dondoc.service.RecordService;
import java.time.YearMonth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    public ApiResponse<Records.RecordSaveResponse> createRecord(
            @RequestHeader Long userId,
            @RequestBody Records.RecordSaveRequest saveRequest) {
        return ApiResponse.ok(recordService.createRecord(userId, saveRequest), "거래 추가 성공");
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<ApiResponse<Records.DeleteResponse>> deleteRecord(
            @RequestHeader(value = "userId", required = false) Long userId,
            @PathVariable Long recordId) {
        return ResponseEntity.ok(ApiResponse.ok(recordService.deleteRecord(userId, recordId), "거래 삭제 성공"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<RecordUpdateResponse>> updateRecord(
            @RequestHeader("userId") long userId,
            @PathVariable long id,
            @RequestBody RecordUpdateRequest dto) {
        return ResponseEntity.ok(ApiResponse.ok(recordService.updateRecord(userId, id, dto), "거래 수정 성공"));
    }

    @GetMapping
    public ResponseEntity<?> getMonthlyRecords(
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestParam String yearMonth,
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(recordService.getMonthlyRecords(userId, yearMonth, type));
    }

    @GetMapping("/summary/daily")
    public ResponseEntity<ApiResponse<List<DailySummaryResponse>>> getDailySummaries(
            @RequestHeader("userId") long userId,
            @RequestParam String month) {
        YearMonth yearMonth = YearMonth.parse(month);
        return ResponseEntity.ok(ApiResponse.ok(recordService.getDailySummaries(userId, yearMonth), "일별 통계 조회 성공"));
    }

    @GetMapping("/summary")
    public ApiResponse<MonthlySummaryResponse> getMonthlySummary(
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestParam(value = "month", required = false) String month) {
        return ApiResponse.ok(recordService.getMonthlySummary(userId, month), "월별 요약 통계 조회 성공");
    }

    @GetMapping("/closing")
    public ApiResponse<MonthlySettlementResponse> getMonthlySettlement(
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestParam(value = "month", required = false) String month) {
        return ApiResponse.ok(recordService.getMonthlySettlement(userId, month), "월간 결산 통계 조회 성공");
    }
}

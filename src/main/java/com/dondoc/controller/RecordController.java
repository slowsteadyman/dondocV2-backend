package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.Categories;
import com.dondoc.dto.MonthlyHistories;
import com.dondoc.dto.Records;
import com.dondoc.dto.Records.DailySummaryResponse;
import com.dondoc.service.RecordService;
import java.time.Year;
import java.time.YearMonth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService){
        this.recordService = recordService;
    }

    /*@GetMapping
    public List<Records> getRecords() {
        return recordService.getRecords();
    }*/

    @GetMapping("/categories")
    public List<Categories> getCategories() {
        return recordService.getCategories();
    }

    @GetMapping("/monthly-history")
    public List<MonthlyHistories> getMonthlyHistory() {
        return recordService.getMonthlyHistories();
    }

    @PostMapping
    public ApiResponse<Records.RecordSaveResponse> createRecord(@RequestHeader Long userId, @RequestBody Records.RecordSaveRequest saveRequest){
        return ApiResponse.ok(recordService.createRecord(userId, saveRequest),"거래 추가 성공");
    }

    @PostMapping("/categories")
    public void createCategory(@RequestBody Categories category){
        recordService.createCategory(category);
    }

    @PostMapping("/monthly-history")
    public void createMonthlyHistory(@RequestBody MonthlyHistories monthlyHistory){
        recordService.createMonthlyHistory(monthlyHistory);
    }

    @GetMapping("/summary/daily")
    public ResponseEntity<ApiResponse<List<DailySummaryResponse>>> getDailySummaries(
            @RequestHeader("userId") long userId,
            @RequestParam String month) {
        YearMonth yearMonth = YearMonth.parse(month);
        List<DailySummaryResponse> data = recordService.getDailySummaries(userId, yearMonth);
        String message = "일별 통계 조회 성공";

        return ResponseEntity.ok(ApiResponse.ok(data, message));

    }
}

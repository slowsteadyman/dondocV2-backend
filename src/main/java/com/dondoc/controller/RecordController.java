package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.Categories;
import com.dondoc.dto.MonthlyHistories;
import com.dondoc.dto.Records;
import com.dondoc.dto.Records.RecordUpdateRequest;
import com.dondoc.dto.Records.RecordUpdateResponse;
import com.dondoc.service.RecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService){
        this.recordService = recordService;
    }

    @GetMapping
    public List<Records> getRecords() {
        return recordService.getRecords();
    }

    @GetMapping("/categories")
    public List<Categories> getCategories() {
        return recordService.getCategories();
    }

    @GetMapping("/monthly-history")
    public List<MonthlyHistories> getMonthlyHistory() {
        return recordService.getMonthlyHistories();
    }

    @PostMapping
    public void createRecord(@RequestBody Records record){
        recordService.createRecord(record);
    }

    @PostMapping("/categories")
    public void createCategory(@RequestBody Categories category){
        recordService.createCategory(category);
    }

    @PostMapping("/monthly-history")
    public void createMonthlyHistory(@RequestBody MonthlyHistories monthlyHistory){
        recordService.createMonthlyHistory(monthlyHistory);
    }

    @PatchMapping("/{id}")
    public ApiResponse<RecordUpdateResponse> updateRecord(
            @RequestHeader("userId") long userId,
            @PathVariable long id,
            @RequestBody RecordUpdateRequest dto
    ) {
        try {
            RecordUpdateResponse data = recordService.updateRecord(id, dto);
            String message = "거래 수정 성공";
            return ApiResponse.ok(data, message);
        } catch (RuntimeException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }
}

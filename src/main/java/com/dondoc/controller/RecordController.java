package com.dondoc.controller;

import com.dondoc.dto.CategoryDto;
import com.dondoc.dto.RecordDto;
import com.dondoc.service.RecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/record")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService){
        this.recordService = recordService;
    }

    @GetMapping("/categories")
    public List<CategoryDto.Category> getCategories() {
        return recordService.getCategories();
    }

    @GetMapping("/monthly-history")
    public List<RecordDto.MonthlyHistory> getMonthlyHistory() {
        return recordService.getMonthlyHistories();
    }

    @PostMapping
    public void createRecord(@RequestBody RecordDto.Record record){
        recordService.createRecord(record);
    }

    @PostMapping("/categories")
    public void createCategory(@RequestBody CategoryDto.Category category){
        recordService.createCategory(category);
    }

    @PostMapping("/monthly-history")
    public void createMonthlyHistory(@RequestBody RecordDto.MonthlyHistory monthlyHistory){
        recordService.createMonthlyHistory(monthlyHistory);
    }

    @GetMapping
    public ResponseEntity<?> getMonthlyRecords(
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestParam String yearMonth, @RequestParam(required = false) String type ){
        return ResponseEntity.ok(recordService.getMonthlyRecords(userId, yearMonth, type));
    }

}

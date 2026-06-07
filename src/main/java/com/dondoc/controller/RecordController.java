package com.dondoc.controller;

import com.dondoc.dto.*;
import com.dondoc.service.RecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

//    @PostMapping
//    public void createRecord(@RequestBody Records record){
//        recordService.createRecord(record);
//    }

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestHeader Long userId, @RequestBody RecordSaveRequest saveRequest){
        RecordSaveResponse response = recordService.createRecord(userId, saveRequest);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response,
                "message", "거래 추가 성공"
        ));
    }

    @PostMapping("/categories")
    public void createCategory(@RequestBody Categories category){
        recordService.createCategory(category);
    }

    @PostMapping("/monthly-history")
    public void createMonthlyHistory(@RequestBody MonthlyHistories monthlyHistory){
        recordService.createMonthlyHistory(monthlyHistory);
    }
}

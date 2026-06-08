package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.Categories;
import com.dondoc.dto.MonthlyHistories;
import com.dondoc.dto.Records;
import com.dondoc.service.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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

    @DeleteMapping("/{recordId}")
    public ResponseEntity<ApiResponse<Long>> deleteRecord(
            @RequestHeader(value = "userId", required = false) Long userId,
            @PathVariable Long recordId
    ) {
        try {
            Long deletedRecordId = recordService.deleteRecord(userId, recordId);
            return ResponseEntity.ok(new ApiResponse<>(true, deletedRecordId, "거래 삭제 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, null, e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, null, e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }
}

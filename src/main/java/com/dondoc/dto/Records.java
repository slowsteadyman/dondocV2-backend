package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class Records {
    private Long id;
    private Long userId;
    private Long categoryId;
    private Long amount;
    private String description;
    private String memo;
    private LocalDate recordDate;
    private LocalDateTime createdAt;
  
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailySummaryResponse {
        private LocalDate date;
        private long income;
        private long expense;
        private int pigLevel;
    }
    
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecordSaveRequest {
        private String type;
        private Long categoryId;
        private LocalDate date;
        private Long amount;
        private String description;
        private String memo;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecordSaveResponse {
        private Long id;
        private String type;
        private Categories.CategoryDto category;
        private LocalDate date;
        private Long amount;
        private String description;
        private String memo;
    }
}

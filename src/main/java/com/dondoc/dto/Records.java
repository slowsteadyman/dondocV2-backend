package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

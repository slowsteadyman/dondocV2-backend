package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RecordDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record {
        private Long id;
        private Long userId;
        private Long categoryId;
        private Long amount;
        private String description;
        private String memo;
        private LocalDate recordDate;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyHistory {
        private Long id;
        private Long userId;
        private LocalDate targetMonth;
        private Float avgRatio;
        private Integer houseLevel;
    }

    @Getter
    @AllArgsConstructor
    public static class ItemResponse {
        private Long id;
        private String type;
        private String date;
        private CategoryDto.Info category;
        private Long amount;
        private String description;
        private String memo;
    }

    @Getter
    @AllArgsConstructor
    public static class MonthlyResponse {
        private Summary summary;
        private List<ItemResponse> records;
    }

    @Getter
    @AllArgsConstructor
    public static class Summary {
        private Long totalIncome;
        private Long totalExpense;
        private Long balance;
    }
}

package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Records {

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
    @AllArgsConstructor
    public static class DeleteResponse {
        private Long recordId;
    }
}

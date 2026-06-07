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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordUpdateRequest {
        private String type;
        private long categoryId;
        private String date;
        private long amount;
        private String description;
        private String memo;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordUpdateResponse {
        private long id;
        private String type;
        private String date;
        private CategoryInfo category;
        private long amount;
        private String description;
        private String memo;

        @AllArgsConstructor
        public static class CategoryInfo {
            private long id;
            private String name;
        }
    }
}

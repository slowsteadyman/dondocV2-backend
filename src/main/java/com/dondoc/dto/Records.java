package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class Records {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailySummaryResponse {
        private LocalDate date;
        private long income;
        private long expense;
        private int pigLevel;
    }
}

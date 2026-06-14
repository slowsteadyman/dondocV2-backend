package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    public static class MonthlySummaryResponse {
        private final String month;
        private final Long totalIncome;
        private final Long totalExpense;
        private final Long netIncome;
        private final Integer savingRate;
        private final Integer transactionCount;
        private final Long avgDailyExpense;
        private final Long monthlyBudget;
        private final Integer budgetUsedPercent;
        private final Long remainBudget;
        private final Long recommendDailyBudget;
        private final List<SummaryDetail> incomeDetail;
        private final List<SummaryDetail> expenseDetail;
    }

    @Getter
    @AllArgsConstructor
    public static class SummaryDetail {
        private final SummaryCategory category;
        private final Long amount;
        private final Integer ratio;
    }

    @Getter
    @AllArgsConstructor
    public static class SummaryCategory {
        private final Long id;
        private final String name;
    }
}

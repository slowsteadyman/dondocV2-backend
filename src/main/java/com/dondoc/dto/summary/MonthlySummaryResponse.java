package com.dondoc.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlySummaryResponse {
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

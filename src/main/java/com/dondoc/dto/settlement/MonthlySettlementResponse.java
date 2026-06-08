package com.dondoc.dto.settlement;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlySettlementResponse {
    private final String month;
    private final Long totalIncome;
    private final Long totalExpense;
    private final Long netIncome;
    private final Long monthlyBudget;
    private final Integer budgetUsedPercent;
    private final Integer avgPigState;
    private final Integer currentHouseLevel;
    private final Integer nextHouseLevel;
    private final List<SettlementCategoryExpense> categoryExpenses;
}

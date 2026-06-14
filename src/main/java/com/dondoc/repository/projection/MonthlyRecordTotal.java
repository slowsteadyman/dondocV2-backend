package com.dondoc.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyRecordTotal {
    private final Long totalIncome;
    private final Long totalExpense;
    private final Integer transactionCount;
}

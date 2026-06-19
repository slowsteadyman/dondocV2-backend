package com.dondoc.repository.projection;

public interface MonthlyRecordTotal {
    Long getTotalIncome();
    Long getTotalExpense();
    Integer getTransactionCount();
}

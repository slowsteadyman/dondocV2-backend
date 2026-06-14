package com.dondoc.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpenseCategorySummary {
    private final Long categoryId;
    private final String categoryName;
    private final Long amount;
}

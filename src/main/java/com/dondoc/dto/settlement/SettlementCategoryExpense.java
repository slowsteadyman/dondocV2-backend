package com.dondoc.dto.settlement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettlementCategoryExpense {
    private final SettlementCategory category;
    private final Long amount;
    private final Integer ratio;
}

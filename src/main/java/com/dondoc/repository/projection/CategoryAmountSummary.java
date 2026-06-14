package com.dondoc.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryAmountSummary {
    private final Long categoryId;
    private final String categoryName;
    private final String categoryType;
    private final Long amount;
}

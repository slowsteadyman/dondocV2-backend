package com.dondoc.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlySettlementHistory {
    private final Float avgRatio;
    private final Integer houseLevel;
}

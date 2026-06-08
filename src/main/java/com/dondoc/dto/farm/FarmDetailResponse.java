package com.dondoc.dto.farm;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FarmDetailResponse {
    private final Long farmId;
    private final String farmName;
    private final Integer memberCount;
    private final Boolean joined;
    private final List<FarmDetailMemberResponse> members;
}

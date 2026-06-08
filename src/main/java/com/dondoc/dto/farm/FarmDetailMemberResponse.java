package com.dondoc.dto.farm;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FarmDetailMemberResponse {
    private final Long userId;
    private final String name;
    private final Integer currentPigLevel;
    private final Integer currentHouseLevel;
    private final LocalDateTime joinedAt;
}

package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Farms {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class FarmDetailResponse {
        private final Long farmId;
        private final String farmName;
        private final Integer memberCount;
        private final Boolean joined;
        private final List<FarmDetailMemberResponse> members;
    }

    @Getter
    @AllArgsConstructor
    public static class FarmDetailMemberResponse {
        private final Long userId;
        private final String name;
        private final Integer currentPigLevel;
        private final Integer currentHouseLevel;
        private final LocalDateTime joinedAt;
    }
}

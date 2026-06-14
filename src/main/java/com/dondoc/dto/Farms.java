package com.dondoc.dto;

import lombok.*;

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
 
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FarmGetResponse {
        private Long farmId;
        private String farmName;
        private Integer memberCount;
        private Boolean joined;
        private LocalDateTime createdAt;
    }
}

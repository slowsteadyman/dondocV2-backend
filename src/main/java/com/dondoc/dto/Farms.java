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
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Farm {
        private Long id;
        private String name;
        private LocalDateTime createdAt;
    }

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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FarmGetResponse {
        private Long farmId;
        private String farmName;
        private Integer memberCount;
        private Boolean joined;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Member {
        private Long id;
        private Long userId;
        private Long farmId;
        private LocalDateTime joinedAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public static class CreateResponse {
        private Long farmId;
        private String farmName;
        private boolean joined;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    public static class LeaveResponse {
        private Long farmId;
        private Long userId;
    }
}

package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class Farms {

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
}

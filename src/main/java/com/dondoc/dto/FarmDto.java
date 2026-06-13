package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class FarmDto {

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
    @AllArgsConstructor
    public static class LeaveResponse {
        private Long farmId;
        private Long userId;
    }
}

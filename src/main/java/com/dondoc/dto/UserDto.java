package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private String userId;
        private String userPassword;
        private String name;
        private Integer age;
        private Integer currentPigLevel;
        private Integer currentHouseLevel;
        private Long monthlyIncome;
        private Integer targetExpenseRatio;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    public static class UserResponse {
        private Long id;
        private String userId;
        private String name;
        private Integer age;
        private Integer currentPigLevel;
        private Integer currentHouseLevel;
        private Long monthlyIncome;
        private Integer targetExpenseRatio;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    public static class MeResponse {
        private String name;
        private Integer age;
        private Integer currentPigLevel;
        private Integer currentHouseLevel;
        private Integer currentCharacterLevel;
        private Long monthlyIncome;
        private Integer targetExpenseRatio;
        private Long monthlyBudget;
        private Long dailyBudget;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PatchRequest {
        private String name;
        private Integer age;
        private Long monthlyIncome;
        private Integer targetExpenseRatio;
    }

    @Getter
    @AllArgsConstructor
    public static class PatchResponse {
        private Long id;
        private String name;
        private Integer age;
        private Long monthlyIncome;
        private Integer targetExpenseRatio;
        private Long monthlyBudget;
        private Long dailyBudget;
    }
}

package com.dondoc.service;

import com.dondoc.dto.summary.MonthlySummaryResponse;
import com.dondoc.dto.summary.SummaryDetail;
import com.dondoc.entity.User;
import com.dondoc.exception.ApiException;
import com.dondoc.repository.RecordRepository;
import com.dondoc.repository.UserRepository;
import com.dondoc.repository.projection.CategoryAmountSummary;
import com.dondoc.repository.projection.MonthlyRecordTotal;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordSummaryServiceTest {

    @Test
    void getMonthlySummaryCalculatesBudgetAndCategoryRatios() {
        RecordSummaryService service = new RecordSummaryService(
                new RecordRepository(null) {
                    @Override
                    public MonthlyRecordTotal findMonthlyTotal(Long userId, LocalDate startDate, LocalDate endDate) {
                        assertEquals(1L, userId);
                        assertEquals(LocalDate.of(2026, 4, 1), startDate);
                        assertEquals(LocalDate.of(2026, 5, 1), endDate);
                        return new MonthlyRecordTotal(3_000_000L, 850_000L, 24);
                    }

                    @Override
                    public List<CategoryAmountSummary> findMonthlyCategoryAmounts(Long userId, LocalDate startDate, LocalDate endDate) {
                        return List.of(
                                new CategoryAmountSummary(6L, "월급", "INCOME", 3_000_000L),
                                new CategoryAmountSummary(1L, "식비", "EXPENSE", 300_000L),
                                new CategoryAmountSummary(2L, "교통비", "EXPENSE", 120_000L),
                                new CategoryAmountSummary(3L, "기타", "EXPENSE", 430_000L)
                        );
                    }
                },
                userRepository(Optional.of(new User(
                        1L,
                        "test-user",
                        "password",
                        "테스트",
                        20,
                        1,
                        3,
                        3_000_000L,
                        40,
                        null
                ))),
                fixedClock()
        );

        MonthlySummaryResponse response = service.getMonthlySummary("1", "2026-04");

        assertEquals("2026-04", response.getMonth());
        assertEquals(3_000_000L, response.getTotalIncome());
        assertEquals(850_000L, response.getTotalExpense());
        assertEquals(2_150_000L, response.getNetIncome());
        assertEquals(72, response.getSavingRate());
        assertEquals(24, response.getTransactionCount());
        assertEquals(28_333L, response.getAvgDailyExpense());
        assertEquals(1_200_000L, response.getMonthlyBudget());
        assertEquals(71, response.getBudgetUsedPercent());
        assertEquals(350_000L, response.getRemainBudget());
        assertEquals(16_666L, response.getRecommendDailyBudget());

        SummaryDetail income = response.getIncomeDetail().get(0);
        assertEquals(6L, income.getCategory().getId());
        assertEquals("월급", income.getCategory().getName());
        assertEquals(3_000_000L, income.getAmount());
        assertEquals(100, income.getRatio());

        SummaryDetail foodExpense = response.getExpenseDetail().get(0);
        assertEquals(1L, foodExpense.getCategory().getId());
        assertEquals("식비", foodExpense.getCategory().getName());
        assertEquals(300_000L, foodExpense.getAmount());
        assertEquals(35, foodExpense.getRatio());
    }

    @Test
    void getMonthlySummaryRejectsInvalidMonthFormat() {
        RecordSummaryService service = new RecordSummaryService(
                new RecordRepository(null),
                userRepository(Optional.empty()),
                fixedClock()
        );

        ApiException exception = assertThrows(
                ApiException.class,
                () -> service.getMonthlySummary("1", "2026-13")
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("올바르지 않은 월 형식", exception.getMessage());
    }

    @Test
    void getMonthlySummaryRejectsMissingUser() {
        RecordSummaryService service = new RecordSummaryService(
                new RecordRepository(null),
                userRepository(Optional.empty()),
                fixedClock()
        );

        ApiException exception = assertThrows(
                ApiException.class,
                () -> service.getMonthlySummary("1", "2026-04")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("인증되지 않은 사용자", exception.getMessage());
    }

    private UserRepository userRepository(Optional<User> user) {
        return new UserRepository(null) {
            @Override
            public Optional<User> findById(Long id) {
                return user;
            }
        };
    }

    private Clock fixedClock() {
        return Clock.fixed(Instant.parse("2026-04-10T00:00:00Z"), ZoneId.of("Asia/Seoul"));
    }
}

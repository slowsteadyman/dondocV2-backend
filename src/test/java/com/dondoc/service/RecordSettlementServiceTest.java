package com.dondoc.service;

import com.dondoc.dto.settlement.MonthlySettlementResponse;
import com.dondoc.dto.settlement.SettlementCategoryExpense;
import com.dondoc.entity.User;
import com.dondoc.exception.ApiException;
import com.dondoc.repository.MonthlyHistoryRepository;
import com.dondoc.repository.RecordRepository;
import com.dondoc.repository.UserRepository;
import com.dondoc.repository.projection.ExpenseCategorySummary;
import com.dondoc.repository.projection.MonthlyRecordAmountSummary;
import com.dondoc.repository.projection.MonthlySettlementHistory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordSettlementServiceTest {

    @Test
    void getMonthlySettlementUsesStoredHistoryBeforeLiveCalculatedFallbacks() {
        RecordSettlementService service = new RecordSettlementService(
                recordRepository(3_000_000L, 850_000L),
                userRepository(Optional.of(user())),
                monthlyHistoryRepository(Optional.of(new MonthlySettlementHistory(71F, 4)))
        );

        MonthlySettlementResponse response = service.getMonthlySettlement("1", "2026-04");

        assertEquals("2026-04", response.getMonth());
        assertEquals(3_000_000L, response.getTotalIncome());
        assertEquals(850_000L, response.getTotalExpense());
        assertEquals(2_150_000L, response.getNetIncome());
        assertEquals(1_200_000L, response.getMonthlyBudget());
        assertEquals(71, response.getBudgetUsedPercent());
        assertEquals(7, response.getAvgPigState());
        assertEquals(3, response.getCurrentHouseLevel());
        assertEquals(4, response.getNextHouseLevel());

        SettlementCategoryExpense food = response.getCategoryExpenses().get(0);
        assertEquals(2L, food.getCategory().getId());
        assertEquals("식비", food.getCategory().getName());
        assertEquals(300_000L, food.getAmount());
        assertEquals(35, food.getRatio());
    }

    @Test
    void getMonthlySettlementCalculatesBudgetPercentAndNextHouseLevelWhenHistoryDoesNotExist() {
        RecordSettlementService service = new RecordSettlementService(
                recordRepository(3_000_000L, 420_000L),
                userRepository(Optional.of(user())),
                monthlyHistoryRepository(Optional.empty())
        );

        MonthlySettlementResponse response = service.getMonthlySettlement("1", "2026-04");

        assertEquals(35, response.getBudgetUsedPercent());
        assertEquals(4, response.getAvgPigState());
        assertEquals(4, response.getNextHouseLevel());
    }

    @Test
    void getMonthlySettlementRejectsInvalidMonthFormat() {
        RecordSettlementService service = new RecordSettlementService(
                recordRepository(0L, 0L),
                userRepository(Optional.empty()),
                monthlyHistoryRepository(Optional.empty())
        );

        ApiException exception = assertThrows(
                ApiException.class,
                () -> service.getMonthlySettlement("1", "2026-13")
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("올바르지 않은 월 형식", exception.getMessage());
    }

    @Test
    void getMonthlySettlementRejectsUnknownUser() {
        RecordSettlementService service = new RecordSettlementService(
                recordRepository(0L, 0L),
                userRepository(Optional.empty()),
                monthlyHistoryRepository(Optional.empty())
        );

        ApiException exception = assertThrows(
                ApiException.class,
                () -> service.getMonthlySettlement("1", "2026-04")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("인증되지 않은 사용자", exception.getMessage());
    }

    private RecordRepository recordRepository(Long totalIncome, Long totalExpense) {
        return new RecordRepository(null) {
            @Override
            public MonthlyRecordAmountSummary findMonthlyAmountSummary(Long userId, LocalDate startDate, LocalDate endDate) {
                assertEquals(1L, userId);
                assertEquals(LocalDate.of(2026, 4, 1), startDate);
                assertEquals(LocalDate.of(2026, 5, 1), endDate);
                return new MonthlyRecordAmountSummary(totalIncome, totalExpense);
            }

            @Override
            public List<ExpenseCategorySummary> findMonthlyExpenseCategories(Long userId, LocalDate startDate, LocalDate endDate) {
                return List.of(
                        new ExpenseCategorySummary(2L, "식비", 300_000L),
                        new ExpenseCategorySummary(3L, "교통비", 120_000L)
                );
            }
        };
    }

    private UserRepository userRepository(Optional<User> user) {
        return new UserRepository(null) {
            @Override
            public Optional<User> findById(Long id) {
                return user;
            }
        };
    }

    private MonthlyHistoryRepository monthlyHistoryRepository(Optional<MonthlySettlementHistory> history) {
        return new MonthlyHistoryRepository(null) {
            @Override
            public Optional<MonthlySettlementHistory> findSettlementHistory(Long userId, String month) {
                assertEquals(1L, userId);
                assertEquals("2026-04", month);
                return history;
            }
        };
    }

    private User user() {
        return new User(
                1L,
                "test-user",
                "password",
                "테스트",
                20,
                5,
                3,
                3_000_000L,
                40,
                null
        );
    }
}

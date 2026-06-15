package com.dondoc.service;

import com.dondoc.dto.Records.MonthlySettlementResponse;
import com.dondoc.dto.Records.SettlementCategory;
import com.dondoc.dto.Records.SettlementCategoryExpense;
import com.dondoc.entity.User;
import com.dondoc.exception.ApiException;
import com.dondoc.repository.MonthlyHistoryRepository;
import com.dondoc.repository.RecordRepository;
import com.dondoc.repository.UserRepository;
import com.dondoc.repository.projection.ExpenseCategorySummary;
import com.dondoc.repository.projection.MonthlyRecordAmountSummary;
import com.dondoc.repository.projection.MonthlySettlementHistory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Optional;

@Service
public class RecordSettlementService {
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM")
            .withResolverStyle(ResolverStyle.STRICT);

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final MonthlyHistoryRepository monthlyHistoryRepository;

    public RecordSettlementService(
            RecordRepository recordRepository,
            UserRepository userRepository,
            MonthlyHistoryRepository monthlyHistoryRepository
    ) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
        this.monthlyHistoryRepository = monthlyHistoryRepository;
    }

    @Transactional(readOnly = true)
    public MonthlySettlementResponse getMonthlySettlement(Long userId, String month) {
        if (userId == null) throw new ApiException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자");
        YearMonth targetMonth = parseMonth(month);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자"));

        LocalDate startDate = targetMonth.atDay(1);
        LocalDate endDate = targetMonth.plusMonths(1).atDay(1);

        MonthlyRecordAmountSummary amountSummary = recordRepository.findMonthlyAmountSummary(userId, startDate, endDate);
        List<ExpenseCategorySummary> expenseCategories = recordRepository.findMonthlyExpenseCategories(userId, startDate, endDate);
        Optional<MonthlySettlementHistory> settlementHistory = monthlyHistoryRepository.findSettlementHistory(
                userId,
                targetMonth.format(MONTH_FORMATTER)
        );

        Long totalIncome = amountSummary.getTotalIncome();
        Long totalExpense = amountSummary.getTotalExpense();
        Long monthlyBudget = calculateMonthlyBudget(user);
        Integer budgetUsedPercent = settlementHistory
                .map(history -> roundPercent(history.getAvgRatio()))
                .orElseGet(() -> percent(totalExpense, monthlyBudget));
        Integer currentHouseLevel = safeLevel(user.getCurrentHouseLevel(), 1);

        return new MonthlySettlementResponse(
                targetMonth.format(MONTH_FORMATTER),
                totalIncome,
                totalExpense,
                totalIncome - totalExpense,
                monthlyBudget,
                budgetUsedPercent,
                calculateAvgPigState(user, budgetUsedPercent),
                currentHouseLevel,
                settlementHistory
                        .map(MonthlySettlementHistory::getHouseLevel)
                        .orElseGet(() -> calculateNextHouseLevel(currentHouseLevel, budgetUsedPercent)),
                toCategoryExpenses(expenseCategories, totalExpense)
        );
    }

    private YearMonth parseMonth(String month) {
        if (!StringUtils.hasText(month)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "올바르지 않은 월 형식");
        }

        try {
            return YearMonth.parse(month, MONTH_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "올바르지 않은 월 형식");
        }
    }

    private Long calculateMonthlyBudget(User user) {
        Long monthlyIncome = user.getMonthlyIncome() == null ? 0L : user.getMonthlyIncome();
        Integer targetExpenseRatio = user.getTargetExpenseRatio() == null ? 0 : user.getTargetExpenseRatio();

        return monthlyIncome * targetExpenseRatio / 100;
    }

    private List<SettlementCategoryExpense> toCategoryExpenses(
            List<ExpenseCategorySummary> expenseCategories,
            Long totalExpense
    ) {
        return expenseCategories.stream()
                .map(category -> new SettlementCategoryExpense(
                        new SettlementCategory(category.getCategoryId(), category.getCategoryName()),
                        category.getAmount(),
                        percent(category.getAmount(), totalExpense)
                ))
                .toList();
    }

    private Integer percent(Long numerator, Long denominator) {
        if (denominator == null || denominator == 0) {
            return 0;
        }

        return (int) Math.round(numerator * 100.0 / denominator);
    }

    private Integer roundPercent(Float value) {
        if (value == null) {
            return 0;
        }

        return Math.round(value);
    }

    private Integer calculateAvgPigState(User user, Integer budgetUsedPercent) {
        Integer currentPigLevel = safeLevel(user.getCurrentPigLevel(), 1);
        if (budgetUsedPercent == null || budgetUsedPercent == 0) {
            return currentPigLevel;
        }

        return clamp((int) Math.round(budgetUsedPercent / 10.0), 1, 10);
    }

    private Integer calculateNextHouseLevel(Integer currentHouseLevel, Integer budgetUsedPercent) {
        if (budgetUsedPercent == null || budgetUsedPercent <= 100) {
            return currentHouseLevel + 1;
        }

        return Math.max(currentHouseLevel - 1, 1);
    }

    private Integer safeLevel(Integer level, Integer defaultLevel) {
        return level == null ? defaultLevel : level;
    }

    private Integer clamp(Integer value, Integer min, Integer max) {
        return Math.max(min, Math.min(max, value));
    }
}

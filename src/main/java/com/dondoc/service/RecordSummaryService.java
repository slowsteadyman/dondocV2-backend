package com.dondoc.service;

import com.dondoc.dto.summary.MonthlySummaryResponse;
import com.dondoc.dto.summary.SummaryCategory;
import com.dondoc.dto.summary.SummaryDetail;
import com.dondoc.entity.User;
import com.dondoc.exception.ApiException;
import com.dondoc.repository.RecordRepository;
import com.dondoc.repository.UserRepository;
import com.dondoc.repository.projection.CategoryAmountSummary;
import com.dondoc.repository.projection.MonthlyRecordTotal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

@Service
public class RecordSummaryService {
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM")
            .withResolverStyle(ResolverStyle.STRICT);

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    public RecordSummaryService(RecordRepository recordRepository, UserRepository userRepository, Clock clock) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public MonthlySummaryResponse getMonthlySummary(String userIdHeader, String month) {
        Long userId = parseUserId(userIdHeader);
        YearMonth targetMonth = parseMonth(month);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자"));

        LocalDate startDate = targetMonth.atDay(1);
        LocalDate endDate = targetMonth.plusMonths(1).atDay(1);

        MonthlyRecordTotal total = recordRepository.findMonthlyTotal(userId, startDate, endDate);
        List<CategoryAmountSummary> categoryAmounts = recordRepository.findMonthlyCategoryAmounts(userId, startDate, endDate);

        Long totalIncome = total.getTotalIncome();
        Long totalExpense = total.getTotalExpense();
        Long netIncome = totalIncome - totalExpense;
        Long monthlyBudget = calculateMonthlyBudget(user);
        Long remainBudget = monthlyBudget - totalExpense;

        List<SummaryDetail> incomeDetail = toDetails(categoryAmounts, totalIncome, CategoryType.INCOME);
        List<SummaryDetail> expenseDetail = toDetails(categoryAmounts, totalExpense, CategoryType.EXPENSE);

        return new MonthlySummaryResponse(
                targetMonth.format(MONTH_FORMATTER),
                totalIncome,
                totalExpense,
                netIncome,
                percent(netIncome, totalIncome),
                total.getTransactionCount(),
                totalExpense / targetMonth.lengthOfMonth(),
                monthlyBudget,
                percent(totalExpense, monthlyBudget),
                remainBudget,
                calculateRecommendDailyBudget(targetMonth, remainBudget),
                incomeDetail,
                expenseDetail
        );
    }

    private Long parseUserId(String userIdHeader) {
        if (!StringUtils.hasText(userIdHeader)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자");
        }

        try {
            return Long.parseLong(userIdHeader);
        } catch (NumberFormatException exception) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자");
        }
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

    private List<SummaryDetail> toDetails(
            List<CategoryAmountSummary> categoryAmounts,
            Long totalAmount,
            CategoryType categoryType
    ) {
        return categoryAmounts.stream()
                .filter(categoryAmount -> categoryType.matches(categoryAmount.getCategoryType()))
                .map(categoryAmount -> new SummaryDetail(
                        new SummaryCategory(categoryAmount.getCategoryId(), categoryAmount.getCategoryName()),
                        categoryAmount.getAmount(),
                        percent(categoryAmount.getAmount(), totalAmount)
                ))
                .toList();
    }

    private Integer percent(Long numerator, Long denominator) {
        if (denominator == null || denominator == 0) {
            return 0;
        }

        return (int) Math.round(numerator * 100.0 / denominator);
    }

    private Long calculateRecommendDailyBudget(YearMonth targetMonth, Long remainBudget) {
        LocalDate today = LocalDate.now(clock);
        YearMonth currentMonth = YearMonth.from(today);

        int remainingDays;
        if (targetMonth.isBefore(currentMonth)) {
            remainingDays = 0;
        } else if (targetMonth.equals(currentMonth)) {
            remainingDays = targetMonth.lengthOfMonth() - today.getDayOfMonth() + 1;
        } else {
            remainingDays = targetMonth.lengthOfMonth();
        }

        if (remainingDays <= 0) {
            return 0L;
        }

        return remainBudget / remainingDays;
    }

    private enum CategoryType {
        INCOME {
            @Override
            boolean matches(String type) {
                return "INCOME".equalsIgnoreCase(type) || "수입".equals(type);
            }
        },
        EXPENSE {
            @Override
            boolean matches(String type) {
                return "EXPENSE".equalsIgnoreCase(type) || "지출".equals(type);
            }
        };

        abstract boolean matches(String type);
    }
}

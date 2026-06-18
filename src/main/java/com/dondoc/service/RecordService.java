package com.dondoc.service;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.Categories;
import com.dondoc.dto.Records;
import com.dondoc.dto.Records.DailySummaryResponse;
import com.dondoc.dto.Records.MonthlySummaryResponse;
import com.dondoc.dto.Records.MonthlySettlementResponse;
import com.dondoc.dto.Records.RecordSaveRequest;
import com.dondoc.dto.Records.RecordUpdateRequest;
import com.dondoc.dto.Records.RecordUpdateResponse;
import com.dondoc.dto.Records.SettlementCategory;
import com.dondoc.dto.Records.SettlementCategoryExpense;
import com.dondoc.dto.Records.SummaryCategory;
import com.dondoc.dto.Records.SummaryDetail;
import com.dondoc.entity.Category;
import com.dondoc.entity.MonthlyHistory;
import com.dondoc.entity.Recorde;
import com.dondoc.entity.User;
import com.dondoc.exception.ApiException;
import com.dondoc.repository.CategoryRepository;
import com.dondoc.repository.MonthlyHistoryRepository;
import com.dondoc.repository.RecordRepository;
import com.dondoc.repository.UserRepository;
import com.dondoc.repository.projection.CategoryAmountSummary;
import com.dondoc.repository.projection.ExpenseCategorySummary;
import com.dondoc.repository.projection.MonthlyRecordAmountSummary;
import com.dondoc.repository.projection.MonthlyRecordTotal;
import com.dondoc.repository.projection.MonthlySettlementHistory;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class RecordService {

    private static final String INCOME = "INCOME";
    private static final String EXPENSE = "EXPENSE";
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM")
            .withResolverStyle(ResolverStyle.STRICT);

    private final RecordRepository recordRepository;
    private final MonthlyHistoryRepository monthlyHistoryRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    public RecordService(
            RecordRepository recordRepository,
            MonthlyHistoryRepository monthlyHistoryRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.monthlyHistoryRepository = monthlyHistoryRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // ── 거래 CRUD ──────────────────────────────────────────────────────────────

    @Transactional
    public Records.RecordSaveResponse createRecord(Long userId, Records.RecordSaveRequest saveRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "존재하지 않는 사용자"));

        Recorde newRecord = Recorde.builder()
            .userId(userId)
            .categoryId(saveRequest.getCategoryId())
            .amount(saveRequest.getAmount())
            .description(saveRequest.getDescription())
            .memo(saveRequest.getMemo())
            .recordDate(saveRequest.getDate())
            .build();

        Recorde saved = recordRepository.save(newRecord);

        Category category = categoryRepository.findById(saved.getCategoryId())
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "카테고리 조회 중 오류 발생"));

        return new Records.RecordSaveResponse(
                saved.getId(),
                category.getType(),
                new Categories.CategoryDto(category.getId(), category.getName()),
                saved.getRecordDate(),
                saved.getAmount(),
                saved.getDescription(),
                saved.getMemo()
        );
    }

    public Records.DeleteResponse deleteRecord(Long userId, Long recordId) {
        if (userId == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "인증 토큰 없음");
        }

        Recorde record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "거래를 찾을 수 없음"));

        if (!record.getUserId().equals(userId)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인 거래가 아님");
        }

        recordRepository.deleteById(recordId);
        return new Records.DeleteResponse(recordId);
    }

    public RecordUpdateResponse updateRecord(long userId, long id, RecordUpdateRequest dto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        Recorde existing = recordRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 거래입니다."));

        if (existing.getUserId() != userId) {
            throw new ApiException(HttpStatus.FORBIDDEN, "본인의 거래만 수정할 수 있습니다.");
        }

        Recorde recorde = new Recorde(
                id, existing.getUserId(), dto.getCategoryId(), dto.getAmount(),
                dto.getDescription(), dto.getMemo(), LocalDate.parse(dto.getDate()), existing.getCreatedAt()
        );
        recordRepository.update(recorde);

        Recorde updated = recordRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "거래 수정 후 조회에 실패했습니다."));
        Category category = categoryRepository.findById(updated.getCategoryId())
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "카테고리 조회에 실패했습니다."));

        return new RecordUpdateResponse(
                updated.getId(), category.getType(), updated.getRecordDate().toString(),
                new Categories.CategoryInfo(category.getId(), category.getName()),
                updated.getAmount(), updated.getDescription(), updated.getMemo()
        );
    }

    // ── 월별 거래 내역 ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ApiResponse<Records.MonthlyResponse> getMonthlyRecords(Long userId, String yearMonth, String type) {
        if (userId == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        List<Records.ItemResponse> records = recordRepository.findByUserMonth(userId, yearMonth, type);
        Records.Summary summary = recordRepository.findSummaryByUserMonth(userId, yearMonth, type);
        return new ApiResponse<>(true, new Records.MonthlyResponse(summary, records), "거래 내역 조회 성공");
    }

    // ── 일별 통계 ──────────────────────────────────────────────────────────────

    public List<DailySummaryResponse> getDailySummaries(long userId, YearMonth yearMonth) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<Recorde> records = recordRepository.findByUserIdAndRecordDateBetween(userId, start, end);
        Map<Long, String> categoryTypeMap = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getType));

        Map<LocalDate, long[]> summaryByDate = new HashMap<>();
        for (Recorde record : records) {
            LocalDate date = record.getRecordDate();
            String type = categoryTypeMap.get(record.getCategoryId());
            summaryByDate.putIfAbsent(date, new long[]{0L, 0L});
            if (INCOME.equals(type)) summaryByDate.get(date)[0] += record.getAmount();
            else if (EXPENSE.equals(type)) summaryByDate.get(date)[1] += record.getAmount();
        }

        List<DailySummaryResponse> result = new ArrayList<>();
        new TreeMap<>(summaryByDate).forEach((date, amounts) ->
                result.add(new DailySummaryResponse(date, amounts[0], amounts[1], 0)));
        return result;
    }

    // ── 월별 요약 통계 ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public MonthlySummaryResponse getMonthlySummary(Long userId, String month) {
        if (userId == null) throw new ApiException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자");
        YearMonth targetMonth = parseMonth(month);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자"));

        LocalDate startDate = targetMonth.atDay(1);
        LocalDate endDate = targetMonth.plusMonths(1).atDay(1);

        MonthlyRecordTotal total = recordRepository.findMonthlyTotal(userId, startDate, endDate);
        List<CategoryAmountSummary> categoryAmounts = recordRepository.findMonthlyCategoryAmounts(userId, startDate, endDate);

        Long totalIncome = total.getTotalIncome();
        Long totalExpense = total.getTotalExpense();
        Long monthlyBudget = calculateMonthlyBudget(user);
        Long remainBudget = monthlyBudget - totalExpense;

        return new MonthlySummaryResponse(
                targetMonth.format(MONTH_FORMATTER),
                totalIncome,
                totalExpense,
                totalIncome - totalExpense,
                percent(totalIncome - totalExpense, totalIncome),
                total.getTransactionCount(),
                totalExpense / targetMonth.lengthOfMonth(),
                monthlyBudget,
                percent(totalExpense, monthlyBudget),
                remainBudget,
                calculateRecommendDailyBudget(targetMonth, remainBudget),
                toSummaryDetails(categoryAmounts, totalIncome, INCOME),
                toSummaryDetails(categoryAmounts, totalExpense, EXPENSE)
        );
    }

    // ── 월간 결산 ──────────────────────────────────────────────────────────────

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
        Optional<MonthlySettlementHistory> settlementHistory = monthlyHistoryRepository.findByUserIdAndTargetMonth(
                userId, targetMonth.format(MONTH_FORMATTER));

        Long totalIncome = amountSummary.getTotalIncome();
        Long totalExpense = amountSummary.getTotalExpense();
        Long monthlyBudget = calculateMonthlyBudget(user);
        Integer avgExpenseRatio = settlementHistory
                .map(h -> roundPercent(h.getAvgRatio()))
                .orElseGet(() -> percent(totalExpense, monthlyBudget));
        Integer currentHouseLevel = safeLevel(user.getCurrentHouseLevel(), 1);

        return new MonthlySettlementResponse(
                targetMonth.format(MONTH_FORMATTER),
                totalIncome,
                totalExpense,
                totalIncome - totalExpense,
                monthlyBudget,
                avgExpenseRatio,
                calculateAvgPigLevel(user, avgExpenseRatio),
                currentHouseLevel,
                settlementHistory
                        .map(MonthlySettlementHistory::getHouseLevel)
                        .orElseGet(() -> calculateNextHouseLevel(currentHouseLevel, avgExpenseRatio)),
                toSettlementCategoryExpenses(expenseCategories, totalExpense)
        );
    }

    // ── 기타 (내부/레거시) ────────────────────────────────────────────────────

    public List<Records.Record> getRecords() {
        return recordRepository.findAll().stream()
                .map(e -> new Records.Record(e.getId(), e.getUserId(), e.getCategoryId(), e.getAmount(),
                        e.getDescription(), e.getMemo(), e.getRecordDate(), e.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public List<Records.MonthlyHistory> getMonthlyHistories() {
        return monthlyHistoryRepository.findAll().stream()
                .map(e -> new Records.MonthlyHistory(e.getId(), e.getUserId(), e.getTargetMonth(),
                        e.getAvgRatio(), e.getHouseLevel()))
                .collect(Collectors.toList());
    }

    public List<Categories.Category> getCategories() {
        return categoryRepository.findAll().stream()
                .map(e -> new Categories.Category(e.getId(), e.getName(), e.getIcon(), e.getType()))
                .collect(Collectors.toList());
    }

    public void createMonthlyHistory(Records.MonthlyHistory dto) {
        monthlyHistoryRepository.save(new MonthlyHistory(
                null, dto.getUserId(), dto.getTargetMonth(), dto.getAvgRatio(), dto.getHouseLevel()));
    }

    public void createCategory(Categories.Category dto) {
        categoryRepository.save(new Category(null, dto.getName(), dto.getIcon(), dto.getType()));
    }

    // ── private helpers ────────────────────────────────────────────────────────

    private YearMonth parseMonth(String month) {
        if (!StringUtils.hasText(month)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "올바르지 않은 월 형식");
        }
        try {
            return YearMonth.parse(month, MONTH_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "올바르지 않은 월 형식");
        }
    }

    private Long calculateMonthlyBudget(User user) {
        Long monthlyIncome = user.getMonthlyIncome() == null ? 0L : user.getMonthlyIncome();
        Integer targetExpenseRatio = user.getTargetExpenseRatio() == null ? 0 : user.getTargetExpenseRatio();
        return monthlyIncome * targetExpenseRatio / 100;
    }

    private Integer percent(Long numerator, Long denominator) {
        if (denominator == null || denominator == 0) return 0;
        return (int) Math.round(numerator * 100.0 / denominator);
    }

    private Integer roundPercent(Float value) {
        return value == null ? 0 : Math.round(value);
    }

    private Integer safeLevel(Integer level, Integer defaultLevel) {
        return level == null ? defaultLevel : level;
    }

    private Integer clamp(Integer value, Integer min, Integer max) {
        return Math.max(min, Math.min(max, value));
    }

    private Long calculateRecommendDailyBudget(YearMonth targetMonth, Long remainBudget) {
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);
        int remainingDays;
        if (targetMonth.isBefore(currentMonth)) {
            remainingDays = 0;
        } else if (targetMonth.equals(currentMonth)) {
            remainingDays = targetMonth.lengthOfMonth() - today.getDayOfMonth() + 1;
        } else {
            remainingDays = targetMonth.lengthOfMonth();
        }
        return remainingDays <= 0 ? 0L : remainBudget / remainingDays;
    }

    private Integer calculateAvgPigLevel(User user, Integer budgetUsedPercent) {
        Integer currentPigLevel = safeLevel(user.getCurrentPigLevel(), 1);
        if (budgetUsedPercent == null || budgetUsedPercent == 0) return currentPigLevel;
        return clamp((int) Math.round(budgetUsedPercent / 10.0), 1, 10);
    }

    private Integer calculateNextHouseLevel(Integer currentHouseLevel, Integer budgetUsedPercent) {
        if (budgetUsedPercent == null || budgetUsedPercent <= 100) return currentHouseLevel + 1;
        return Math.max(currentHouseLevel - 1, 1);
    }

    private List<SummaryDetail> toSummaryDetails(
            List<CategoryAmountSummary> categoryAmounts, Long totalAmount, String type) {
        return categoryAmounts.stream()
                .filter(c -> hasType(c.getCategoryType(), type))
                .map(c -> new SummaryDetail(
                        new SummaryCategory(c.getCategoryId(), c.getCategoryName()),
                        c.getAmount(),
                        percent(c.getAmount(), totalAmount)))
                .toList();
    }

    private List<SettlementCategoryExpense> toSettlementCategoryExpenses(
            List<ExpenseCategorySummary> expenseCategories, Long totalExpense) {
        return expenseCategories.stream()
                .map(c -> new SettlementCategoryExpense(
                        new SettlementCategory(c.getCategoryId(), c.getCategoryName()),
                        c.getAmount(),
                        percent(c.getAmount(), totalExpense)))
                .toList();
    }

    private boolean hasType(String actualType, String expectedType) {
        if (INCOME.equals(expectedType)) return INCOME.equalsIgnoreCase(actualType) || "수입".equals(actualType);
        if (EXPENSE.equals(expectedType)) return EXPENSE.equalsIgnoreCase(actualType) || "지출".equals(actualType);
        return false;
    }
}

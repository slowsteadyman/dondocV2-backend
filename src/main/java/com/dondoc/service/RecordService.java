package com.dondoc.service;

import com.dondoc.dto.Categories;
import com.dondoc.dto.MonthlyHistories;
import com.dondoc.dto.Records;
import com.dondoc.dto.Records.DailySummaryResponse;
import com.dondoc.entity.Category;
import com.dondoc.entity.MonthlyHistory;
import com.dondoc.entity.Recorde;
import com.dondoc.repository.CategoryRepository;
import com.dondoc.repository.MonthlyHistoryRepository;
import com.dondoc.repository.RecordRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final MonthlyHistoryRepository monthlyHistoryRepository;
    private final CategoryRepository categoryRepository;

    public RecordService(RecordRepository recordRepository, MonthlyHistoryRepository monthlyHistoryRepository, CategoryRepository categoryRepository){
        this.recordRepository = recordRepository;
        this.monthlyHistoryRepository = monthlyHistoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<MonthlyHistories> getMonthlyHistories(){
        List<MonthlyHistory> entities = monthlyHistoryRepository.findAll();
        return entities.stream()
                .map(entity -> new MonthlyHistories(
                        entity.getId(),
                        entity.getUserId(),
                        entity.getTargetMonth(),
                        entity.getAvgRatio(),
                        entity.getHouseLevel()
                ))
                .collect(Collectors.toList());
    }

    public List<Categories> getCategories(){
        List<Category> entities = categoryRepository.findAll();
        return entities.stream()
                .map(entity -> new Categories(
                        entity.getId(),
                        entity.getName(),
                        entity.getIcon(),
                        entity.getType()
                ))
                .collect(Collectors.toList());
    }

    /*public void createRecord(Records dto){
        Recorde recorde = new Recorde(
                null, dto.getUserId(), dto.getCategoryId(),
                dto.getAmount(), dto.getDescription(), dto.getMemo(), dto.getRecordDate(),
                dto.getCreatedAt()
        );
        recordRepository.save(recorde);
    }*/

    public void createMonthlyHistory(MonthlyHistories dto){
        MonthlyHistory monthlyHistory = new MonthlyHistory(
                null, dto.getUserId(), dto.getTargetMonth(),
                dto.getAvgRatio(), dto.getHouseLevel()
        );
        monthlyHistoryRepository.save(monthlyHistory);
    }

    public void createCategory(Categories dto){
        Category category = new Category(
                null, dto.getName(), dto.getIcon(),
                dto.getType()
        );
        categoryRepository.save(category);
    }

    public List<DailySummaryResponse> getDailySummaries(long userId, YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<Recorde> records = recordRepository.findByDateRange(userId, start, end);

        Map<Long, String> categoryTypeMap = categoryRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Category::getId, Category::getType));

        Map<LocalDate, long[]> summaryByDate = new HashMap<>();

        for (Recorde record : records) {
            LocalDate date = record.getRecordDate();
            String type = categoryTypeMap.get(record.getCategoryId());

            summaryByDate.putIfAbsent(date, new long[]{0L, 0L});

            if ("INCOME".equals(type)) {
                summaryByDate.get(date)[0] += record.getAmount();
            } else if ("EXPENSE".equals(type)) {
                summaryByDate.get(date)[1] += record.getAmount();
            }
        }

        // 날짜 오름차순 정렬을 위해 TreeMap으로 변환
        Map<LocalDate, long[]> sorted = new TreeMap<>(summaryByDate);

        List<DailySummaryResponse> result = new ArrayList<>();
        for (Map.Entry<LocalDate, long[]> entry : sorted.entrySet()) {
            LocalDate date = entry.getKey();
            long income = entry.getValue()[0];
            long expense = entry.getValue()[1];
            result.add(new DailySummaryResponse(date, income, expense, 0));
        }

        return result;
    }
}

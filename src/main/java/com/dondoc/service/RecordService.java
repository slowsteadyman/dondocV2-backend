package com.dondoc.service;

import com.dondoc.dto.*;
import com.dondoc.entity.Category;
import com.dondoc.entity.MonthlyHistory;
import com.dondoc.entity.Recorde;
import com.dondoc.repository.CategoryRepository;
import com.dondoc.repository.MonthlyHistoryRepository;
import com.dondoc.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Records> getRecords(){
        List<Recorde> entities = recordRepository.findAll();
        return entities.stream()
                .map(entity -> new Records(
                        entity.getId(),
                        entity.getUserId(),
                        entity.getCategoryId(),
                        entity.getAmount(),
                        entity.getDescription(),
                        entity.getMemo(),
                        entity.getRecordDate(),
                        entity.getCreatedAt()
                ))
                .collect(Collectors.toList());
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

    // 사용하지 않을 수도 있다
    public void createRecord(Records dto){
        Recorde recorde = new Recorde(
                null, dto.getUserId(), dto.getCategoryId(),
                dto.getAmount(), dto.getDescription(), dto.getMemo(), dto.getRecordDate(),
                dto.getCreatedAt()
        );
        recordRepository.save(recorde);
    }

    public RecordSaveResponse createRecord(Long userId, RecordSaveRequest saveRequest) {
        Long savedId = recordRepository.save(userId, saveRequest);
        Recorde recorde = recordRepository.findById(savedId);
        Category category = categoryRepository.findById(recorde.getCategoryId());

        return new RecordSaveResponse(
                recorde.getId(),
                category.getType(),
                new CategoryDto(category.getId(), category.getName()),
                recorde.getRecordDate(),
                recorde.getAmount(),
                recorde.getDescription(),
                recorde.getMemo()
        );
    }

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

}

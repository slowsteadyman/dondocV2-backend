package com.dondoc.service;

import com.dondoc.dto.Categories;
import com.dondoc.dto.MonthlyHistories;
import com.dondoc.dto.Records;
import com.dondoc.entity.Category;
import com.dondoc.entity.MonthlyHistory;
import com.dondoc.entity.Recorde;
import com.dondoc.exception.ApiException;
import com.dondoc.repository.CategoryRepository;
import com.dondoc.repository.MonthlyHistoryRepository;
import com.dondoc.repository.RecordRepository;
import com.dondoc.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final MonthlyHistoryRepository monthlyHistoryRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public RecordService(RecordRepository recordRepository, MonthlyHistoryRepository monthlyHistoryRepository, CategoryRepository categoryRepository, UserRepository userRepository){
        this.recordRepository = recordRepository;
        this.monthlyHistoryRepository = monthlyHistoryRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
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

    @Transactional
    public Records.RecordSaveResponse createRecord(Long userId, Records.RecordSaveRequest saveRequest) {
        // 정합성 검사
        userRepository.findById(userId).orElseThrow(()->new ApiException(HttpStatus.CONFLICT, "존재하지 않는 사용자"));

        Long savedId = recordRepository.save(userId, saveRequest);
        Recorde recorde = recordRepository.findById(savedId).orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "거래 추가 중 에러 발생"));
        Category category = categoryRepository.findById(recorde.getCategoryId()).orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "카테고리 조회 중 오류 발생"));

        return new Records.RecordSaveResponse(
                recorde.getId(),
                category.getType(),
                new Categories.CategoryDto(category.getId(), category.getName()),
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

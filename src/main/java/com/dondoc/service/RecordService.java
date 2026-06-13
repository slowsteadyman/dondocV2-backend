package com.dondoc.service;

import com.dondoc.dto.*;
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

    public RecordService(
            RecordRepository recordRepository,
            MonthlyHistoryRepository monthlyHistoryRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository
    ){
        this.recordRepository = recordRepository;
        this.monthlyHistoryRepository = monthlyHistoryRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<RecordDto.Record> getRecords(){
        List<Recorde> entities = recordRepository.findAll();
        return entities.stream()
                .map(entity -> new RecordDto.Record(
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

    public List<RecordDto.MonthlyHistory> getMonthlyHistories(){
        List<MonthlyHistory> entities = monthlyHistoryRepository.findAll();
        return entities.stream()
                .map(entity -> new RecordDto.MonthlyHistory(
                        entity.getId(),
                        entity.getUserId(),
                        entity.getTargetMonth(),
                        entity.getAvgRatio(),
                        entity.getHouseLevel()
                ))
                .collect(Collectors.toList());
    }

    public List<CategoryDto.Category> getCategories(){
        List<Category> entities = categoryRepository.findAll();
        return entities.stream()
                .map(entity -> new CategoryDto.Category(
                        entity.getId(),
                        entity.getName(),
                        entity.getIcon(),
                        entity.getType()
                ))
                .collect(Collectors.toList());
    }

    public void createRecord(RecordDto.Record dto){
        Recorde recorde = new Recorde(
                null, dto.getUserId(), dto.getCategoryId(),
                dto.getAmount(), dto.getDescription(), dto.getMemo(), dto.getRecordDate(),
                dto.getCreatedAt()
        );
        recordRepository.save(recorde);
    }

    public void createMonthlyHistory(RecordDto.MonthlyHistory dto){
        MonthlyHistory monthlyHistory = new MonthlyHistory(
                null, dto.getUserId(), dto.getTargetMonth(),
                dto.getAvgRatio(), dto.getHouseLevel()
        );
        monthlyHistoryRepository.save(monthlyHistory);
    }

    public void createCategory(CategoryDto.Category dto){
        Category category = new Category(
                null, dto.getName(), dto.getIcon(),
                dto.getType()
        );
        categoryRepository.save(category);
    }

    //  1. repository에서 records와 summary 가져오기
    //  2. summary + records 합쳐서 응답 반환
    @Transactional(readOnly = true)
    public ApiResponse<RecordDto.MonthlyResponse> getMonthlyRecords(Long userId, String yearMonth, String type) {
        if (userId == null) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "인증되지 않은 사용자입니다."
            );
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }

        List<RecordDto.ItemResponse> records = recordRepository.findByUserMonth(userId, yearMonth, type);
        RecordDto.Summary summary = recordRepository.findSummaryByUserMonth(userId, yearMonth, type);
        RecordDto.MonthlyResponse data = new RecordDto.MonthlyResponse(summary, records);

        return new ApiResponse<>(true, data, "거래 내역 조회 성공");

    }

}

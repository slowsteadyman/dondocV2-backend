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
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

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

    public List<Records.Record> getRecords(){
        List<Recorde> entities = recordRepository.findAll();
        return entities.stream()
                .map(entity -> new Records.Record(
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

    public void createRecord(Records.Record dto){
        Recorde recorde = new Recorde(
                null, dto.getUserId(), dto.getCategoryId(),
                dto.getAmount(), dto.getDescription(), dto.getMemo(), dto.getRecordDate(),
                dto.getCreatedAt()
        );
        recordRepository.save(recorde);
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

}

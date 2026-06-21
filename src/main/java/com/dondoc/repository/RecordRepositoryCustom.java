package com.dondoc.repository;

import com.dondoc.dto.Records;

import java.util.List;

public interface RecordRepositoryCustom {
    List<Records.ItemResponse> findByUserMonth(Long userId, String yearMonth, String type);
    Records.Summary findSummaryByUserMonth(Long userId, String yearMonth, String type);
}

package com.dondoc.repository;

import com.dondoc.entity.MonthlyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.dondoc.repository.projection.MonthlySettlementHistory;

public interface MonthlyHistoryRepository extends JpaRepository<MonthlyHistory, Long> {
    Optional<MonthlySettlementHistory> findByUserIdAndTargetMonth(Long userId, String targetMonth);
}

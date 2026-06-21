package com.dondoc.repository;

import com.dondoc.entity.Recorde;
import com.dondoc.repository.projection.CategoryAmountSummary;
import com.dondoc.repository.projection.ExpenseCategorySummary;
import com.dondoc.repository.projection.MonthlyRecordAmountSummary;
import com.dondoc.repository.projection.MonthlyRecordTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Recorde, Long>, RecordRepositoryCustom {

    List<Recorde> findByUserIdAndRecordDateBetween(Long userId, LocalDate start, LocalDate end);

    @Query("""
        SELECT
            COALESCE(SUM(CASE
                WHEN UPPER(c.type) = 'INCOME' OR c.type = '수입' THEN r.amount
                ELSE 0
            END), 0) AS totalIncome,
            COALESCE(SUM(CASE
                WHEN UPPER(c.type) = 'EXPENSE' OR c.type = '지출' THEN r.amount
                ELSE 0
            END), 0) AS totalExpense,
            COUNT(r.id) AS transactionCount
        FROM Recorde r
        LEFT JOIN Category c ON r.categoryId = c.id
        WHERE r.userId = :userId
          AND r.recordDate >= :startDate
          AND r.recordDate < :endDate
    """)
    MonthlyRecordTotal findMonthlyTotal(
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query("""
        SELECT
            c.id AS categoryId,
            c.name AS categoryName,
            c.type AS categoryType,
            SUM(r.amount) AS amount
        FROM Recorde r
        INNER JOIN Category c ON r.categoryId = c.id
        WHERE r.userId = :userId
          AND r.recordDate >= :startDate
          AND r.recordDate < :endDate
        GROUP BY c.id, c.name, c.type
        ORDER BY amount DESC, c.id ASC
    """)
    List<CategoryAmountSummary> findMonthlyCategoryAmounts(
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query("""
        SELECT
            COALESCE(SUM(CASE
                WHEN UPPER(c.type) = 'INCOME' OR c.type = '수입' THEN r.amount
                ELSE 0
            END), 0) AS totalIncome,
            COALESCE(SUM(CASE
                WHEN UPPER(c.type) = 'EXPENSE' OR c.type = '지출' THEN r.amount
                ELSE 0
            END), 0) AS totalExpense
        FROM Recorde r
        INNER JOIN Category c ON r.categoryId = c.id
        WHERE r.userId = :userId
          AND r.recordDate >= :startDate
          AND r.recordDate < :endDate
    """)
    MonthlyRecordAmountSummary findMonthlyAmountSummary(
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query("""
        SELECT
            c.id AS categoryId,
            c.name AS categoryName,
            SUM(r.amount) AS amount
        FROM Recorde r
        INNER JOIN Category c ON r.categoryId = c.id
        WHERE r.userId = :userId
          AND r.recordDate >= :startDate
          AND r.recordDate < :endDate
          AND (UPPER(c.type) = 'EXPENSE' OR c.type = '지출')
        GROUP BY c.id, c.name
        ORDER BY amount DESC, c.id ASC
    """)
    List<ExpenseCategorySummary> findMonthlyExpenseCategories(
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
}

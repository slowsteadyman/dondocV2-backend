package com.dondoc.repository;

import com.dondoc.entity.Recorde;
import com.dondoc.repository.projection.ExpenseCategorySummary;
import com.dondoc.repository.projection.MonthlyRecordAmountSummary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RecordRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecordRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Recorde> findAll(){
        String sql = "SELECT * FROM records";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Recorde(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("category_id"),
                rs.getLong("amount"),
                rs.getString("description"),
                rs.getString("memo"),
                rs.getObject("record_date", LocalDate.class),
                rs.getObject("created_at", LocalDateTime.class)
        ));
    }

    public MonthlyRecordAmountSummary findMonthlyAmountSummary(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT
                    COALESCE(SUM(CASE
                        WHEN UPPER(c.type) = 'INCOME' OR c.type = '수입' THEN r.amount
                        ELSE 0
                    END), 0) AS total_income,
                    COALESCE(SUM(CASE
                        WHEN UPPER(c.type) = 'EXPENSE' OR c.type = '지출' THEN r.amount
                        ELSE 0
                    END), 0) AS total_expense
                FROM records r
                INNER JOIN categories c ON r.category_id = c.id
                WHERE r.user_id = ?
                  AND r.record_date >= ?
                  AND r.record_date < ?
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new MonthlyRecordAmountSummary(
                rs.getLong("total_income"),
                rs.getLong("total_expense")
        ), userId, startDate, endDate);
    }

    public List<ExpenseCategorySummary> findMonthlyExpenseCategories(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT
                    c.id AS category_id,
                    c.name AS category_name,
                    SUM(r.amount) AS amount
                FROM records r
                INNER JOIN categories c ON r.category_id = c.id
                WHERE r.user_id = ?
                  AND r.record_date >= ?
                  AND r.record_date < ?
                  AND (UPPER(c.type) = 'EXPENSE' OR c.type = '지출')
                GROUP BY c.id, c.name
                ORDER BY amount DESC, c.id ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ExpenseCategorySummary(
                rs.getLong("category_id"),
                rs.getString("category_name"),
                rs.getLong("amount")
        ), userId, startDate, endDate);
    }

    public void save(Recorde recorde) {
        String sql = "INSERT INTO records (user_id, category_id, amount, description, memo, record_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, recorde.getUserId(), recorde.getCategoryId(), recorde.getAmount(), recorde.getDescription(), recorde.getMemo(), recorde.getRecordDate(), recorde.getCreatedAt());
    }
}

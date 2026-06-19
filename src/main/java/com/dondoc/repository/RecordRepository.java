package com.dondoc.repository;

import com.dondoc.dto.Records;
import com.dondoc.dto.Categories;
import com.dondoc.entity.Recorde;
import com.dondoc.repository.projection.ExpenseCategorySummary;
import com.dondoc.repository.projection.MonthlyRecordAmountSummary;
import com.dondoc.repository.projection.CategoryAmountSummary;
import com.dondoc.repository.projection.MonthlyRecordTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Recorde, Long> {

    public MonthlyRecordTotal findMonthlyTotal(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT
                    COALESCE(SUM(CASE
                        WHEN UPPER(c.type) = 'INCOME' OR c.type = '수입' THEN r.amount
                        ELSE 0
                    END), 0) AS total_income,
                    COALESCE(SUM(CASE
                        WHEN UPPER(c.type) = 'EXPENSE' OR c.type = '지출' THEN r.amount
                        ELSE 0
                    END), 0) AS total_expense,
                    COUNT(r.id) AS transaction_count
                FROM records r
                LEFT JOIN categories c ON r.category_id = c.id
                WHERE r.user_id = ?
                  AND r.record_date >= ?
                  AND r.record_date < ?
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new MonthlyRecordTotal(
                rs.getLong("total_income"),
                rs.getLong("total_expense"),
                rs.getInt("transaction_count")
        ), userId, startDate, endDate);
    }

    public List<CategoryAmountSummary> findMonthlyCategoryAmounts(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT
                    c.id AS category_id,
                    c.name AS category_name,
                    c.type AS category_type,
                    SUM(r.amount) AS amount
                FROM records r
                INNER JOIN categories c ON r.category_id = c.id
                WHERE r.user_id = ?
                  AND r.record_date >= ?
                  AND r.record_date < ?
                GROUP BY c.id, c.name, c.type
                ORDER BY amount DESC, c.id ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new CategoryAmountSummary(
                rs.getLong("category_id"),
                rs.getString("category_name"),
                rs.getString("category_type"),
                rs.getLong("amount")
        ), userId, startDate, endDate);
    }

    List<Recorde> findByUserIdAndRecordDateBetween(Long userId, LocalDate start, LocalDate end);
    /*public List<Recorde> findByDateRange(long userId, LocalDate start, LocalDate end) {
        String sql = "SELECT * FROM records WHERE user_id = ? AND record_date BETWEEN ? AND ?";

        return jdbcTemplate.query(sql,
            (rs, rowNum) -> new Recorde(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("category_id"),
                rs.getLong("amount"),
                rs.getString("description"),
                rs.getString("memo"),
                rs.getObject("record_date", LocalDate.class),
                rs.getObject("created_at", LocalDateTime.class)
            ),
            userId, start, end
        );
    }*/

    /*public Long save(Long userId, Records.RecordSaveRequest saveRequest) {
        String sql = "INSERT INTO records (user_id, category_id, amount, description, memo, record_date) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, userId);
            pstmt.setLong(2, saveRequest.getCategoryId());
            pstmt.setLong(3, saveRequest.getAmount());
            pstmt.setString(4, saveRequest.getDescription());
            pstmt.setString(5, saveRequest.getMemo());
            pstmt.setDate(6, Date.valueOf(saveRequest.getDate()));

            return pstmt;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }*/

    public int update(Recorde recorde) {
        String sql = "UPDATE records SET category_id = ?, amount = ?, description = ?, memo = ?, record_date = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
            recorde.getCategoryId(),
            recorde.getAmount(),
            recorde.getDescription(),
            recorde.getMemo(),
            recorde.getRecordDate(),
            recorde.getId()
        );
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

    public int deleteById(Long id) {
        String sql = "DELETE FROM records WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Records.ItemResponse> findByUserMonth(Long userId, String yearMonth, String type){
        String sql = "SELECT r.id, r.amount, r.description, r.memo, r.record_date, " +
                "c.id as category_id, c.name as category_name, c.type as category_type " +
                "FROM records r JOIN categories c ON r.category_id = c.id " +
                "WHERE r.user_id = ? AND DATE_FORMAT(r.record_date, '%Y-%m') = ?";

        if (type != null){
            sql += " AND c.type = ?";

            return jdbcTemplate.query(sql, (rs, rowNum) -> new Records.ItemResponse(
                    rs.getLong("id"),
                    rs.getString("category_type").toUpperCase(),
                    rs.getString("record_date"),
                    new Categories.Info(rs.getLong("category_id"),
                            rs.getString("category_name")),
                    rs.getLong("amount"),
                    rs.getString("description"),
                    rs.getString("memo")
            ), userId, yearMonth, type);
        }
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Records.ItemResponse(
                rs.getLong("id"),
                rs.getString("category_type").toUpperCase(),
                rs.getString("record_date"),
                new Categories.Info(rs.getLong("category_id"), rs.getString("category_name")),
                rs.getLong("amount"),
                rs.getString("description"),
                rs.getString("memo")
        ), userId, yearMonth);

    }

    public Records.Summary findSummaryByUserMonth(Long userId, String yearMonth, String type) {
        String sql = "SELECT " +
                "COALESCE(SUM(CASE WHEN c.type = 'INCOME' THEN r.amount ELSE 0 END), 0) AS total_income, " +
                "COALESCE(SUM(CASE WHEN c.type = 'EXPENSE' THEN r.amount ELSE 0 END), 0) AS total_expense " +
                "FROM records r JOIN categories c ON r.category_id = c.id " +
                "WHERE r.user_id = ? AND DATE_FORMAT(r.record_date, '%Y-%m') = ?";

        if (type != null) {
            sql += " AND c.type = ?";
            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> toSummary(
                            rs.getLong("total_income"),
                            rs.getLong("total_expense")
                    ),
                    userId,
                    yearMonth,
                    type
            );
        }

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> toSummary(
                        rs.getLong("total_income"),
                        rs.getLong("total_expense")
                ),
                userId,
                yearMonth
        );
    }

    private Records.Summary toSummary(long totalIncome, long totalExpense) {
        return new Records.Summary(
                totalIncome,
                totalExpense,
                totalIncome - totalExpense
        );
    }
}

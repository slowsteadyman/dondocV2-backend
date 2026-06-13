package com.dondoc.repository;

import com.dondoc.dto.CategoryDto;
import com.dondoc.dto.RecordDto;
import com.dondoc.entity.Recorde;
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

    public void save(Recorde recorde) {
        String sql = "INSERT INTO records (user_id, category_id, amount, description, memo, record_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, recorde.getUserId(), recorde.getCategoryId(), recorde.getAmount(), recorde.getDescription(), recorde.getMemo(), recorde.getRecordDate(), recorde.getCreatedAt());
    }


    //  - records 테이블과 categories 테이블을 JOIN해서 카테고리 이름과 타입을 같이 가져와
    //  - type이 있으면 INCOME/EXPENSE 필터링, 없으면 전체 조회
    public List<RecordDto.ItemResponse> findByUserMonth(Long userId, String yearMonth, String type){
        String sql = "SELECT r.id, r.amount, r.description, r.memo, r.record_date, " +
                "c.id as category_id, c.name as category_name, c.type as category_type " +
                "FROM records r JOIN categories c ON r.category_id = c.id " +
                "WHERE r.user_id = ? AND DATE_FORMAT(r.record_date, '%Y-%m') = ?";

        if (type != null){
            sql += " AND c.type = ?";

            return jdbcTemplate.query(sql, (rs, rowNum) -> new RecordDto.ItemResponse(
                    rs.getLong("id"),
                    rs.getString("category_type").toUpperCase(),
                    rs.getString("record_date"),
                    new CategoryDto.Info(rs.getLong("category_id"),
                            rs.getString("category_name")),
                    rs.getLong("amount"),
                    rs.getString("description"),
                    rs.getString("memo")
            ), userId, yearMonth, type);
        }
        return jdbcTemplate.query(sql, (rs, rowNum) -> new RecordDto.ItemResponse(
                rs.getLong("id"),
                rs.getString("category_type").toUpperCase(),
                rs.getString("record_date"),
                new CategoryDto.Info(rs.getLong("category_id"), rs.getString("category_name")),
                rs.getLong("amount"),
                rs.getString("description"),
                rs.getString("memo")
        ), userId, yearMonth);

    }

    public RecordDto.Summary findSummaryByUserMonth(Long userId, String yearMonth, String type) {
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

    private RecordDto.Summary toSummary(long totalIncome, long totalExpense) {
        return new RecordDto.Summary(
                totalIncome,
                totalExpense,
                totalIncome - totalExpense
        );
    }
}

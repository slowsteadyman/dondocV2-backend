package com.dondoc.repository;

import com.dondoc.entity.Recorde;
import org.springframework.cglib.core.Local;
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

    public List<Recorde> findByDateRange(long userId, LocalDate start, LocalDate end) {
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
    }
}

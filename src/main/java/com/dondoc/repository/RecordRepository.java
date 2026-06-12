package com.dondoc.repository;

import com.dondoc.dto.Records;
import com.dondoc.entity.Recorde;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    // 사용하지 않을 수도 있다
    public void save(Recorde recorde) {
        String sql = "INSERT INTO records (user_id, category_id, amount, description, memo, record_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, recorde.getUserId(), recorde.getCategoryId(), recorde.getAmount(), recorde.getDescription(), recorde.getMemo(), recorde.getRecordDate(), recorde.getCreatedAt());
    }

    public Long save(Long userId, Records.RecordSaveRequest saveRequest) {
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
    }

    public Optional<Recorde> findById(Long id) {
        String sql = "SELECT * FROM records WHERE id = ?";
        return jdbcTemplate.query(sql,(rs, rowNum) -> new Recorde(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("category_id"),
                rs.getLong("amount"),
                rs.getString("description"),
                rs.getString("memo"),
                rs.getObject("record_date", LocalDate.class),
                rs.getObject("created_at", LocalDateTime.class)
        ), id).stream().findFirst();
    }
}

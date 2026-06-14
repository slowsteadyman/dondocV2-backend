package com.dondoc.repository;

import com.dondoc.dto.Records;
import com.dondoc.entity.Recorde;
import org.springframework.cglib.core.Local;
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

    public Optional<Recorde> findById(long id) {
        String sql = "SELECT * FROM records WHERE id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Recorde(
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

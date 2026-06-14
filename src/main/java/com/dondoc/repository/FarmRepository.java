package com.dondoc.repository;

import com.dondoc.entity.Farm;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class FarmRepository {

    private final JdbcTemplate jdbcTemplate;

    public FarmRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Farm> findAll(){
        String sql = "SELECT * FROM farms";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Farm(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getObject("created_at", LocalDateTime.class)
        ));
    }

    public Optional<Farm> findById(Long id) {
        String sql = "SELECT * FROM farms WHERE id = ?";
        List<Farm> farms = jdbcTemplate.query(sql, (rs, rowNum) -> new Farm(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getObject("created_at", LocalDateTime.class)
        ), id);

        return farms.stream().findFirst();
    }

    public void save(Farm farm) {
        String sql = "INSERT INTO farms (name, created_at) VALUES (?, ?)";
        jdbcTemplate.update(sql, farm.getName(), farm.getCreatedAt());
    }
}

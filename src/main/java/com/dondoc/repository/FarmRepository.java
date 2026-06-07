package com.dondoc.repository;

import com.dondoc.entity.Farm;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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

    //  - deleteByFarmIdAndUserId → farm_members에서 해당 유저 row 삭제
    //  - countByFarmId → 탈퇴 후 남은 멤버 수 확인 (0이면 농장도 삭제해야 함)
    public void save(Farm farm) {
        String sql = "INSERT INTO farms (name, created_at) VALUES (?, ?)";
        jdbcTemplate.update(sql, farm.getName(), farm.getCreatedAt());
    }

    public void deleteByFarmIdAndUserId(Long farmId, Long userId) {
        String sql = "DELETE FROM farm_members WHERE farm_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, farmId, userId);
    }

    public int countByFarmId(Long farmId) {
        String sql = "SELECT COUNT(*) FROM farm_members WHERE farm_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, farmId);
    }
}

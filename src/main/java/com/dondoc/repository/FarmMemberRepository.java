package com.dondoc.repository;

import com.dondoc.entity.FarmMember;
import com.dondoc.repository.projection.FarmMemberDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class FarmMemberRepository {

    private final JdbcTemplate jdbcTemplate;

    public FarmMemberRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public List<FarmMember> findAll(){
        String sql = "SELECT * FROM farm_members";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new FarmMember(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("farm_id"),
                rs.getObject("joined_at", LocalDateTime.class)
        ));
    }

    public boolean existsByFarmIdAndUserId(Long farmId, Long userId) {
        String sql = "SELECT COUNT(*) FROM farm_members WHERE farm_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, farmId, userId);

        return count != null && count > 0;
    }

    public List<FarmMemberDetail> findMemberDetailsByFarmId(Long farmId) {
        String sql = """
                SELECT
                    u.id AS user_id,
                    u.name AS name,
                    u.current_pig_level AS current_pig_level,
                    u.current_house_level AS current_house_level,
                    fm.joined_at AS joined_at
                FROM farm_members fm
                INNER JOIN users u ON fm.user_id = u.id
                WHERE fm.farm_id = ?
                ORDER BY fm.joined_at ASC, u.id ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new FarmMemberDetail(
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getInt("current_pig_level"),
                rs.getInt("current_house_level"),
                rs.getObject("joined_at", LocalDateTime.class)
        ), farmId);
    }

    public Optional<FarmMember> findByUserIdAndFarmId(long userId, long farmId) {
        String sql = "SELECT * FROM farm_members WHERE user_id = ? AND farm_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new FarmMember(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("farm_id"),
                rs.getObject("joined_at", LocalDateTime.class)
        ), userId, farmId).stream().findFirst();
    }

    public void save(FarmMember farmMember) {
        String sql = "INSERT INTO farm_members (user_id, farm_id, joined_at) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, farmMember.getUserId(), farmMember.getFarmId(), farmMember.getJoinedAt());
    }

    public Map<Long, Integer> findMemberCount() {
        Map<Long, Integer> result = new HashMap<>();
        String sql = "SELECT farm_id, count(*) member_count FROM farm_members GROUP BY farm_id";
        jdbcTemplate.query(sql, rs -> {
            result.put(rs.getLong("farm_id"), rs.getInt("member_count"));
        });
        return result;
    }

    public List<Long> findJoinedFarmIdsByUserId(Long userId) {
        String sql = "SELECT farm_id FROM farm_members WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }
}

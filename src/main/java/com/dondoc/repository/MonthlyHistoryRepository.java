package com.dondoc.repository;

import com.dondoc.entity.MonthlyHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.dondoc.repository.projection.MonthlySettlementHistory;

@Repository
public class MonthlyHistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public MonthlyHistoryRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public List<MonthlyHistory> findAll(){
        String sql = "SELECT * FROM monthly_history";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new MonthlyHistory(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getObject("target_month", LocalDate.class),
                rs.getFloat("avg_ratio"),
                rs.getInt("house_level")
        ));
    }

    public Optional<MonthlySettlementHistory> findSettlementHistory(Long userId, String month) {
        String sql = "SELECT avg_ratio, house_level FROM monthly_history WHERE user_id = ? AND target_month = ?";
        List<MonthlySettlementHistory> histories = jdbcTemplate.query(sql, (rs, rowNum) -> new MonthlySettlementHistory(
                rs.getFloat("avg_ratio"),
                rs.getInt("house_level")
        ), userId, month);

        return histories.stream().findFirst();
    }

    public void save(MonthlyHistory monthlyHistory) {
        String sql = "INSERT INTO monthly_history (user_id, target_month, avg_ratio, house_level) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, monthlyHistory.getUserId(), monthlyHistory.getTargetMonth(), monthlyHistory.getAvgRatio(), monthlyHistory.getHouseLevel());
    }
}

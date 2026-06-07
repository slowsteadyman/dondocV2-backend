package com.dondoc.repository;

import com.dondoc.entity.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}

    public List<Category> findAll(){
        String sql = "SELECT * FROM categories";
        return jdbcTemplate.query(sql, (rs,rowNum) -> new Category(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("icon"),
                rs.getString("type")
        ));
    }

    public void save(Category category) {
        String sql = "INSERT INTO categories (name, icon, type) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, category.getName(), category.getIcon(), category.getType());
    }

    public Category findById(long id) {
        String sql = "SELECT * FROM categories WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Category(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("icon"),
            rs.getString("type")
        ), id);
    }
}

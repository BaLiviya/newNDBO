package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryDao extends AbstractDao<Category> {

    public Category         get(int categoryId) {
        sql = "SELECT * FROM CATEGORY WHERE ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(categoryId), this::mapper);
    }

    public List<Category>   getAll() {
        sql = "SELECT * FROM CATEGORY ORDER BY IS_HIDE, NAMES";
        return getJdbcTemplate().query(sql, this::mapper);
    }

    @Override
    protected Category      mapper(ResultSet rs, int index) throws SQLException {
        Category category = new Category();
        category.setId      (rs.getInt    (1));
        category.setName    (rs.getString (2));
        category.setLanguage(rs.getBoolean(3));
        category.setHide    (rs.getBoolean(4));
        return category;
    }
}

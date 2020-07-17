package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.CategoryGroup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CategoryGroupDao extends AbstractDao<CategoryGroup> {

    public List<CategoryGroup>  getByGroupChatId(long groupChatId) {
        sql = "SELECT * FROM CATEGORY_GROUP WHERE GROUP_CHAT_ID = ?";
        return getJdbcTemplate().query(sql, setParam(groupChatId), this::mapper);
    }

    public boolean              isUseCategory(int categoryId, long groupChatId) {
        sql = "SELECT count(*) FROM CATEGORY_GROUP WHERE ID_CATEGORY = ? AND GROUP_CHAT_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(categoryId, groupChatId), Integer.class) > 0;
    }

    public List<CategoryGroup>  get(int categoryId, long groupChatId) {
        sql = "SELECT * FROM CATEGORY_GROUP WHERE ID_CATEGORY = ? AND GROUP_CHAT_ID = ?";
        return getJdbcTemplate().query(sql, setParam(categoryId, groupChatId), this::mapper);
    }

    public void                 deleteFromGroup(int categoryId, long groupChatId) {
        sql = "DELETE FROM CATEGORY_GROUP WHERE ID_CATEGORY = ? AND GROUP_CHAT_ID = ?";
        getJdbcTemplate().update(sql, setParam(categoryId, groupChatId));
    }

    public void                 addGroup(int categoryId, long groupChatId) {
        sql = "INSERT INTO CATEGORY_GROUP(ID_CATEGORY, GROUP_CHAT_ID) VALUES ( ?,? )";
        getJdbcTemplate().update(sql, setParam(categoryId, groupChatId));
    }

    @Override
    protected CategoryGroup     mapper(ResultSet rs, int index) throws SQLException {
        CategoryGroup categoryGroup = new CategoryGroup();
        categoryGroup.setId         (rs.getInt  (1));
        categoryGroup.setGroupChatId(rs.getLong (2));
        return categoryGroup;
    }
}

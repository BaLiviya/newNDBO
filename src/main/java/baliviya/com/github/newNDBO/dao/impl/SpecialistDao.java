package baliviya.com.github.newNDBO.dao.impl;

import baliviya.com.github.newNDBO.dao.AbstractDao;
import baliviya.com.github.newNDBO.entity.custom.Specialist;
import baliviya.com.github.newNDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SpecialistDao extends AbstractDao<Specialist> {

    public boolean          isSpecialist(long chatId) {
        sql = "SELECT count(*) FROM " + Const.TABLE_NAME + ".SPECIALIST WHERE CHAT_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId), Integer.class) > 0;
    }

    public void         insert(Specialist specialist) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".SPECIALIST (CHAT_ID, FULL_NAME) VALUES (?,?)";
        getJdbcTemplate().update(sql, specialist.getChatId(), specialist.getFullName());
    }

    public List<Long>   getAll() {
        sql = "SELECT CHAT_ID FROM " + Const.TABLE_NAME + ".SPECIALIST ORDER BY ID";
        return getJdbcTemplate().queryForList(sql, Long.class);
    }

    public void         delete(long chatId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".SPECIALIST WHERE CHAT_ID = ?";
        getJdbcTemplate().update(sql, chatId);
    }

    public List<Specialist>  getAllSpec() {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SPECIALIST";
        return getJdbcTemplate().query(sql,this::mapper);
    }

    @Override
    protected Specialist    mapper(ResultSet rs, int index) throws SQLException {
        Specialist specialist = new Specialist();
        specialist.setId(rs.getInt(1));
        specialist.setChatId(rs.getLong(2));
        specialist.setFullName(rs.getString(3));
        return specialist;
    }
}

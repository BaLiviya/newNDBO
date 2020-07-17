package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.Specialist;
import baliviya.com.github.NDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecialistDao extends AbstractDao<Specialist> {

    public boolean          isSpecialist(long chatId) {
        sql = "SELECT count(*) FROM " + Const.TABLE_NAME + ".SPECIALIST WHERE CHAT_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId), Integer.class) > 0;
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

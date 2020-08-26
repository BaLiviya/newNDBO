package baliviya.com.github.newBOSTANDDBO.dao.impl;

import baliviya.com.github.newBOSTANDDBO.dao.AbstractDao;
import baliviya.com.github.newBOSTANDDBO.entity.custom.HandlingName;
import baliviya.com.github.newBOSTANDDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HandlingNameDao extends AbstractDao<HandlingName> {

    public List<HandlingName>   getAllTraining() {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".TRAINING_NAME WHERE LANG_ID = ?" ;
        return getJdbcTemplate().query(sql, setParam(getLanguage().getId()) ,this::mapper);
    }

    public List<HandlingName>   getAllBusiness() {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".BUSINESS_NAME" ;
        return getJdbcTemplate().query(sql, this::mapper);
    }

    public HandlingName         get(int id) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".TRAINING_NAME WHERE LANG_ID = ? AND ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(getLanguage().getId(), id) ,this::mapper);
    }

    public HandlingName         getConsultation(int id) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".CONSULTATION_NAME WHERE LANG_ID = ? AND ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(getLanguage().getId(), id) ,this::mapper);
    }

    @Override
    protected HandlingName      mapper(ResultSet rs, int index) throws SQLException {
        HandlingName handlingName = new HandlingName();
        handlingName.setId(rs.getInt(1));
        handlingName.setName(rs.getString(2));
        return handlingName;
    }
}

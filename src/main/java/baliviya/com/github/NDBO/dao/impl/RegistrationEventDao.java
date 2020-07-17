package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.RegistrationEvent;
import baliviya.com.github.NDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationEventDao extends AbstractDao<RegistrationEvent> {

    public void                 insert(RegistrationEvent event) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".REGISTRATION_EVENT(CHAT_ID, EVENT_ID, REGISTRATION_DATE, IS_COME) VALUES ( ?,?,?,? )";
        getJdbcTemplate().update(sql, event.getChatId(), event.getEventId(), event.getRegistrationDate(), event.isCome());
    }

    public boolean              isJoinToEvent(long chatId, long eventId) {
        sql = "SELECT count(*) FROM " + Const.TABLE_NAME + ".REGISTRATION_EVENT WHERE CHAT_ID = ? AND EVENT_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId, eventId), Integer.class) > 0;
    }

    @Override
    protected RegistrationEvent mapper(ResultSet rs, int index) throws SQLException {
        RegistrationEvent event = new RegistrationEvent();
        event.setId(rs.getInt(1));
        event.setChatId(rs.getLong(2));
        event.setEventId(rs.getLong(3));
        event.setRegistrationDate(rs.getDate(4));
        event.setCome(rs.getBoolean(5));
        return event;
    }
}

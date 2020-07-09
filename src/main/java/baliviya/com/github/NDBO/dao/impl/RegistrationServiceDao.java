package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.RegistrationService;
import baliviya.com.github.NDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class RegistrationServiceDao extends AbstractDao<RegistrationService> {

    public void insert(RegistrationService service) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".REGISTRATION_SERVICE (CHAT_ID, IIN, SERVICE_TYPE_ID, SERVICE_ID, REGISTRATION_DATE, IS_COME) VALUES ( ?,?,?,?,?,? )";
        getJdbcTemplate().update(sql, setParam(service.getChatId(), service.getIin(), service.getServiceTypeId(), service.getServiceId(), service.getRegistrationDate(), service.isCome()));
    }

    @Override
    protected RegistrationService mapper(ResultSet rs, int index) throws SQLException {
        RegistrationService registrationService = new RegistrationService();
        registrationService.setId(rs.getInt(1));
        registrationService.setChatId(rs.getLong(2));
        registrationService.setIin(rs.getLong(3));
        registrationService.setServiceTypeId(rs.getInt(4));
        registrationService.setServiceId(rs.getInt(5));
        registrationService.setRegistrationDate(rs.getDate(6));
        registrationService.setCome(rs.getBoolean(7));
        return registrationService;
    }

}

package baliviya.com.github.newNDBO.dao.impl;

import baliviya.com.github.newNDBO.dao.AbstractDao;
import baliviya.com.github.newNDBO.entity.custom.RegistrationHandling;
import baliviya.com.github.newNDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class RegistrationHandlingDao extends AbstractDao<RegistrationHandling> {

    public void                         insertCourse(RegistrationHandling course) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".REGISTRATION_COURSES (CHAT_ID, IIN, COURSE_ID, REGISTRATION_DATE, IS_COME) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, setParam(course.getChatId(), course.getIin(), course.getIdHandling(), course.getRegistrationDate(), course.isCome()));
    }

    public List<RegistrationHandling>   getAllCoursesByTime(Date dateBegin, Date dateEnd) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_COURSES WHERE REGISTRATION_DATE BETWEEN to_date(?,'YYYY-MM-DD') AND to_date(?,'YYYY-MM-DD') ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(dateBegin, dateEnd), this::courseMapper);
    }

    public List<RegistrationHandling>   getAllCoursesTeacherByTime(Date dateBegin, Date dateEnd, int handlingId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_COURSES WHERE REGISTRATION_DATE BETWEEN ? AND ? AND COURSE_ID = ? ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(dateBegin, dateEnd, handlingId), this::courseMapper);
    }

    public RegistrationHandling         getCourseById(int id) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_COURSES WHERE ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(id), this::courseMapper);
    }

    public void                         updateCourse(RegistrationHandling registrationHandling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".REGISTRATION_COURSES SET IS_COME = ?, MEETING_DATE = ?, TIME = ?, COMMING = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, registrationHandling.isCome(), registrationHandling.getMeetingDate(), registrationHandling.getTime(), registrationHandling.getComing(), registrationHandling.getId());
    }


    public void                         insertTraining(RegistrationHandling training) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".REGISTRATION_TRAINING (CHAT_ID, IIN, TRAINING_ID, REGISTRATION_DATE, IS_COME) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, setParam(training.getChatId(), training.getIin(), training.getIdHandling(), training.getRegistrationDate(), training.isCome()));
    }

    public List<RegistrationHandling>   getAllTrainingsByTime(Date dateBegin, Date dateEnd) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_TRAINING WHERE REGISTRATION_DATE BETWEEN to_date(?,'YYYY-MM-DD') AND to_date(?,'YYYY-MM-DD') ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(dateBegin, dateEnd), this::mapper);
    }

    public List<RegistrationHandling>   getAllTrainingsTeacherByTime(Date dateBegin, Date dateEnd, int handlingId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_TRAINING WHERE REGISTRATION_DATE BETWEEN ? AND ? AND TRAINING_ID = ? ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(dateBegin, dateEnd, handlingId), this::mapper);
    }

    public RegistrationHandling         getTrainingById(int id) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_TRAINING WHERE ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(id), this::mapper);
    }

    public void                         updateTraining(RegistrationHandling registrationHandling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".REGISTRATION_TRAINING SET IS_COME = ?, MEETING_DATE = ?, TIME = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, registrationHandling.isCome(), registrationHandling.getMeetingDate(), registrationHandling.getTime(), registrationHandling.getId());
    }


    public void                         insertBusiness(RegistrationHandling business) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".REGISTRATION_BUSINESS (CHAT_ID, IIN, BUSINESS_ID, REGISTRATION_DATE, IS_COME) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, setParam(business.getChatId(), business.getIin(), business.getIdHandling(), business.getRegistrationDate(), business.isCome()));
    }

    public List<RegistrationHandling>   getAllBusinessByTime(Date dateBegin, Date dateEnd) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_BUSINESS WHERE REGISTRATION_DATE BETWEEN to_date(?,'YYYY-MM-DD') AND to_date(?,'YYYY-MM-DD') ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(dateBegin, dateEnd), this::mapper);
    }

    public int                          getCountBusinessById(Date dateBegin, Date dateEnd, int id, boolean isCome) {
        sql = "SELECT count(ID) FROM " + Const.TABLE_NAME + ".REGISTRATION_BUSINESS WHERE REGISTRATION_DATE BETWEEN to_date(?,'YYYY-MM-DD') AND to_date(?,'YYYY-MM-DD') AND BUSINESS_ID = ? AND IS_COME = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(dateBegin, dateEnd, id, isCome), Integer.class);
    }


    public void                         insertConsultation(RegistrationHandling consultation) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".REGISTRATION_CONSULTATION (CHAT_ID, IIN, BUSINESS_ID, REGISTRATION_DATE, IS_COME) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, setParam(consultation.getChatId(), consultation.getIin(), consultation.getIdHandling(), consultation.getRegistrationDate(), consultation.isCome()));
    }

    public List<RegistrationHandling>   getAllConsultationByTime(Date dateBegin, Date dateEnd) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_CONSULTATION WHERE REGISTRATION_DATE BETWEEN to_date(?,'YYYY-MM-DD') AND to_date(?,'YYYY-MM-DD') ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(dateBegin, dateEnd), this::mapper);
    }

    public int                          getCountConsultationById(Date dateBegin, Date dateEnd, int id) {
        sql = "SELECT count(ID) FROM " + Const.TABLE_NAME + ".REGISTRATION_CONSULTATION WHERE REGISTRATION_DATE BETWEEN to_date(?,'YYYY-MM-DD') AND to_date(?,'YYYY-MM-DD') AND BUSINESS_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(dateBegin, dateEnd, id), Integer.class);
    }

    public List<RegistrationHandling>   getAllConsultationsTeacherByTime(Date dateBegin, Date dateEnd, int handlingId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_CONSULTATION WHERE REGISTRATION_DATE BETWEEN ? AND ? AND BUSINESS_ID = ? ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(dateBegin, dateEnd, handlingId), this::mapper);
    }

    public RegistrationHandling         getConsultationById(int id) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_CONSULTATION WHERE ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(id), this::mapper);
    }

    public void                         updateConsultation(RegistrationHandling registrationHandling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".REGISTRATION_CONSULTATION SET IS_COME = ?, MEETING_DATE = ?, TIME = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, registrationHandling.isCome(), registrationHandling.getMeetingDate(), registrationHandling.getTime(), registrationHandling.getId());
    }


    public void                         insertService(RegistrationHandling service) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".REGISTRATION_SERVICE (CHAT_ID, IIN, SERVICE_ID, REGISTRATION_DATE, IS_COME) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, setParam(service.getChatId(), service.getIin(), service.getIdHandling(), service.getRegistrationDate(), service.isCome()));
    }

    public List<RegistrationHandling>   getAllServicesTeacherByTime(Date dateBegin, Date dateEnd, int handlingId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_SERVICE WHERE REGISTRATION_DATE BETWEEN ? AND ? AND SERVICE_ID = ? ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(dateBegin, dateEnd, handlingId), this::mapper);
    }

    public RegistrationHandling         getServiceById(int id) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REGISTRATION_SERVICE WHERE ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(id), this::mapper);
    }

    public void                         updateService(RegistrationHandling registrationHandling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".REGISTRATION_SERVICE SET IS_COME = ?, MEETING_DATE = ?, TIME = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, registrationHandling.isCome(), registrationHandling.getMeetingDate(), registrationHandling.getTime(), registrationHandling.getId());
    }

    @Override
    protected RegistrationHandling      mapper(ResultSet rs, int index) throws SQLException {
        RegistrationHandling registrationHandling = new RegistrationHandling();
        registrationHandling.setId(rs.getInt(1));
        registrationHandling.setChatId(rs.getLong(2));
        registrationHandling.setIin(rs.getLong(3));
        registrationHandling.setIdHandling(rs.getInt(4));
        registrationHandling.setRegistrationDate(rs.getDate(5));
        registrationHandling.setCome(rs.getBoolean(6));
        registrationHandling.setMeetingDate(rs.getDate(7));
        registrationHandling.setTime(rs.getString(8));
        return registrationHandling;
    }

    protected RegistrationHandling      courseMapper(ResultSet rs, int index) throws SQLException {
        RegistrationHandling registrationHandling = new RegistrationHandling();
        registrationHandling.setId(rs.getInt(1));
        registrationHandling.setChatId(rs.getLong(2));
        registrationHandling.setIin(rs.getLong(3));
        registrationHandling.setIdHandling(rs.getInt(4));
        registrationHandling.setRegistrationDate(rs.getDate(5));
        registrationHandling.setCome(rs.getBoolean(6));
        registrationHandling.setMeetingDate(rs.getDate(7));
        registrationHandling.setTime(rs.getString(8));
        registrationHandling.setComing(rs.getString(9));
        return registrationHandling;
    }
}

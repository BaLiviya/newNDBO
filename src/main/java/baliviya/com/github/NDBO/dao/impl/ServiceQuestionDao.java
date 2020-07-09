package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.ServiceQuestion;
import baliviya.com.github.NDBO.entity.enums.Language;
import baliviya.com.github.NDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ServiceQuestionDao extends AbstractDao<ServiceQuestion> {

    public List<ServiceQuestion> getAllActive(Language language, long chatId, int serviceId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SERVICE_QUESTION WHERE SERVICE_ID = ? AND IS_HIDE = FALSE AND HANDLING_TYPE = 'SERVICE' AND LANGUAGE_ID = ? AND ID NOT IN (SELECT SURVEY_ID FROM " + Const.TABLE_NAME + ".SERVICE_SURVEY_ANSWERS WHERE CHAT_ID = ?) ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(serviceId, language.getId(), chatId), this::mapper);
    }

    public List<ServiceQuestion> getAllActiveCourse(Language language, long chatId, int courseNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SERVICE_QUESTION WHERE SERVICE_ID = ? AND IS_HIDE = FALSE AND HANDLING_TYPE = 'COURSE' AND LANGUAGE_ID = ? AND ID NOT IN (SELECT SURVEY_ID FROM " + Const.TABLE_NAME + ".SERVICE_SURVEY_ANSWERS WHERE CHAT_ID = ? AND SERVICE_SURVEY_ANSWERS.HANDLING_TYPE = 'COURSE') ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(courseNameId, language.getId(), chatId), this::mapper);
    }

    public List<ServiceQuestion> getAllActiveTraining(Language language, long chatId, int trainingNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SERVICE_QUESTION WHERE SERVICE_ID = ? AND IS_HIDE = FALSE AND HANDLING_TYPE = 'TRAINING' AND LANGUAGE_ID = ? AND ID NOT IN (SELECT SURVEY_ID FROM " + Const.TABLE_NAME + ".SERVICE_SURVEY_ANSWERS WHERE CHAT_ID = ? AND SERVICE_SURVEY_ANSWERS.HANDLING_TYPE = 'TRAINING') ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(trainingNameId, language.getId(), chatId), this::mapper);
    }

    public List<ServiceQuestion> getAllActiveBusiness(Language language, long chatId, int businessNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SERVICE_QUESTION WHERE SERVICE_ID = ? AND IS_HIDE = FALSE AND HANDLING_TYPE = 'BUSINESS' AND LANGUAGE_ID = ? AND ID NOT IN (SELECT SURVEY_ID FROM " + Const.TABLE_NAME + ".SERVICE_SURVEY_ANSWERS WHERE CHAT_ID = ? AND SERVICE_SURVEY_ANSWERS.HANDLING_TYPE = 'BUSINESS') ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(businessNameId, language.getId(), chatId), this::mapper);
    }

    public List<ServiceQuestion> getAllActiveConsultation(Language language, long chatId, int consultationNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SERVICE_QUESTION WHERE SERVICE_ID = ? AND IS_HIDE = FALSE AND HANDLING_TYPE = 'CONSULTATION' AND LANGUAGE_ID = ? AND ID NOT IN (SELECT SURVEY_ID FROM " + Const.TABLE_NAME + ".SERVICE_SURVEY_ANSWERS WHERE CHAT_ID = ? AND SERVICE_SURVEY_ANSWERS.HANDLING_TYPE = 'CONSULTATION') ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(consultationNameId, language.getId(), chatId), this::mapper);
    }

    @Override
    protected ServiceQuestion mapper(ResultSet rs, int index) throws SQLException {
        ServiceQuestion serviceQuestion = new ServiceQuestion();
        serviceQuestion.setId(           rs.getInt(    1));
        serviceQuestion.setName(         rs.getString( 2));
        serviceQuestion.setQuestion(     rs.getString( 3));
        serviceQuestion.setLanguageId(   rs.getInt(    4));
        serviceQuestion.setServiceId(    rs.getInt(    5));
        serviceQuestion.setHide(         rs.getBoolean(6));
        serviceQuestion.setHandlingType( rs.getString( 7));
        return serviceQuestion;
    }
}

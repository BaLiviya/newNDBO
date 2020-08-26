package baliviya.com.github.newBOSTANDDBO.dao.impl;

import baliviya.com.github.newBOSTANDDBO.dao.AbstractDao;
import baliviya.com.github.newBOSTANDDBO.entity.custom.ServiceSurveyAnswer;
import baliviya.com.github.newBOSTANDDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceSurveyAnswerDao extends AbstractDao<ServiceSurveyAnswer> {

    public void                     insert(ServiceSurveyAnswer surveyAnswer) {
        int id = getNextId("SERVICE_SURVEY_ANSWERS");
        sql = "INSERT INTO " + Const.TABLE_NAME + ".SERVICE_SURVEY_ANSWERS(ID, SURVEY_ID, CHAT_ID, BUTTON, HANDLING_TYPE) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, setParam(id, surveyAnswer.getSurveyId(), surveyAnswer.getChatId(), surveyAnswer.getButton(), surveyAnswer.getHandlingType() ));
        surveyAnswer.setId(id);
    }

    @Override
    protected ServiceSurveyAnswer   mapper(ResultSet rs, int index) throws SQLException {
        ServiceSurveyAnswer serviceSurveyAnswer = new ServiceSurveyAnswer();
        serviceSurveyAnswer.setId(rs.getInt(1));
        serviceSurveyAnswer.setSurveyId(rs.getInt(2));
        serviceSurveyAnswer.setChatId(rs.getLong(3));
        serviceSurveyAnswer.setButton(rs.getString(4));
        serviceSurveyAnswer.setHandlingType(rs.getString(5));
        return serviceSurveyAnswer;
    }
}

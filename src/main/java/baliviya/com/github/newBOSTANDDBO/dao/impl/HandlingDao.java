package baliviya.com.github.newBOSTANDDBO.dao.impl;

import baliviya.com.github.newBOSTANDDBO.dao.AbstractDao;
import baliviya.com.github.newBOSTANDDBO.entity.custom.Handling;
import baliviya.com.github.newBOSTANDDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HandlingDao extends AbstractDao<Handling> {
/*
    public      void        insertCourse(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".COURSE (PHOTO, TEXT, COURSE_NAME_ID, FULL_NAME, COURSE_TEACHER_ID) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId());
    }*/

    public      int        insertCourse(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".COURSE (PHOTO, TEXT, COURSE_NAME_ID, FULL_NAME, COURSE_TEACHER_ID, LANG_ID) VALUES ( ?,?,?,?,?,? )";
        return (int) getDBUtils().updateForKeyId(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId(), handling.getLangId());
    }

    public      void         insertCourseKz(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".COURSE (ID, PHOTO, TEXT, COURSE_NAME_ID, FULL_NAME, COURSE_TEACHER_ID, LANG_ID) VALUES ( ?,?,?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getId(), handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId(), handling.getLangId());
    }

    public      void        updateCourse(Handling handling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".COURSE SET FULL_NAME = ?, TEXT = ?, PHOTO = ?, COURSE_TEACHER_ID = ? WHERE ID = ? AND LANG_ID = ?";
        getJdbcTemplate().update(sql, handling.getFullName(), handling.getText(), handling.getPhoto(), handling.getHandlingTeacherId(), handling.getId(), handling.getLangId());
    }

    public      void        updateTeacherCourse(Handling handling, int langId) {
        sql = "UPDATE " + Const.TABLE_NAME + ".COURSE SET COURSE_TEACHER_ID = ? WHERE ID = ? AND LANG_ID = ?";
        getJdbcTemplate().update(sql, handling.getHandlingTeacherId(), handling.getId(), langId);
    }

    public      void        deleteCourse(int handlingId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".COURSE WHERE ID = ?";
        getJdbcTemplate().update(sql, handlingId);
    }

    public List<Handling>   getAllCourse(int courseNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".COURSE WHERE COURSE_NAME_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(courseNameId, getLanguage().getId()), this::mapper);
    }

    public List<Handling>   getAllCourse(int courseNameId, int langId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".COURSE WHERE COURSE_NAME_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(courseNameId, langId), this::mapper);
    }

    public      boolean     isCourseTeacher(long chatId) {
        sql = "SELECT count(*) FROM " + Const.TABLE_NAME + ".COURSE WHERE COURSE_TEACHER_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId), Integer.class) > 0;
    }

    public      Handling    getCourseByChatId(long chatId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".COURSE WHERE COURSE_TEACHER_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId), this::mapper);
    }


/*    public      void        insertTraining(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".TRAINING (PHOTO, TEXT, TRAINING_NAME_ID, FULL_NAME, TRAINING_TEACHER_ID) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId());
    }*/

    public      int        insertTraining(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".TRAINING (PHOTO, TEXT, TRAINING_NAME_ID, FULL_NAME, TRAINING_TEACHER_ID, LANG_ID) VALUES ( ?,?,?,?,?,? )";
        return (int) getDBUtils().updateForKeyId(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId(), handling.getLangId());
    }

    public      void         insertTrainingKz(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".TRAINING (ID, PHOTO, TEXT, TRAINING_NAME_ID, FULL_NAME, TRAINING_TEACHER_ID, LANG_ID) VALUES ( ?,?,?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getId(), handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId(), handling.getLangId());
    }

    public      void        updateTraining(Handling handling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".TRAINING SET FULL_NAME = ?, TEXT = ?, PHOTO = ?, TRAINING_TEACHER_ID = ? WHERE ID = ? AND LANG_ID = ?";
        getJdbcTemplate().update(sql, handling.getFullName(), handling.getText(), handling.getPhoto(), handling.getHandlingTeacherId(), handling.getId(), handling.getLangId());
    }

    public      void        updateTeacherTraining(Handling handling, int langId) {
        sql = "UPDATE " + Const.TABLE_NAME + ".TRAINING SET TRAINING_TEACHER_ID = ? WHERE ID = ? AND LANG_ID = ?";
        getJdbcTemplate().update(sql, handling.getHandlingTeacherId(),handling.getId(), langId);
    }

    public      void        deleteTraining(int handlingId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".TRAINING WHERE ID = ?";
        getJdbcTemplate().update(sql, handlingId);
    }

    public List<Handling>   getAllTraining(int trainingNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".TRAINING WHERE TRAINING_NAME_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(trainingNameId, getLanguage().getId()), this::mapper);
    }

    public List<Handling>   getAllTraining(int trainingNameId, int langId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".TRAINING WHERE TRAINING_NAME_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(trainingNameId, langId), this::mapper);
    }

    public      boolean     isTrainingTeacher(long chatId) {
        sql = "SELECT count(*) FROM " + Const.TABLE_NAME + ".TRAINING WHERE TRAINING_TEACHER_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId), Integer.class) > 0;
    }

    public      Handling    getTrainingByChatId(long chatId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".TRAINING WHERE TRAINING_TEACHER_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId, getLanguage().getId()), this::mapper);
    }


    public      void        deleteBusiness(int handlingId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".BUSINESS WHERE ID = ?";
        getJdbcTemplate().update(sql, handlingId);
    }

    public List<Handling>   getAllBusiness(int businessNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".BUSINESS WHERE BUSINESS_NAME_ID = ?";
        return getJdbcTemplate().query(sql, setParam(businessNameId), this::mapper);
    }


    /*public      void        insertConsultation(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".CONSULTATION (PHOTO, TEXT, CONSULTATION_NAME_ID, FULL_NAME, CONSULTATION_TEACHER_ID) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId());
    }*/

    public      int        insertConsultation(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".CONSULTATION (PHOTO, TEXT, CONSULTATION_NAME_ID, FULL_NAME, CONSULTATION_TEACHER_ID, LANG_ID) VALUES ( ?,?,?,?,?,? )";
        return (int) getDBUtils().updateForKeyId(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId(), handling.getLangId());
    }

    public      void         insertConsultationKz(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".CONSULTATION (ID, PHOTO, TEXT, CONSULTATION_NAME_ID, FULL_NAME, CONSULTATION_TEACHER_ID, LANG_ID) VALUES ( ?,?,?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getId(), handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId(), handling.getLangId());
    }

    public      void        updateConsultation(Handling handling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".CONSULTATION SET FULL_NAME = ?, TEXT = ?, PHOTO = ?, CONSULTATION_TEACHER_ID = ? WHERE ID = ? AND LANG_ID = ?";
        getJdbcTemplate().update(sql, handling.getFullName(), handling.getText(), handling.getPhoto(), handling.getHandlingTeacherId(), handling.getId(), handling.getLangId());
    }

    public      void        updateTeacherConsultation(Handling handling, int langId) {
        sql = "UPDATE " + Const.TABLE_NAME + ".CONSULTATION SET CONSULTATION_TEACHER_ID = ? WHERE ID = ? AND LANG_ID = ?";
        getJdbcTemplate().update(sql, handling.getHandlingTeacherId(), handling.getId(), langId);
    }

    public      void        deleteConsultation(int handlingId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".CONSULTATION WHERE ID = ?";
        getJdbcTemplate().update(sql, handlingId);
    }

    public List<Handling>   getAllConsultation(int consultationNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".CONSULTATION WHERE CONSULTATION_NAME_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(consultationNameId, getLanguage().getId()), this::mapper);
    }

    public List<Handling>   getAllConsultation(int consultationNameId, int langId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".CONSULTATION WHERE CONSULTATION_NAME_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(consultationNameId, langId), this::mapper);
    }

    public      boolean     isConsultationTeacher(long chatId) {
        sql = "SELECT count(*) FROM " + Const.TABLE_NAME + ".CONSULTATION WHERE CONSULTATION_TEACHER_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId), Integer.class) > 0;
    }

    public      Handling    getConsultationByChatId(long chatId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".CONSULTATION WHERE CONSULTATION_TEACHER_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId, getLanguage().getId()), this::mapper);
    }


    public      int        insertService(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".SERVICE (PHOTO, TEXT, SERVICE_TYPE_ID, FULL_NAME, SERVICE_TEACHER_ID, LANG_ID) VALUES ( ?,?,?,?,?,? )";
        return (int) getDBUtils().updateForKeyId(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId(), handling.getLangId());
    }

    public      void         insertServiceKz(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".SERVICE (ID, PHOTO, TEXT, SERVICE_TYPE_ID, FULL_NAME, SERVICE_TEACHER_ID, LANG_ID) VALUES ( ?,?,?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getId(), handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId(), handling.getLangId());
    }

    public      void        updateService(Handling handling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".SERVICE SET FULL_NAME = ?, TEXT = ?, PHOTO = ?, SERVICE_TEACHER_ID = ? WHERE ID = ? AND LANG_ID = ?";
        getJdbcTemplate().update(sql, handling.getFullName(), handling.getText(), handling.getPhoto(), handling.getHandlingTeacherId() ,handling.getId(), handling.getLangId());
    }

    public      void        updateTeacherService(Handling handling, int langId) {
        sql = "UPDATE " + Const.TABLE_NAME + ".SERVICE SET SERVICE_TEACHER_ID = ? WHERE ID = ? AND LANG_ID = ?";
        getJdbcTemplate().update(sql, handling.getHandlingTeacherId(), handling.getId(), langId);
    }

    public      void        deleteService(int handlingId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".SERVICE WHERE ID = ?";
        getJdbcTemplate().update(sql, handlingId);
    }

    public List<Handling>   getAllService(int serviceTypeId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SERVICE WHERE SERVICE_TYPE_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(serviceTypeId, getLanguage().getId()), this::mapper);
    }

    public List<Handling>   getAllUserService(int serviceTypeId, int langId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SERVICE WHERE SERVICE_TYPE_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(serviceTypeId, langId), this::mapper);
    }

    public      boolean     isServiceTeacher(long chatId) {
        sql = "SELECT count(*) FROM " + Const.TABLE_NAME + ".SERVICE WHERE SERVICE_TEACHER_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId), Integer.class) > 0;
    }

    public      Handling    getServiceByChatId(long chatId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SERVICE WHERE SERVICE_TEACHER_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(chatId, getLanguage().getId()), this::mapper);
    }

    public List<Handling>   getAll() {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SERVICE";
        return getJdbcTemplate().query(sql, this::mapper);
    }

    @Override
    protected   Handling    mapper(ResultSet rs, int index) throws SQLException {
        Handling handling = new Handling();
        handling.setId(rs.getInt(1));
        handling.setPhoto(rs.getString(2));
        handling.setText(rs.getString(3));
        handling.setHandlingTypeId(rs.getInt(4));
        handling.setFullName(rs.getString(5));
        handling.setHandlingTeacherId(rs.getInt(6));
        handling.setLangId(rs.getInt(7));
        return handling;
    }
}

package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.Handling;
import baliviya.com.github.NDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HandlingDao extends AbstractDao<Handling> {

//    public Handling         getCourse(int courseNameId) {
//        sql = "SELECT * FROM " + Const.TABLE_NAME + ".COURSE WHERE COURSE_NAME_ID = ?";
//        return getJdbcTemplate().queryForObject(sql, setParam(courseNameId), this::mapper);
//    }

    public List<Handling>   getAllCourse(int courseNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".COURSE WHERE COURSE_NAME_ID = ?";
        return getJdbcTemplate().query(sql, setParam(courseNameId), this::mapper);
    }

    public List<Handling>   getAllTraining(int trainingNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".TRAINING WHERE TRAINING_NAME_ID = ?";
        return getJdbcTemplate().query(sql, setParam(trainingNameId), this::mapper);
    }

//    public Handling         getTraining(int trainingNameId) {
//        sql = "SELECT * FROM " + Const.TABLE_NAME + ".TRAINING WHERE TRAINING_NAME_ID = ?";
//        return getJdbcTemplate().queryForObject(sql, setParam(trainingNameId), this::mapper);
//    }

    public List<Handling>   getAllBusiness(int businessNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".BUSINESS WHERE BUSINESS_NAME_ID = ?";
        return getJdbcTemplate().query(sql, setParam(businessNameId), this::mapper);
    }

    public List<Handling>   getAllConsultation(int consultationNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".CONSULTATION WHERE CONSULTATION_NAME_ID = ?";
        return getJdbcTemplate().query(sql, setParam(consultationNameId), this::mapper);
    }

//    public Handling         getBusiness(int businessNameId) {
//        sql = "SELECT * FROM " + Const.TABLE_NAME + ".BUSINESS WHERE BUSINESS_NAME_ID = ?";
//        return getJdbcTemplate().queryForObject(sql, setParam(businessNameId), this::mapper);
//    }

    public List<Handling>   getAllService(int serviceTypeId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SERVICE WHERE SERVICE_TYPE_ID = ?";
        return getJdbcTemplate().query(sql, setParam(serviceTypeId), this::mapper);
    }

    public      void        insertService(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".SERVICE (PHOTO, TEXT, SERVICE_TYPE_ID, FULL_NAME, SERVICE_TEACHER_ID) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId());
    }

    public      void        insertCourse(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".COURSE (PHOTO, TEXT, COURSE_NAME_ID, FULL_NAME, COURSE_TEACHER_ID) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId());
    }

    public      void        insertTraining(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".TRAINING (PHOTO, TEXT, TRAINING_NAME_ID, FULL_NAME, TRAINING_TEACHER_ID) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId());
    }

//    public      void        insertBusiness(Handling handling) {
//        sql = "INSERT INTO " + Const.TABLE_NAME + ".BUSINESS (PHOTO, TEXT, BUSINESS_NAME_ID, FULL_NAME, BUSINESS_TEACHER_ID) VALUES ( ?,?,?,?,? )";
//        getJdbcTemplate().update(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId());
//    }

    public      void        insertConsultation(Handling handling) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".CONSULTATION (PHOTO, TEXT, CONSULTATION_NAME_ID, FULL_NAME, CONSULTATION_TEACHER_ID) VALUES ( ?,?,?,?,? )";
        getJdbcTemplate().update(sql, handling.getPhoto(), handling.getText(), handling.getHandlingTypeId(), handling.getFullName(), handling.getHandlingTeacherId());
    }

    public      void        updateService(Handling handling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".SERVICE SET FULL_NAME = ?, TEXT = ?, PHOTO = ?, SERVICE_TEACHER_ID = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, handling.getFullName(), handling.getText(), handling.getPhoto(), handling.getHandlingTeacherId() ,handling.getId());
    }

    public      void        updateCourse(Handling handling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".COURSE SET FULL_NAME = ?, TEXT = ?, PHOTO = ?, COURSE_TEACHER_ID = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, handling.getFullName(), handling.getText(), handling.getPhoto(), handling.getHandlingTeacherId(), handling.getId());
    }

    public      void        updateTraining(Handling handling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".TRAINING SET FULL_NAME = ?, TEXT = ?, PHOTO = ?, TRAINING_TEACHER_ID = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, handling.getFullName(), handling.getText(), handling.getPhoto(), handling.getHandlingTeacherId(),handling.getId());
    }

//    public      void        updateBusiness(Handling handling) {
//        sql = "UPDATE " + Const.TABLE_NAME + ".BUSINESS SET FULL_NAME = ?, TEXT = ?, PHOTO = ? WHERE ID = ?";
//        getJdbcTemplate().update(sql, handling.getFullName(), handling.getText(), handling.getPhoto(), handling.getId());
//    }

    public      void        updateConsultation(Handling handling) {
        sql = "UPDATE " + Const.TABLE_NAME + ".CONSULTATION SET FULL_NAME = ?, TEXT = ?, PHOTO = ?, CONSULTATION_TEACHER_ID = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, handling.getFullName(), handling.getText(), handling.getPhoto(), handling.getHandlingTeacherId(), handling.getId());
    }

    public      void        deleteService(int handlingId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".SERVICE WHERE ID = ?";
        getJdbcTemplate().update(sql, handlingId);
    }

    public      void        deleteCourse(int handlingId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".COURSE WHERE ID = ?";
        getJdbcTemplate().update(sql, handlingId);
    }

    public      void        deleteTraining(int handlingId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".TRAINING WHERE ID = ?";
        getJdbcTemplate().update(sql, handlingId);
    }

    public      void        deleteBusiness(int handlingId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".BUSINESS WHERE ID = ?";
        getJdbcTemplate().update(sql, handlingId);
    }

    public      void        deleteConsultation(int handlingId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".CONSULTATION WHERE ID = ?";
        getJdbcTemplate().update(sql, handlingId);
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
        return handling;
    }
}

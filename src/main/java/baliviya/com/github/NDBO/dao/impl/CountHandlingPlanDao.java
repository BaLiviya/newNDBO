package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.CountHandlingPlan;
import baliviya.com.github.NDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountHandlingPlanDao extends AbstractDao<CountHandlingPlan> {

    public CountHandlingPlan    getCount(int courseNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".PLAN_COUNT WHERE COURSE_NAME_ID = ? AND HANDLING_TYPE = 'COURSE'";
        return getJdbcTemplate().queryForObject(sql, setParam(courseNameId), this::mapper);
    }

    public CountHandlingPlan    getCountTraining(int trainingNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".PLAN_COUNT WHERE COURSE_NAME_ID = ? AND HANDLING_TYPE = 'TRAINING'";
        return getJdbcTemplate().queryForObject(sql, setParam(trainingNameId), this::mapper);
    }

    public CountHandlingPlan    getCountBusiness(int businessNameId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".PLAN_COUNT WHERE COURSE_NAME_ID = ? AND HANDLING_TYPE = 'BUSINESS'";
        return getJdbcTemplate().queryForObject(sql, setParam(businessNameId), this::mapper);
    }

    public CountHandlingPlan    getCountById(int id) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".PLAN_COUNT WHERE ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(id), this::mapper);
    }

    public void                 updateCount(int id, int countPeople) {
        sql = "UPDATE " + Const.TABLE_NAME + ".PLAN_COUNT SET COUNT_PEOPLE = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, setParam(countPeople, id));
    }

    @Override
    protected CountHandlingPlan mapper(ResultSet rs, int index) throws SQLException {
        CountHandlingPlan countHandlingPlan = new CountHandlingPlan();
        countHandlingPlan.setId(rs.getInt(1));
        countHandlingPlan.setHandlingType(rs.getString(2));
        countHandlingPlan.setCourseTypeId(rs.getInt(3));
        countHandlingPlan.setCourseNameId(rs.getInt(4));
        countHandlingPlan.setCountPeople(rs.getInt(5));
        return countHandlingPlan;
    }
}

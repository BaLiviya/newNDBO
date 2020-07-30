package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.CoursesName;
import baliviya.com.github.NDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CoursesNameDao extends AbstractDao<CoursesName> {

    public List<CoursesName>    getAll(int idCoursesType) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".COURSES_NAME WHERE COURSES_TYPE_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(idCoursesType, getLanguage().getId()), this::mapper);
    }

    public List<CoursesName>    getAll(int idCoursesType, int langId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".COURSES_NAME WHERE COURSES_TYPE_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(idCoursesType, langId), this::mapper);
    }

    public CoursesName          get(int courseId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".COURSES_NAME WHERE ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(courseId, getLanguage().getId()), this::mapper);
    }

    public List<CoursesName>    getAllConsultation(int idConsultationType) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".CONSULTATION_NAME WHERE CONSULTATION_TYPE_ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().query(sql, setParam(idConsultationType, getLanguage().getId()), this::mapper);
    }

    @Override
    protected CoursesName       mapper(ResultSet rs, int index) throws SQLException {
        CoursesName coursesName = new CoursesName();
        coursesName.setId(rs.getInt(1));
        coursesName.setName(rs.getString(2));
        coursesName.setIdCoursesType(rs.getInt(3));
        return coursesName;
    }
}

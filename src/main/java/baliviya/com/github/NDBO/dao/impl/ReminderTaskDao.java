package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.ReminderTask;
import baliviya.com.github.NDBO.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ReminderTaskDao extends AbstractDao<ReminderTask> {

    public List<ReminderTask> getAll() {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REMINDER_TASK";
        return getJdbcTemplate().query(sql, this::mapper);
    }

    public void insert(ReminderTask reminderTask) {
        sql = "INSERT INTO " + Const.TABLE_NAME + ".REMINDER_TASK(TEXT, DATE_BEGIN) VALUES ( ?,? )";
        getJdbcTemplate().update(sql, reminderTask.getText(), reminderTask.getDateBegin());
    }

    public void delete(int reminderTaskId) {
        sql = "DELETE FROM " + Const.TABLE_NAME + ".REMINDER_TASK WHERE ID = ?";
        getJdbcTemplate().update(sql, setParam(reminderTaskId));
    }

    public void update(ReminderTask reminderTask) {
        sql = "UPDATE " + Const.TABLE_NAME + ".REMINDER_TASK SET DATE_BEGIN = ?, TEXT = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, reminderTask.getDateBegin(),reminderTask.getText(), reminderTask.getId());
    }

    public ReminderTask getById(int reminderTaskId) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REMINDER_TASK WHERE ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(reminderTaskId), this::mapper);
    }

    public List<ReminderTask> getByTime(Date dateBegin, Date dateEnd) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".REMINDER_TASK WHERE DATE_BEGIN BETWEEN to_date(?,'YYYY-MM-DD') AND to_date(?,'YYYY-MM-DD HH:mm:SS')";
        return getJdbcTemplate().query(sql, setParam(dateBegin, dateEnd), this::mapper);
    }

    @Override
    protected ReminderTask mapper(ResultSet rs, int index) throws SQLException {
        ReminderTask reminderTask = new ReminderTask();
        reminderTask.setId(rs.getInt(1));
        reminderTask.setText(rs.getString(2));
        reminderTask.setDateBegin(rs.getDate(3));
        return reminderTask;
    }
}

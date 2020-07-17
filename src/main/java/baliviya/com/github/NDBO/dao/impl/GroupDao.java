package baliviya.com.github.NDBO.dao.impl;

import baliviya.com.github.NDBO.dao.AbstractDao;
import baliviya.com.github.NDBO.entity.custom.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDao extends AbstractDao<Group> {

    public Group    getGroupToId(int id) {
        sql = "SELECT * FROM GROUPS WHERE ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(id), this::mapper);
    }

    public void     update(Group group) {
        sql = "UPDATE GROUPS SET NAMES = ?, CHAT_ID = ?, USER_NAME = ?, IS_REGISTERED = ?, MESSAGE = ?, IS_CAN_PHOTO = ?, IS_CAN_VIDEO = ?, IS_CAN_AUDIO = ?, IS_CAN_FILE = ?, IS_CAN_LINK = ?, IS_CAN_STICKERS = ?, IS_CAN_WITHOUT_TAG = ? WHERE CHAT_ID = ?";
        getJdbcTemplate().update(sql, group.getNames(), group.getChatId(), group.getUserName(), group.isRegistered(), group.getMessage(), group.isCanPhoto(), group.isCanVideo(), group.isCanAudio(),
                group.isCanFile(), group.isCanLink(), group.isCanSticker(), group.isCanWithoutTag(), group.getChatId());
    }

    @Override
    protected Group mapper(ResultSet rs, int index) throws SQLException {
        Group group = new Group();
        group.setId             (rs.getInt    (1));
        group.setNames          (rs.getString (2));
        group.setChatId         (rs.getLong   (3));
        group.setUserName       (rs.getString (4));
        group.setRegistered     (rs.getBoolean(5));
        group.setMessage        (rs.getString (6));
        group.setCanPhoto       (rs.getBoolean(7));
        group.setCanVideo       (rs.getBoolean(8));
        group.setCanAudio       (rs.getBoolean(9));
        group.setCanFile        (rs.getBoolean(10));
        group.setCanLink        (rs.getBoolean(11));
        group.setCanSticker     (rs.getBoolean(12));
        group.setCanWithoutTag  (rs.getBoolean(13));
        return group;
    }
}

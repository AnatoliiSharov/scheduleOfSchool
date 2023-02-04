package ua.com.foxminded.asharov.universityschedule.dao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.asharov.universityschedule.model.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static ua.com.foxminded.asharov.universityschedule.model.Group.GROUP_ID;
import static ua.com.foxminded.asharov.universityschedule.model.Group.GROUP_NAME;

public class GroupDaoUtil implements DaoUtil<Group> {
    private static final Logger logger = LoggerFactory.getLogger(GroupDaoUtil.class);

    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();

        group.setId(rs.getLong(GROUP_ID));
        group.setName(rs.getString(GROUP_NAME));
        logger.debug("Group mapRow return course = {}", group);
        return group;
    }

    @Override
    public Map<String, Object> matchParam(Group group) {
        Map<String, Object> param = new HashMap<>();

        param.put(GROUP_ID, group.getId());
        param.put(GROUP_NAME, group.getName());
        logger.debug("Group matchParam return param = {}", param);
        return param;
    }

}

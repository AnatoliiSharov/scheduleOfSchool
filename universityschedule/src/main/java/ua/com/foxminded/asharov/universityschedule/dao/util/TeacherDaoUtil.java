package ua.com.foxminded.asharov.universityschedule.dao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.asharov.universityschedule.model.Teacher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static ua.com.foxminded.asharov.universityschedule.model.Teacher.*;

public class TeacherDaoUtil implements DaoUtil<Teacher> {
    private static final Logger logger = LoggerFactory.getLogger(TeacherDaoUtil.class);

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();

        teacher.setId(rs.getLong(TEACHER_ID));
        teacher.setFirstName(rs.getString(TEACHER_FIRST_NAME));
        teacher.setLastName(rs.getString(TEACHER_LAST_NAME));
        logger.debug("Teacher mapRow return course = {}", teacher);
        return teacher;
    }

    @Override
    public Map<String, Object> matchParam(Teacher entity) {
        Map<String, Object> param = new HashMap<>();

        param.put(TEACHER_ID, entity.getId());
        param.put(TEACHER_FIRST_NAME, entity.getFirstName());
        param.put(TEACHER_LAST_NAME, entity.getLastName());
        logger.debug("Teacher matchParam return param = {}", param);
        return param;
    }

}

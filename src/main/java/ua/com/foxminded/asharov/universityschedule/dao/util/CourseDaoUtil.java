package ua.com.foxminded.asharov.universityschedule.dao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.asharov.universityschedule.model.Course;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static ua.com.foxminded.asharov.universityschedule.model.Course.*;

public class CourseDaoUtil implements DaoUtil<Course> {
    private static final Logger logger = LoggerFactory.getLogger(CourseDaoUtil.class);

    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        Course course = new Course();
        course.setId(rs.getLong(COURSE_ID));
        course.setName(rs.getString(COURSE_NAME));
        course.setDescription(rs.getString(COURSE_DESCRIPTION));
        logger.debug("Course mapRow return course = {}", course);
        return course;
    }

    @Override
    public Map<String, Object> matchParam(Course entity) {
        Map<String, Object> param = new HashMap<>();

        param.put(COURSE_ID, entity.getId());
        param.put(COURSE_NAME, entity.getName());
        param.put(COURSE_DESCRIPTION, entity.getDescription());
        logger.debug("Course matchParam return param = {}", param);
        return param;
    }

}

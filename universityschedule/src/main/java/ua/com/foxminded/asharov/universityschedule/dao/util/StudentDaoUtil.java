package ua.com.foxminded.asharov.universityschedule.dao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.asharov.universityschedule.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static ua.com.foxminded.asharov.universityschedule.model.Student.*;

public class StudentDaoUtil implements DaoUtil<Student> {
    private static final Logger logger = LoggerFactory.getLogger(StudentDaoUtil.class);

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();

        student.setId(rs.getLong(STUDENT_ID));
        student.setGroupId((Long) rs.getObject(STUDENT_GROUP_ID));
        student.setFirstName(rs.getString(STUDENT_FIRST_NAME));
        student.setLastName(rs.getString(STUDENT_LAST_NAME));
        logger.debug("Student mapRow return course = {}", student);
        return student;
    }

    @Override
    public Map<String, Object> matchParam(Student student) {
        Map<String, Object> param = new HashMap<>();

        param.put(STUDENT_ID, student.getId());
        param.put(STUDENT_GROUP_ID, student.getGroupId());
        param.put(STUDENT_FIRST_NAME, student.getFirstName());
        param.put(STUDENT_LAST_NAME, student.getLastName());
        logger.debug("Sudent matchParam return param = {}", param);
        return param;
    }

}

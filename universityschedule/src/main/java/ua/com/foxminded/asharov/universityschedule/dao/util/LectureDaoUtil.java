package ua.com.foxminded.asharov.universityschedule.dao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.foxminded.asharov.universityschedule.model.Lecture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static ua.com.foxminded.asharov.universityschedule.model.Lecture.*;

public class LectureDaoUtil implements DaoUtil<Lecture> {
    private static final Logger logger = LoggerFactory.getLogger(LectureDaoUtil.class);

    @Override
    public Lecture mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lecture lecture = new Lecture();

        lecture.setId(rs.getLong(LECTURE_ID));
        lecture.setSerialNumberPerDay(rs.getInt(NUMBER_OF_LECTURE_PER_DAY));
        lecture.setDate(rs.getDate(DATE_OF_LECTURE).toLocalDate());
        lecture.setTeacherId(rs.getLong(TEACHER_ID));
        lecture.setRoomId(rs.getLong(ROOM_ID));
        lecture.setCourseId(rs.getLong(COURSE_ID));
        lecture.setGroupId(rs.getLong(GROUP_ID));
        logger.debug("Lecture mapRow return course = {}", lecture);
        return lecture;
    }

    @Override
    public Map<String, Object> matchParam(Lecture entity) {
        Map<String, Object> param = new HashMap<>();

        param.put(LECTURE_ID, entity.getId());
        param.put(NUMBER_OF_LECTURE_PER_DAY, entity.getSerialNumberPerDay());
        param.put(DATE_OF_LECTURE, entity.getDate());
        param.put(TEACHER_ID, entity.getTeacherId());
        param.put(ROOM_ID, entity.getRoomId());
        param.put(COURSE_ID, entity.getCourseId());
        param.put(GROUP_ID, entity.getGroupId());
        logger.debug("Lecture matchParam return param = {}", param);
        return param;
    }

}

package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.dao.AbstractCrudEntityDao;
import ua.com.foxminded.asharov.universityschedule.dao.LectureDao;
import ua.com.foxminded.asharov.universityschedule.dao.util.LectureDaoUtil;
import ua.com.foxminded.asharov.universityschedule.model.Lecture;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.asharov.universityschedule.model.Lecture.LECTURE_ID;
import static ua.com.foxminded.asharov.universityschedule.model.Lecture.LECTURE_TABLE_NAME;

@Repository
public class LectureDaoImpl extends AbstractCrudEntityDao<Lecture, Long> implements LectureDao {
    private static final Logger logger = LoggerFactory.getLogger(LectureDaoImpl.class);

    public static final String FIND_BY_ID = "SELECT * FROM lectures WHERE lecture_id = ?";
    public static final String SELECT_ALL = "SELECT * FROM lectures";
    public static final String DELETE_BY_ID = "DELETE FROM lectures WHERE lecture_id = ?";
    public static final String SELECT_SCHEDULE_GROUP = "SELECT * FROM lectures WHERE group_id = ? AND date_lecture >= ? AND date_lecture <= ?";
    public static final String SELECT_SCHEDULE_TEACHER = "SELECT * FROM lectures WHERE teacher_id = ? AND date_lecture >= ? AND date_lecture <= ?";
    public static final String SELECT_SCHEDULE_ROOM = "SELECT * FROM lectures WHERE room_id = ? AND date_lecture >= ? AND date_lecture <= ?";
    public static final String UPDATE_BY_ID = "UPDATE lectures SET day_lecture_number = :day_lecture_number, date_lecture = :date_lecture, room_id = :room_id, teacher_id = :teacher_id, course_id = :course_id, group_id = :group_id WHERE lecture_id = :lecture_id";

    JdbcTemplate template;
    NamedParameterJdbcTemplate namedParamTemplate;
    SimpleJdbcInsert simpleInsert;
    LectureDaoUtil lectureDaoUtil;

    public LectureDaoImpl(JdbcTemplate template, NamedParameterJdbcTemplate namedParamTemplate) {
        this.template = template;
        this.namedParamTemplate = namedParamTemplate;
        this.lectureDaoUtil = new LectureDaoUtil();
        this.simpleInsert = new SimpleJdbcInsert(template).withTableName(LECTURE_TABLE_NAME)
            .usingGeneratedKeyColumns(LECTURE_ID);
    }

    @Override
    public Optional<Lecture> findById(Long id) {
        logger.debug("findById for Lecture started with id = {}", id);

        if (id == null) {
            logger.debug("findById for Lecture return {} with id = {}", "EMPTY", id);
            return Optional.empty();
        }
        try {
            return Optional.of(template.queryForObject(FIND_BY_ID, lectureDaoUtil::mapRow, id));
        } catch (EmptyResultDataAccessException e) {
            logger.error("findById for Lecture has exception {} and return {} with id = {}", e, "EMPTY", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Lecture> findAll() {
        logger.debug("findAll for Lecture started");
        return template.query(SELECT_ALL, lectureDaoUtil::mapRow);
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("deleteById for Lecture started with id = {}", id);
        template.update(DELETE_BY_ID, id);
    }

    @Override
    public List<Lecture> findGroupSchedule(Long groupId, LocalDate startDate, LocalDate finishDate) {
        logger.debug("findGroupSchedule for Lecture started with groupId = {}, LocalDate = {}, finishDate = {}",
            groupId, startDate, finishDate);
        return template.query(SELECT_SCHEDULE_GROUP, lectureDaoUtil::mapRow, groupId, startDate, finishDate);
    }

    public List<Lecture> findTeacherSchedule(Long teacherId, LocalDate startDate, LocalDate finishDate) {
        logger.debug("findTeacherSchedule for Lecture started with teacherId = {}, LocalDate = {}, finishDate = {}",
            teacherId, startDate, finishDate);
        return template.query(SELECT_SCHEDULE_TEACHER, lectureDaoUtil::mapRow, teacherId, startDate, finishDate);
    }

    @Override
    public List<Lecture> findRoomSchedule(Long roomId, LocalDate startDate, LocalDate finishDate) {
        logger.debug("findRoomSchedule for Lecture started with roomId = {}, LocalDate = {}, finishDate = {}", roomId,
            startDate, finishDate);
        return template.query(SELECT_SCHEDULE_ROOM, lectureDaoUtil::mapRow, roomId, startDate, finishDate);
    }

    @Override
    protected Lecture update(Lecture entity) {
        logger.debug("update for Lecture started with Lecture = {}", entity);

        if (namedParamTemplate.update(UPDATE_BY_ID, lectureDaoUtil.matchParam(entity)) != 1) {
            logger.error("update for Lecture {} with Lecture = {}", "Unable to update Lecture ", entity);
            throw new EmptyResultDataAccessException("Unable to update student " + entity, 0);
        }
        logger.debug("update for Lecture success with result = {}", entity);
        return entity;
    }

    @Override
    protected Lecture create(Lecture entity) {
        logger.debug("create for Lecture started with Lecture = {}", entity);
        entity.setId(simpleInsert.executeAndReturnKey(lectureDaoUtil.matchParam(entity)).longValue());
        logger.debug("create for Lecture success with result = {}", entity);
        return entity;
    }

}

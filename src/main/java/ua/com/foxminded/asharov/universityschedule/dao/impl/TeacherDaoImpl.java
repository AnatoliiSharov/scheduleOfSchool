package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.dao.AbstractCrudEntityDao;
import ua.com.foxminded.asharov.universityschedule.dao.TeacherDao;
import ua.com.foxminded.asharov.universityschedule.dao.util.TeacherDaoUtil;
import ua.com.foxminded.asharov.universityschedule.model.Teacher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.asharov.universityschedule.model.Teacher.TEACHER_ID;
import static ua.com.foxminded.asharov.universityschedule.model.Teacher.TEACHER_TABLE_NAME;

@Repository
public class TeacherDaoImpl extends AbstractCrudEntityDao<Teacher, Long> implements TeacherDao {
    private static final Logger logger = LoggerFactory.getLogger(TeacherDaoImpl.class);

    static final String FIND_BY_ID = "SELECT * FROM teachers WHERE teacher_id = ?";
    static final String SELECT_ALL = "SELECT * FROM teachers";
    static final String DELETE_BY_ID = "DELETE FROM teachers WHERE teacher_id = ?";
    static final String SELECT_BY_FULLNAME = "SELECT * FROM teachers WHERE teacher_firstname LIKE ? AND teacher_lastname LIKE ?";
    static final String UPDATE_BY_ID = "UPDATE teachers SET teacher_firstname = :teacher_firstname, teacher_lastname = :teacher_lastname WHERE teacher_id = :teacher_id";
    static final String SELECT_BY_COURSE_ID = "SELECT t.teacher_id, t.teacher_firstname, t.teacher_lastname FROM teachers AS t JOIN teachers_courses AS tc ON t.teacher_id = tc.teacher_id JOIN courses AS c ON tc.course_id = c.course_id WHERE c.course_id = ?";
    static final String SELECT_FREE_BY_COURSE_BY_TIME = "SELECT * FROM teachers AS t WHERE t.teacher_id IN (SELECT tc.teacher_id FROM teachers_courses AS tc WHERE tc.course_id =?) AND t.teacher_id NOT IN(SELECT l.teacher_id FROM lectures AS l WHERE date_lecture = ? and day_lecture_number = ?)";
    static final String SELECT_FREE_BY_GROUP_BY_TIME = "SELECT * FROM teachers AS t WHERE t.teacher_id IN (SELECT tc.teacher_id FROM teachers_courses AS tc  WHERE tc.course_id IN(SELECT cg.course_id FROM courses_groups AS cg WHERE  cg.group_id = ?)) AND t.teacher_id NOT IN (SELECT l.teacher_id FROM lectures AS l WHERE l.date_lecture = ? AND l.day_lecture_number = ?)";
    static final String SELECT_FREE_BY_GROUP_BY_COURSE_BY_TIME = "SELECT * FROM teachers AS t WHERE t.teacher_id IN(SELECT tc.teacher_id FROM teachers_courses AS tc WHERE tc.course_id IN(SELECT cg.course_id FROM courses_groups AS cg WHERE cg.group_id = ?) AND tc.course_id = ?) AND t.teacher_id NOT IN(SELECT l.teacher_id FROM lectures AS l WHERE date_lecture = ? AND day_lecture_number = ?)";

    private final NamedParameterJdbcTemplate namedParameterTemplate;
    private final SimpleJdbcInsert simpleInsert;
    private final TeacherDaoUtil teacherDaoUtil;
    private final JdbcTemplate template;

    public TeacherDaoImpl(JdbcTemplate template, NamedParameterJdbcTemplate namedParameterTemplate) {
        this.template = template;
        this.namedParameterTemplate = namedParameterTemplate;
        this.teacherDaoUtil = new TeacherDaoUtil();
        this.simpleInsert = new SimpleJdbcInsert(template).withTableName(TEACHER_TABLE_NAME)
            .usingGeneratedKeyColumns(TEACHER_ID);
    }

    public List<Teacher> findByCoureId(Long courseId) {
        logger.debug("findByCoureId in TeacherDaoImpl started with courseId = {}", courseId);
        return template.query(SELECT_BY_COURSE_ID, teacherDaoUtil::mapRow, courseId);
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        logger.debug("findById in TeacherDaoImpl started with id = {}", id);

        if (id == null) {
            logger.debug("findById in TeacherDaoImpl return {} with id = {}", "EMPTY", id);
            return Optional.empty();
        }
        try {
            return Optional.of(template.queryForObject(FIND_BY_ID, teacherDaoUtil::mapRow, id));
        } catch (EmptyResultDataAccessException e) {
            logger.error("findById in TeacherDaoImpl has exception {} and return {} with id = {}", e, "EMPTY", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Teacher> findAll() {
        logger.debug("findAll in TeacherDaoImpl started");
        return template.query(SELECT_ALL, teacherDaoUtil::mapRow);
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("deleteById in TeacherDaoImpl started with id = {}", id);
        template.update(DELETE_BY_ID, id);
    }

    @Override
    public List<Teacher> findByFullName(String firstName, String lastName) {
        logger.debug("findByFullName in TeacherDaoImpl started with firstName = {}, lastName = {}", firstName,
            lastName);

        if (firstName == null) {
            firstName = "";
        }

        if (lastName == null) {
            lastName = "";
        }
        return template.query(SELECT_BY_FULLNAME, teacherDaoUtil::mapRow, "%" + firstName + "%", "%" + lastName + "%");
    }

    @Override
    protected Teacher update(Teacher entity) {
        logger.debug("update in TeacherDaoImpl started with Teacher = {}", entity);

        if (namedParameterTemplate.update(UPDATE_BY_ID, teacherDaoUtil.matchParam(entity)) != 1) {
            logger.error("update for Teacher {} with Teacher = {}", "Unable to update Teacher ", entity);
            throw new EmptyResultDataAccessException("Unable to update teacher " + entity, 0);
        }
        logger.debug("update in TeacherDaoImpl was success with result = {}", entity);
        return entity;
    }

    @Override
    protected Teacher create(Teacher entity) {
        logger.debug("create in TeacherDaoImpl started with Teacher = {}", entity);
        entity.setId(simpleInsert.executeAndReturnKey(teacherDaoUtil.matchParam(entity)).longValue());
        logger.debug("create in TeacherDaoImpl was success with result = {}", entity);
        return entity;
    }

    @Override
    public List<Teacher> findFreeOnesByTimeForCourse(Long courseId, LocalDate date, int numberPerDay) {
        logger.debug(
            "start findFreeOnesByTimeForCourse in TeacherDaoImpl started with Long courseId = {}, LocalDate date = {}, int numberPerDay = {}",
            courseId, date, numberPerDay);
        return template.query(SELECT_FREE_BY_COURSE_BY_TIME, teacherDaoUtil::mapRow, courseId, date, numberPerDay);
    }

    @Override
    public List<Teacher> findFreeByGroupByCourseByTime(Long groupId, Long courseId, LocalDate date, int numberPerDay) {
        logger.debug(
            "start findWhatCramGroupTime in TeacherDaoImpl started with Long groupId = {}, Long courseId = {}, LocalDate date = {}, int numberPerDay = {}",
            groupId, courseId, date, numberPerDay);
        return template.query(SELECT_FREE_BY_GROUP_BY_COURSE_BY_TIME, teacherDaoUtil::mapRow, groupId, courseId, date, numberPerDay);
    }

    @Override
    public List<Teacher> findFreeByTimeByGroupId(Long groupId, LocalDate currentDate, int serialNumberPerDay) {
        logger.debug(
            "start findFreeByTimeByGroupId in TeacherDaoImpl started with Long groupId = {}, LocalDate currentDate = {}, int serialNumberPerDay = {}",
            groupId, currentDate, serialNumberPerDay);
        return template.query(SELECT_FREE_BY_GROUP_BY_TIME, teacherDaoUtil::mapRow, groupId, currentDate, serialNumberPerDay);
    }

}

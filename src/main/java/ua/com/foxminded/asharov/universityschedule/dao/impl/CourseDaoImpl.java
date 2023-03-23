package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.dao.AbstractCrudEntityDao;
import ua.com.foxminded.asharov.universityschedule.dao.CourseDao;
import ua.com.foxminded.asharov.universityschedule.dao.util.CourseDaoUtil;
import ua.com.foxminded.asharov.universityschedule.model.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.asharov.universityschedule.model.Course.COURSE_ID;
import static ua.com.foxminded.asharov.universityschedule.model.Course.COURSE_TABLE_NAME;

@Repository
public class CourseDaoImpl extends AbstractCrudEntityDao<Course, Long> implements CourseDao {

    private static final Logger logger = LoggerFactory.getLogger(CourseDaoImpl.class);

    static final String FIND_BY_ID = "SELECT * FROM courses WHERE course_id = ?";
    static final String SELECT_ALL = "SELECT * FROM courses";
    static final String DELETE_BY_ID = "DELETE FROM courses WHERE course_id = ?";
    static final String FIND_BY_NAME = "SELECT * FROM courses WHERE course_name = ?";
    static final String UPDATE_BY_ID = "UPDATE courses SET course_name = :course_name, course_description = :course_description WHERE course_id = :course_id";
    static final String SELECT_BY_ACCREDITATED_TEACHER = "SELECT c.course_id, c.course_name, c.course_description FROM courses AS c JOIN teachers_courses AS tc ON tc.course_id = c.course_id WHERE tc.teacher_id = ?";
    static final String CREATE_TEACHER_LINK_COURSE = "INSERT INTO teachers_courses(teacher_id, course_id) VALUES (?, ?)";
    static final String DELETE_TEACHER_LINK_COURSE = "DELETE FROM teachers_courses WHERE teacher_id = ? AND course_id = ?";
    static final String SELECT_BY_GROUP_ID = "SELECT c.course_id, c.course_name, c.course_description FROM courses AS c JOIN courses_groups AS cg ON cg.course_id = c.course_id WHERE cg.group_id = ?";
    static final String CREATE_COURSE_LINK_GROUP = "INSERT INTO courses_groups(course_id, group_id) VALUES (?, ?)";
    static final String DELETE_COURSE_LINK_GROUP = "DELETE FROM courses_groups WHERE course_id = ? AND group_id = ?";
    static final String SELECT_BY_ACCREDITATED_TEACHER_FOR_GROUP = "SELECT * FROM courses AS c WHERE c.course_id IN (SELECT cg.course_id FROM courses_groups AS cg WHERE cg.group_id=?) AND c.course_id IN (SELECT tc.course_id FROM teachers_courses AS tc WHERE tc.teacher_id=?)";
    static final String SELECT_FREE_BY_GROUP_ID_WITH_FREE_TEACHERS_BY_TIME = "SELECT * FROM courses AS c WHERE c.course_id IN(SELECT cg.course_id FROM courses_groups AS cg WHERE cg.group_id = ?) AND c.course_id IN(SELECT tc.course_id FROM teachers_courses AS tc WHERE tc.teacher_id IN (SELECT t.teacher_id FROM teachers AS t WHERE t.teacher_id NOT IN (SELECT l.teacher_id FROM lectures AS l WHERE date_lecture = ? AND day_lecture_number = ?)))";
    static final String SELECT_FREE_BY_TEACHER_ID_WITH_FREE_GROUPS_BY_TIME = "SELECT * FROM courses AS c WHERE c.course_id IN(SELECT tc.course_id FROM teachers_courses AS tc WHERE tc.teacher_id = ?)AND c.course_id IN(SELECT cg.course_id FROM courses_groups AS cg WHERE cg.group_id IN(SELECT g.group_id FROM groups AS g WHERE g.group_id NOT IN(SELECT l.group_id FROM lectures AS l WHERE date_lecture = ? AND day_lecture_number = ?)))";

    NamedParameterJdbcTemplate namedParamTemplate;
    SimpleJdbcInsert simpleInsert;
    CourseDaoUtil courseDaoUtil;
    JdbcTemplate template;

    public CourseDaoImpl(JdbcTemplate template, NamedParameterJdbcTemplate namedParamTemplate) {
        this.template = template;
        this.namedParamTemplate = namedParamTemplate;
        this.simpleInsert = new SimpleJdbcInsert(template).withTableName(COURSE_TABLE_NAME)
            .usingGeneratedKeyColumns(COURSE_ID);
        this.courseDaoUtil = new CourseDaoUtil();
    }

    @Override
    public Optional<Course> findById(Long id) {
        logger.debug("findById in CourseDaoImpl started with id = {}", id);

        if (id == null) {
            logger.debug("findById in CourseDaoImpl return {} with id = {}", "empty", id);
            return Optional.empty();
        }
        try {
            return Optional.of(template.queryForObject(FIND_BY_ID, courseDaoUtil::mapRow, id));
        } catch (EmptyResultDataAccessException e) {
            logger.error("findById in CourseDaoImpl has exception {} and return {} with id = {}", e, "empty", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Course> findAll() {
        logger.debug("findAll in CourseDaoImpl started");
        return template.query(SELECT_ALL, courseDaoUtil::mapRow);
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("deleteById in CourseDaoImpl started with id = {}", id);
        template.update(DELETE_BY_ID, id);
    }

    @Override
    public Optional<Course> findByName(String name) {
        logger.debug("findByName in CourseDaoImpl started with name = {}", name);

        if (name == null) {
            logger.debug("findByName in CourseDaoImpl return {} with name = {}", "EMPTY", name);
            return Optional.empty();
        }
        try {
            logger.debug("findByName in CourseDaoImpl started with name = {}", name);
            return Optional.of(template.queryForObject(FIND_BY_NAME, courseDaoUtil::mapRow, name));
        } catch (EmptyResultDataAccessException e) {
            logger.error("findById in CourseDaoImpl has exception {} and return {} with name = {}", e, "EMPTY", name);
            return Optional.empty();
        }
    }

    @Override
    protected Course update(Course entity) {
        logger.debug("update in CourseDaoImpl started with Course = {}", entity);

        if (namedParamTemplate.update(UPDATE_BY_ID, courseDaoUtil.matchParam(entity)) != 1) {
            logger.error("update for Course {} with Course = {}", "Unable to update course ", entity);
            throw new EmptyResultDataAccessException("Unable to update course " + entity, 0);
        }
        logger.debug("update in CourseDaoImpl success with result = {}", entity);
        return entity;
    }

    @Override
    protected Course create(Course entity) {
        logger.debug("create in CourseDaoImpl  started with Course = {}", entity);
        entity.setId(simpleInsert.executeAndReturnKey(courseDaoUtil.matchParam(entity)).longValue());
        logger.debug("create in CourseDaoImpl  success with result = {}", entity);
        return entity;
    }

    @Override
    public List<Course> findByAccreditedTeacher(Long teacherId) {
        logger.debug("findByAccreditedTeacher in CourseDaoImpl teacherId = {}", teacherId);
        return template.query(SELECT_BY_ACCREDITATED_TEACHER, courseDaoUtil::mapRow, teacherId);
    }

    @Override
    public void linkCourseAndTeacher(Long teacherId, Long courseId) {
        logger.debug("linkCourseAndTeacher in CourseDaoImpl with courseId = {} and teacherId = {}", teacherId,
            courseId);
        template.update(CREATE_TEACHER_LINK_COURSE, teacherId, courseId);
    }

    @Override
    public void ripCourseAndTeacher(Long teacherId, Long courseId) {
        logger.debug("ripCourseAndTeacher in CourseDaoImpl with courseId = {} and teacherId = {}", teacherId, courseId);
        template.update(DELETE_TEACHER_LINK_COURSE, teacherId, courseId);
    }

    @Override
    public List<Course> findByGroupId(Long groupId) {
        logger.debug("findByGroupId in CourseDaoImpl with groupId = {}", groupId);
        return template.query(SELECT_BY_GROUP_ID, courseDaoUtil::mapRow, groupId);
    }

    @Override
    public void linkCourseAndGroup(Long courseId, Long groupId) {
        logger.debug("linkCourseAndGroup in CourseDaoImpl with courseId = {} and groupId = {}", courseId, groupId);
        template.update(CREATE_COURSE_LINK_GROUP, courseId, groupId);
    }

    @Override
    public void ripCourseAndGroup(Long courseId, Long groupId) {
        logger.debug("ripCourseAndGroup in CourseDaoImpl with courseId = {} and groupId = {}", courseId, groupId);
        template.update(DELETE_COURSE_LINK_GROUP, courseId, groupId);
    }

    @Override
    public List<Course> findByAccreditedTeacherForGroup(Long groupId, Long teacherId) {
        logger.debug("findByAccreditedTeacherForGroup in CourseDaoImpl with groupId = {}, teacherId = {}", groupId,
            teacherId);
        return template.query(SELECT_BY_ACCREDITATED_TEACHER_FOR_GROUP, courseDaoUtil::mapRow, groupId, teacherId);
    }

    @Override
    public List<Course> findFreeByGroupWithFreeTeachersByTime(Long groupId, LocalDate currentDate,
                                                              int serialNumberPerDay) {
        logger.debug(
            "findFreeByGroupWithFreeTeachersByTime in CourseDaoImpl with Long groupId = {}, LocalDate currentDate = {}, int serialNumberPerDay = {}",
            groupId, currentDate, serialNumberPerDay);
        return template.query(SELECT_FREE_BY_GROUP_ID_WITH_FREE_TEACHERS_BY_TIME, courseDaoUtil::mapRow, groupId,
            currentDate, serialNumberPerDay);
    }

    @Override
    public List<Course> findFreeByTeacherWithAllFreeGroupsByTime(Long techerId, LocalDate currentDate,
                                                                 int serialNumberPerDay) {
        logger.debug(
            "retrieveFreeByTeacherWithAllFreeGroupsByTime in CourseDaoImpl with Long techerId = {}, LocalDate currentDate = {}, int serialNumberPerDay = {}",
            techerId, currentDate, serialNumberPerDay);
        return template.query(SELECT_FREE_BY_TEACHER_ID_WITH_FREE_GROUPS_BY_TIME, courseDaoUtil::mapRow, techerId,
            currentDate, serialNumberPerDay);
    }

}

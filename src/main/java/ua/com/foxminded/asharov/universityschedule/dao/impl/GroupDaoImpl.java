package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.dao.AbstractCrudEntityDao;
import ua.com.foxminded.asharov.universityschedule.dao.GroupDao;
import ua.com.foxminded.asharov.universityschedule.dao.util.GroupDaoUtil;
import ua.com.foxminded.asharov.universityschedule.model.Group;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.asharov.universityschedule.model.Group.GROUP_ID;
import static ua.com.foxminded.asharov.universityschedule.model.Group.GROUP_TABLE_NAME;

@Repository
public class GroupDaoImpl extends AbstractCrudEntityDao<Group, Long> implements GroupDao {
    private static final Logger logger = LoggerFactory.getLogger(GroupDaoImpl.class);

    public static final String FIND_BY_ID = "SELECT * FROM groups WHERE group_id = ?";
    public static final String SELECT_ALL = "SELECT * FROM groups";
    public static final String DELETE_BY_ID = "DELETE FROM groups WHERE group_id = ?";
    public static final String SELECT_BY_NAME = "SELECT * FROM groups WHERE group_name = ?";
    public static final String UPDATE_BY_ID = "UPDATE groups SET group_name = :group_name WHERE group_id = :group_id";
    public static final String SELECT_BY_STUDENT_ID = "SELECT g.group_id, g.group_name FROM groups AS g JOIN students AS s ON g.group_id = s.group_id WHERE s.student_id = ?";
    public static final String SELECT_FREE_BY_TIME = "SELECT * FROM groups AS g WHERE g.group_id NOT IN (SELECT l.group_id FROM lectures AS l WHERE l.date_lecture = ? AND l.day_lecture_number = ?)";
    public static final String SELECT_FREE_BY_TIME_BY_TEACHER_ID = "SELECT * FROM groups AS g WHERE g.group_id IN (SELECT cg.group_id FROM courses_groups AS cg WHERE cg.course_id IN(SELECT tc.course_id FROM teachers_courses AS tc WHERE tc.teacher_id = ?)) AND g.group_id NOT IN (SELECT l.group_id FROM lectures AS l WHERE l.date_lecture = ? AND l.day_lecture_number = ?)";
    public static final String SELECT_FREE_BY_TEACHER_BY_COURSE_BY_TIME = "SELECT * FROM groups AS g WHERE g.group_id IN(SELECT cg.group_id FROM courses_groups AS cg WHERE cg.course_id IN(SELECT tc.course_id FROM teachers_courses AS tc WHERE tc.teacher_id = ?) AND cg.course_id = ?) AND g.group_id NOT IN(SELECT l.group_id FROM lectures AS l WHERE date_lecture = ? AND day_lecture_number = ?)";

    private final JdbcTemplate template;
    private final GroupDaoUtil daoUtil;
    private final SimpleJdbcInsert simpleInsert;
    private final NamedParameterJdbcTemplate namedParamTemplate;

    public GroupDaoImpl(JdbcTemplate template, NamedParameterJdbcTemplate namedParamTemplate) {
        this.template = template;
        this.simpleInsert = new SimpleJdbcInsert(template).withTableName(GROUP_TABLE_NAME)
            .usingGeneratedKeyColumns(GROUP_ID);
        this.namedParamTemplate = namedParamTemplate;
        this.daoUtil = new GroupDaoUtil();
    }

    @Override
    public List<Group> findAll() {
        logger.debug("findAll in GroupDaoImpl started");
        return template.query(SELECT_ALL, daoUtil::mapRow);
    }

    @Override
    public Optional<Group> findById(Long id) {
        logger.debug("findById in  GroupDaoImpl started with id = {}", id);

        if (id == null) {
            logger.debug("findById in GroupDaoImpl return EMPTY with id = {}", id);
            return Optional.empty();
        }
        try {
            return Optional.of(template.queryForObject(FIND_BY_ID, daoUtil::mapRow, id));
        } catch (EmptyResultDataAccessException e) {
            logger.error("findById in GroupDaoImpl has exeption {} and return EMPTY with id = {}", e, id);
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("deleteById in GroupDaoImpl started with id = {}", id);
        template.update(DELETE_BY_ID, id);
    }

    @Override
    public Optional<Group> findByName(String name) {
        logger.debug("findByName in GroupDaoImpl started with name = {}", name);

        if (name == null) {
            logger.debug("findByName in GroupDaoImpl return EMPTY with name = {}", name);
            return Optional.empty();
        }
        try {
            return Optional.of(template.queryForObject(SELECT_BY_NAME, daoUtil::mapRow, name));
        } catch (EmptyResultDataAccessException e) {
            logger.error("findByName in GroupDaoImpl has exception {} and return EMPTY with name = {}", e, name);
            return Optional.empty();
        }
    }

    @Override
    protected Group update(Group entity) {
        logger.debug("update in Group started with Group = {}", entity);

        if (namedParamTemplate.update(UPDATE_BY_ID, daoUtil.matchParam(entity)) != 1) {
            logger.error("update in Group {} with Group = {}", "Unable to update Group ", entity);
            throw new EmptyResultDataAccessException("Unable to update group " + entity, 0);
        }
        logger.debug("update in GroupDaoImpl success with result = {}", entity);
        return entity;
    }

    @Override
    protected Group create(Group entity) {
        logger.debug("create in GroupDaoImpl started with Group = {}", entity);
        entity.setId(simpleInsert.executeAndReturnKey(daoUtil.matchParam(entity)).longValue());
        logger.debug("create in GroupDaoImpl success with result = {}", entity);
        return entity;
    }

    @Override
    public Optional<Group> findByStudentId(Long studentId) {
        logger.debug("findByStudentId for GroupDaoImpl started with studentId = {}", studentId);

        if (studentId == 0) {
            logger.debug("findByStudentId for GroupDaoImpl return EMPTY with studentId = {}", studentId);
            return Optional.empty();
        }
        try {
            return Optional.of(template.queryForObject(SELECT_BY_STUDENT_ID, daoUtil::mapRow, studentId));
        } catch (EmptyResultDataAccessException e) {
            logger.error("findByStudentId for GroupDaoImpl has exeption {} and return EMPTY with studentId = {}", e,
                studentId);
            return Optional.empty();
        }
    }

    @Override
    public List<Group> findFreeByTime(LocalDate currentDate, int serialNumberPerDay) {
        logger.debug(
            "findSubstitutesByTime for GroupDaoImpl started with LocalDate currentDate={}, int serialNumberPerDay={}",
            currentDate, serialNumberPerDay);
        return template.query(SELECT_FREE_BY_TIME, daoUtil::mapRow, currentDate, serialNumberPerDay);
    }

    @Override
    public List<Group> findFreeByTimeByTeacherId(Long teacherId, LocalDate currentDate, int serialNumberPerDay) {
        logger.debug(
            "findFreeLinkedCourseByTimeByTeacherId for GroupDaoImpl started with Long teacherId={}, LocalDate currentDate={}, int serialNumberPerDay={}",
            teacherId, currentDate, serialNumberPerDay);
        return template.query(SELECT_FREE_BY_TIME_BY_TEACHER_ID, daoUtil::mapRow, teacherId, currentDate,
            serialNumberPerDay);
    }

    @Override
    public List<Group> findFreeFreeByTeacherByCourseByTime(Long teacherId, Long courseId, LocalDate currentDate, int serialNumberPerDay) {
        logger.debug(
            "findFreeByTimeByCourse for GroupDaoImpl started with Long teacherId={}, Long courseId={}, LocalDate currentDate={}, int serialNumberPerDay={}",
            teacherId, courseId, currentDate, serialNumberPerDay);
        return template.query(SELECT_FREE_BY_TEACHER_BY_COURSE_BY_TIME, daoUtil::mapRow, teacherId, courseId, currentDate,
            serialNumberPerDay);
    }

}

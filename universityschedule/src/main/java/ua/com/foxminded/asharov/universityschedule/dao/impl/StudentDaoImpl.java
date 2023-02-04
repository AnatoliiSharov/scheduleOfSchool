package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.dao.AbstractCrudEntityDao;
import ua.com.foxminded.asharov.universityschedule.dao.StudentDao;
import ua.com.foxminded.asharov.universityschedule.dao.util.StudentDaoUtil;
import ua.com.foxminded.asharov.universityschedule.model.Student;

import java.util.List;
import java.util.Optional;

import static ua.com.foxminded.asharov.universityschedule.model.Student.STUDENT_ID;
import static ua.com.foxminded.asharov.universityschedule.model.Student.STUDENT_TABLE_NAME;

@Repository
public class StudentDaoImpl extends AbstractCrudEntityDao<Student, Long> implements StudentDao {
    private static final Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);

    public static final String FIND_BY_ID = "SELECT * FROM students WHERE student_id = ?";
    public static final String SELECT_ALL = "SELECT * FROM students";
    public static final String DELETE_BY_ID = "DELETE FROM students WHERE student_id = ?";
    public static final String SELECT_BY_FULL_NAME = "SELECT * FROM students WHERE student_firstname = ? AND student_lastname = ?";
    public static final String SELECT_BY_GROUP_ID = "SELECT * FROM students WHERE group_id = ?";
    public static final String INSERT_FULL_NAME = "INSERT INTO students(student_firstname, student_lastname) VALUES ( ?, ?)";
    public static final String UPDATE_BY_ID = "UPDATE students SET group_id = :group_id, student_firstname = :student_firstname, student_lastname = :student_lastname WHERE student_id = :student_id";

    private final NamedParameterJdbcTemplate namedParamTemplate;
    private final SimpleJdbcInsert simpleInsert;
    private final JdbcTemplate template;
    private final StudentDaoUtil studentDaoUtil;

    public StudentDaoImpl(JdbcTemplate template, NamedParameterJdbcTemplate namedParamTemplate) {
        this.template = template;
        this.namedParamTemplate = namedParamTemplate;
        this.simpleInsert = new SimpleJdbcInsert(template).withTableName(STUDENT_TABLE_NAME)
            .usingGeneratedKeyColumns(STUDENT_ID);
        this.studentDaoUtil = new StudentDaoUtil();
    }

    @Override
    public Optional<Student> findById(Long id) {
        logger.debug("findById for Student started with id = {}", id);

        if (id == null) {
            logger.debug("findById for Student return {} with id = {}", "EMPTY", id);
            return Optional.empty();
        }
        try {
            return Optional.of(template.queryForObject(FIND_BY_ID, studentDaoUtil::mapRow, id));
        } catch (EmptyResultDataAccessException e) {
            logger.error("findById for Student has exception {} and return {} with id = {}", e, "EMPTY", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Student> findAll() {
        logger.debug("findAll for Student started");
        return template.query(SELECT_ALL, studentDaoUtil::mapRow);
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("deleteById for Student started with id = {}", id);
        template.update(DELETE_BY_ID, id);
    }

    @Override
    public List<Student> findByFullName(String firstName, String lastName) {
        logger.debug("findByFullName for Student started with firstName = {}, lastName = {}", firstName, lastName);
        return template.query(SELECT_BY_FULL_NAME, studentDaoUtil::mapRow, firstName, lastName);
    }

    @Override
    public List<Student> findListByGroupId(Long groupId) {
        logger.debug("findListByGroupId for Student started with groupId = {}", groupId);
        return template.query(SELECT_BY_GROUP_ID, studentDaoUtil::mapRow, groupId);
    }

    @Override
    protected Student update(Student entity) {
        logger.debug("update for Student started with Student = {}", entity);

        if (namedParamTemplate.update(UPDATE_BY_ID, studentDaoUtil.matchParam(entity)) != 1) {
            logger.error("update for Student {} with Student = {}", "Unable to update Student ", entity);
            throw new EmptyResultDataAccessException("Unable to update student " + entity, 0);
        }
        logger.debug("update for Student was success with result = {}", entity);
        return entity;
    }

    @Override
    protected Student create(Student entity) {
        logger.debug("create for Student started with Student = {}", entity);
        entity.setId(simpleInsert.executeAndReturnKey(studentDaoUtil.matchParam(entity)).longValue());
        logger.debug("create for Student was success with result = {}", entity);
        return entity;
    }

}

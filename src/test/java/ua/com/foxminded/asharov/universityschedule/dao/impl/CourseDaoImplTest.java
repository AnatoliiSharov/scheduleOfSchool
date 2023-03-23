package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.asharov.universityschedule.dao.CourseDao;
import ua.com.foxminded.asharov.universityschedule.model.Course;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseDaoImplTest {

    @Autowired
    JdbcTemplate template;

    @Autowired
    NamedParameterJdbcTemplate namedParamTempate;
    SimpleJdbcInsert simpleInsert;
    CourseDao courseDao;

    @BeforeEach
    void setUp() throws Exception {
        courseDao = new CourseDaoImpl(template, namedParamTempate);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findById_shouldFind_whenRowExist() {
        Optional<Course> actual = courseDao.findById(10003L);

        assertTrue(actual.isPresent());
        assertEquals(new Course(10003L, "Course3", "Description3"), actual.get());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findById_shouldNotFind_whenRowAbsent() {
        Optional<Course> actual = courseDao.findById(999L);

        assertTrue(actual.isEmpty());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findAll() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
            new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3"),
            new Course(10004L, "Course4", "Description4"), new Course(10005L, "Course5", "Description5"));

        assertEquals(expected, courseDao.findAll());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void deleteById() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
            new Course(10002L, "Course2", "Description2"), new Course(10004L, "Course4", "Description4"),
            new Course(10005L, "Course5", "Description5"));

        courseDao.deleteById(10003L);
        assertEquals(expected, courseDao.findAll());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findByName_shouldFind_whenRoeExist() {
        Optional<Course> actual = courseDao.findByName("Course3");

        assertTrue(actual.isPresent());
        assertEquals(new Course(10003L, "Course3", "Description3"), actual.get());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void update() {
        Course expected = new Course(10003L, "ChangedName", "ChangedDescription");

        courseDao.save(expected);
        assertEquals(expected, courseDao.findById(expected.getId()).get());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void create() {
        Course expected = courseDao.save(new Course("NewName", "NewDescription"));

        assertEquals(expected, courseDao.findByName("NewName").get());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindByAccreditedTeacher() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
            new Course(10002L, "Course2", "Description2"));

        assertEquals(expected, courseDao.findByAccreditedTeacher(10001L));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testLinkCourseAndTeacher() {
        assertFalse(courseDao.findByAccreditedTeacher(10001L).contains(new Course(10005L, "Course5", "Description5")));
        courseDao.linkCourseAndTeacher(10001L, 10005L);
        assertTrue(courseDao.findByAccreditedTeacher(10001L).contains(new Course(10005L, "Course5", "Description5")));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testRipCourseAndTeacher() {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        assertTrue(courseDao.findByAccreditedTeacher(teacherId).contains(new Course(10001L, "Course1", "Description1")));
        courseDao.ripCourseAndTeacher(teacherId, courseId);
        assertFalse(courseDao.findByAccreditedTeacher(teacherId).contains(new Course(10001L, "Course1", "Description1")));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindByGroupId() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
            new Course(10005L, "Course5", "Description5"));

        assertEquals(expected, courseDao.findByGroupId(10001L));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testLinkCourseAndGroup() {
        assertFalse(courseDao.findByGroupId(10001L).contains(new Course(10004L, "Course4", "Description4")));
        courseDao.linkCourseAndGroup(10004L, 10001L);
        assertTrue(courseDao.findByGroupId(10001L).contains(new Course(10004L, "Course4", "Description4")));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testRipCourseAndGroup() {
        assertTrue(courseDao.findByGroupId(10001L).contains(new Course(10001L, "Course1", "Description1")));
        courseDao.ripCourseAndGroup(10001L, 10001L);
        assertFalse(courseDao.findByGroupId(10001L).contains(new Course(10001L, "Course1", "Description1")));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindByAccreditedTeacherForGroup() {

        List<Course> expected = Arrays.asList(new Course(10004L, "Course4", "Description4"));

        assertEquals(expected, courseDao.findByAccreditedTeacherForGroup(10002L, 10003L));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindFreeByGroupWithFreeTeachersByTime() {
        List<Course> expected = Arrays.asList(new Course(10003L, "Course3", "Description3"), new Course(10004L, "Course4", "Description4"));

        assertEquals(expected, courseDao.findFreeByGroupWithFreeTeachersByTime(10003L, LocalDate.of(01, 01, 0001), 2));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindFreeByTeacherWithAllFreeGroupsByTime() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3"), new Course(10004L, "Course4", "Description4"));

        assertEquals(expected, courseDao.findFreeByTeacherWithAllFreeGroupsByTime(10002L, LocalDate.of(01, 01, 0001), 2));
    }

}

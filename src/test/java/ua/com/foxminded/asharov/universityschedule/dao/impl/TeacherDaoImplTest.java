package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.asharov.universityschedule.dao.TeacherDao;
import ua.com.foxminded.asharov.universityschedule.model.Teacher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeacherDaoImplTest {

    @Autowired
    JdbcTemplate template;

    @Autowired
    NamedParameterJdbcTemplate namedParamTemplate;
    SimpleJdbcInsert simpleInsert;
    TeacherDao teacherDao;

    @BeforeEach
    void setUp() throws Exception {
        teacherDao = new TeacherDaoImpl(template, namedParamTemplate);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindByCoureId() {
        List<Teacher> expected = Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
            new Teacher(10002L, "teacherFirstName2", "teacherLastName2"));

        assertEquals(expected, teacherDao.findByCoureId(10001L));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findById_shouldFind_whenRowExist() {
        Optional<Teacher> actual = teacherDao.findById(10001L);

        assertTrue(actual.isPresent());
        assertEquals(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"), actual.get());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findById_shouldNotFind_whenRowAbsent() {
        Optional<Teacher> actual = teacherDao.findById(99999L);

        assertTrue(actual.isEmpty());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findAll() {
        List<Teacher> expected = Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
            new Teacher(10002L, "teacherFirstName2", "teacherLastName2"),
            new Teacher(10003L, "teacherFirstName3", "teacherLastName3"));

        assertEquals(expected, teacherDao.findAll());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void deleteById() {
        List<Teacher> expected = Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
            new Teacher(10003L, "teacherFirstName3", "teacherLastName3"));

        teacherDao.deleteById(10002L);
        assertEquals(expected, teacherDao.findAll());
    }

    @ParameterizedTest
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    @CsvSource(value = {" 1, 1", " 1, ", " , 1", " 1, Last", " First, 1", "teacherFirstName1, teacherLastName1"})
    void findByFullName_shouldFind_whenParametersAreTrueForUniqueItem(String firstName, String lastName) {
        List<Teacher> actual = teacherDao.findByFullName(firstName, lastName);
        List<Teacher> expected = Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"));

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    @CsvSource(value = {" 2, 3"})
    void findByFullName_shouldFind_whenParametersAreWrongForAllItems(String firstName, String lastName) {
        List<Teacher> actual = teacherDao.findByFullName(firstName, lastName);
        List<Teacher> expected = new ArrayList<Teacher>();

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    @CsvSource(value = {" , ", " teacher, teacher"})
    void findByFullName_shouldFind_whenParametersAreTrueForAllItems(String firstName, String lastName) {
        List<Teacher> actual = teacherDao.findByFullName(firstName, lastName);
        List<Teacher> expected = teacherDao.findAll();

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findByFullName_shouldFind_whenUseCommonPartName() {
        List<Teacher> actual = teacherDao.findByFullName("teacherFirstName1", "teacherLastName1");
        List<Teacher> expected = Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"));

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    @MethodSource("provideTeacherForTestSave")
    void testSave(Teacher testTeacher, Long expectedId) {
        assertEquals(expectedId, teacherDao.save(testTeacher).getId());
    }

    private static Stream<Arguments> provideTeacherForTestSave() {
        return Stream.of(Arguments.of(new Teacher(10001L, "changedTeacherFirstName", "changedTeacherLastName"), 10001L),
            Arguments.of(new Teacher("newTeacherFirstName", "newTeacherLastName"), 1L));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindFreeOnesByTimeForCourse() {
        List<Teacher> expected = Arrays.asList(new Teacher(10002L, "teacherFirstName2", "teacherLastName2"));
        assertEquals(expected, teacherDao.findFreeOnesByTimeForCourse(10004L, LocalDate.of(0001, 01, 01), 1));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFreeByGroupByCourseByTime() {
        List<Teacher> expected = Arrays.asList(new Teacher(10003L, "teacherFirstName3", "teacherLastName3"));
        assertEquals(expected, teacherDao.findFreeByGroupByCourseByTime(10003L, 10003L, LocalDate.of(0001, 01, 01), 2));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFreeByTimeByGroupId() {
        List<Teacher> expected = Arrays.asList(new Teacher(10002L, "teacherFirstName2", "teacherLastName2"));
        assertEquals(expected, teacherDao.findFreeByTimeByGroupId(10001L, LocalDate.of(0001, 01, 01), 1));
    }

}

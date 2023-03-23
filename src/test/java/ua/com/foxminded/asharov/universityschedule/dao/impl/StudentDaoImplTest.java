package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.asharov.universityschedule.dao.StudentDao;
import ua.com.foxminded.asharov.universityschedule.model.Student;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentDaoImplTest {

    @Autowired
    NamedParameterJdbcTemplate namedParamTemplate;

    @Autowired
    JdbcTemplate template;
    StudentDao studentDao;

    @BeforeEach
    void SetUp() {
        studentDao = new StudentDaoImpl(template, namedParamTemplate);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findById_shouldFind_whenEntityExist() {
        Optional<Student> actual = studentDao.findById(10007L);

        assertTrue(studentDao.findById(10007L).isPresent());
        assertEquals(new Student(10007L, "FirstName7", "LastName7"), actual.get());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findById_shouldNotFind_whenEntityApsent() {
        assertTrue(studentDao.findById(2000L).isEmpty());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findAll_shouldRetrieveAllRowEntity_whenDataExist() {
        List<Student> expected = Arrays.asList(new Student(10001L, 10001L, "FirstName1", "LastName1"),
            new Student(10002L, 10002L, "FirstName2", "LastName2"),
            new Student(10003L, 10002L, "FirstName3", "LastName3"),
            new Student(10004L, 10003L, "FirstName4", "LastName4"),
            new Student(10005L, 10003L, "FirstName5", "LastName5"),
            new Student(10006L, 10003L, "FirstName6", "LastName6"), new Student(10007L, "FirstName7", "LastName7"));
        List<Student> actual = studentDao.findAll();

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void deleteById() {
        studentDao.deleteById(10007L);
        assertTrue(studentDao.findById(10007L).isEmpty());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findByFullName() {
        List<Student> expected = Arrays.asList(new Student(10007L, "FirstName7", "LastName7"));
        List<Student> actual = studentDao.findByFullName("FirstName7", "LastName7");

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void findListByGroupId() {
        List<Student> expected = Arrays.asList(new Student(10002L, 10002L, "FirstName2", "LastName2"),
            new Student(10003L, 10002L, "FirstName3", "LastName3"));
        List<Student> actual = studentDao.findListByGroupId(10002L);

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void update() {
        Student expected = new Student(10007L, 10001L, "ChangedFirstName", "ChangedLastName");
        Student actual = studentDao.save(expected);

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void create() {
        Student actual = studentDao.save(new Student("NewFirstName", "NewLastName"));

        assertEquals(studentDao.findByFullName("NewFirstName", "NewLastName").get(0), actual);
    }

}

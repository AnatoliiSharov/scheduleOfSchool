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
import ua.com.foxminded.asharov.universityschedule.model.Lecture;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LectureDaoImplTest {

    @Autowired
    JdbcTemplate template;

    @Autowired
    NamedParameterJdbcTemplate namedParamTemplate;
    SimpleJdbcInsert simpleInsert;
    LectureDaoImpl lectureDao;

    @BeforeEach
    void setUp() throws Exception {
        lectureDao = new LectureDaoImpl(template, namedParamTemplate);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindById_shuldFind_whenRowExist() {
        Lecture expected = new Lecture(10001L, 1, LocalDate.of(0001, 01, 01), 10001L, 10001L, 10001L, 10001L);
        Optional<Lecture> actual = lectureDao.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindById_shuldNotFind_whenRowAbsent() {
        Optional<Lecture> actual = lectureDao.findById(9999L);

        assertTrue(actual.isEmpty());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindAll() {
        List<Lecture> expected = Arrays.asList(
            new Lecture(10001L, 1, LocalDate.of(0001, 01, 01), 10001L, 10001L, 10001L, 10001L),
            new Lecture(10002L, 2, LocalDate.of(0001, 01, 01), 10002L, 10002L, 10002L, 10001L),
            new Lecture(10003L, 3, LocalDate.of(0001, 01, 01), 10003L, 10003L, 10003L, 10001L),
            new Lecture(10004L, 1, LocalDate.of(0001, 01, 01), 10004L, 10003L, 10003L, 10002L),
            new Lecture(10005L, 2, LocalDate.of(0001, 01, 01), 10004L, 10001L, 10001L, 10002L),
            new Lecture(10006L, 3, LocalDate.of(0001, 01, 01), 10004L, 10002L, 10002L, 10002L),
            new Lecture(10007L, 1, LocalDate.of(0001, 01, 02), 10004L, 10001L, 10001L, 10001L),
            new Lecture(10008L, 2, LocalDate.of(0001, 01, 02), 10004L, 10002L, 10002L, 10001L),
            new Lecture(10009L, 3, LocalDate.of(0001, 01, 02), 10004L, 10003L, 10003L, 10001L),
            new Lecture(10010L, 1, LocalDate.of(0001, 01, 02), 10001L, 10003L, 10003L, 10002L),
            new Lecture(10011L, 2, LocalDate.of(0001, 01, 02), 10002L, 10001L, 10001L, 10002L),
            new Lecture(10012L, 3, LocalDate.of(0001, 01, 02), 10003L, 10002L, 10002L, 10002L));

        assertEquals(expected, lectureDao.findAll());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testDeleteById() {
        List<Lecture> expected = Arrays.asList(
            new Lecture(10002L, 2, LocalDate.of(0001, 01, 01), 10002L, 10002L, 10002L, 10001L),
            new Lecture(10003L, 3, LocalDate.of(0001, 01, 01), 10003L, 10003L, 10003L, 10001L),
            new Lecture(10004L, 1, LocalDate.of(0001, 01, 01), 10004L, 10003L, 10003L, 10002L),
            new Lecture(10005L, 2, LocalDate.of(0001, 01, 01), 10004L, 10001L, 10001L, 10002L),
            new Lecture(10006L, 3, LocalDate.of(0001, 01, 01), 10004L, 10002L, 10002L, 10002L),
            new Lecture(10007L, 1, LocalDate.of(0001, 01, 02), 10004L, 10001L, 10001L, 10001L),
            new Lecture(10008L, 2, LocalDate.of(0001, 01, 02), 10004L, 10002L, 10002L, 10001L),
            new Lecture(10009L, 3, LocalDate.of(0001, 01, 02), 10004L, 10003L, 10003L, 10001L),
            new Lecture(10010L, 1, LocalDate.of(0001, 01, 02), 10001L, 10003L, 10003L, 10002L),
            new Lecture(10011L, 2, LocalDate.of(0001, 01, 02), 10002L, 10001L, 10001L, 10002L),
            new Lecture(10012L, 3, LocalDate.of(0001, 01, 02), 10003L, 10002L, 10002L, 10002L));

        lectureDao.deleteById(10001L);
        assertEquals(expected, lectureDao.findAll());
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindGroupSchedule() {
        List<Lecture> expected = Arrays.asList(
            new Lecture(10001L, 1, LocalDate.of(0001, 01, 01), 10001L, 10001L, 10001L, 10001L),
            new Lecture(10002L, 2, LocalDate.of(0001, 01, 01), 10002L, 10002L, 10002L, 10001L),
            new Lecture(10003L, 3, LocalDate.of(0001, 01, 01), 10003L, 10003L, 10003L, 10001L),
            new Lecture(10007L, 1, LocalDate.of(0001, 01, 02), 10004L, 10001L, 10001L, 10001L),
            new Lecture(10008L, 2, LocalDate.of(0001, 01, 02), 10004L, 10002L, 10002L, 10001L),
            new Lecture(10009L, 3, LocalDate.of(0001, 01, 02), 10004L, 10003L, 10003L, 10001L));
        List<Lecture> actual = lectureDao.findGroupSchedule(10001L, LocalDate.of(0001, 01, 01),
            LocalDate.of(0001, 01, 02));

        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindTeacherSchedule() {
        List<Lecture> expected = Arrays.asList(
            new Lecture(10001L, 1, LocalDate.of(0001, 01, 01), 10001L, 10001L, 10001L, 10001L),
            new Lecture(10002L, 2, LocalDate.of(0001, 01, 01), 10002L, 10002L, 10002L, 10001L),
            new Lecture(10003L, 3, LocalDate.of(0001, 01, 01), 10003L, 10003L, 10003L, 10001L),
            new Lecture(10007L, 1, LocalDate.of(0001, 01, 02), 10004L, 10001L, 10001L, 10001L),
            new Lecture(10008L, 2, LocalDate.of(0001, 01, 02), 10004L, 10002L, 10002L, 10001L),
            new Lecture(10009L, 3, LocalDate.of(0001, 01, 02), 10004L, 10003L, 10003L, 10001L));
        List<Lecture> actual = lectureDao.findGroupSchedule(10001L, LocalDate.of(0001, 01, 01),
            LocalDate.of(0001, 01, 02));

        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual);

    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindRoomSchedule() {
        List<Lecture> expected = Arrays.asList(
            new Lecture(10002L, 2, LocalDate.of(0001, 01, 01), 10002L, 10002L, 10002L, 10001L),
            new Lecture(10011L, 2, LocalDate.of(0001, 01, 02), 10002L, 10001L, 10001L, 10002L));
        List<Lecture> actual = lectureDao.findRoomSchedule(10002L, LocalDate.of(0001, 01, 01),
            LocalDate.of(0001, 01, 02));

        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void update() {
        Lecture expected = new Lecture(10001L, 1, LocalDate.of(0001, 01, 01), 10005L, 10001L, 10001L, 10001L);

        assertFalse(lectureDao.findAll().contains(expected));

        Lecture actual = lectureDao.save(expected);

        assertEquals(actual, expected);
        assertTrue(lectureDao.findAll().contains(expected));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void create() {
        Lecture expected = new Lecture(1, LocalDate.of(0001, 01, 03), 10005L, 10003L, 10003L, 10003L);

        assertFalse(lectureDao.findAll().contains(expected));
        Lecture actual = lectureDao.save(expected);

        assertEquals(expected, actual);
        assertTrue(lectureDao.findAll().contains(expected));
    }

}

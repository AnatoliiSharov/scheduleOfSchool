package ua.com.foxminded.asharov.universityschedule.repository.custom.impl;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import ua.com.foxminded.asharov.universityschedule.entity.Group;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomGroupRepositoryImplTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    CustomGroupRepositoryImpl groupCustRepImpl;

    DateTimeFormatter pattern;

    @BeforeEach
    void setUp() throws Exception {
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindFreeWithTime() {
        List<Group> expected = Arrays.asList(new Group(10003L, "AA-33"));
        List<Group> actual = groupCustRepImpl.findFreeWithTime(LocalDate.parse("01-01-0001", pattern), 2);

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindFreeWithTimeWithTeacherId() {
        List<Group> expected = Arrays.asList(new Group(10003L, "AA-33"));
        List<Group> actual = groupCustRepImpl.findFreeWithTimeWithTeacherId(10003L,
                LocalDate.parse("01-01-0001", pattern), 2);

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindFreeWithTeacherWithCourseWithTime() {
        List<Group> expected = Arrays.asList(new Group(10003L, "AA-33"));
        List<Group> actual = groupCustRepImpl.findFreeWithTeacherWithCourseWithTime(10002L, 10001L,
                LocalDate.parse("01-01-0001", pattern), 2);

        assertEquals(expected, actual);
    }

}

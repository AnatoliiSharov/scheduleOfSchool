package ua.com.foxminded.asharov.universityschedule.repository.custom.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import ua.com.foxminded.asharov.universityschedule.entity.Teacher;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomTeacherRepositoryImplTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    CustomTeacherRepositoryImpl teacherCustRepImpl;
    
    DateTimeFormatter pattern;

    @BeforeEach
    void setUp() throws Exception {
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }
    
    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindWithCoureId() {
        List<Teacher> expected = Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "teacherFirstName2", "teacherLastName2"));

            assertEquals(expected, teacherCustRepImpl.findWithCoureId(10002L));
        }

    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindFreeOnesWithTimeForCourse() {
        List<Teacher> expected = Arrays.asList(new Teacher(10002L, "teacherFirstName2", "teacherLastName2"));
        assertEquals(expected, teacherCustRepImpl.findFreeOnesWithTimeForCourse(10004L, LocalDate.parse("01-01-0001", pattern), 1));
    }

    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindFreeWithGroupWithCourseByTime() {
        List<Teacher> expected = Arrays.asList(new Teacher(10003L, "teacherFirstName3", "teacherLastName3"));
        assertEquals(expected, teacherCustRepImpl.findFreeWithGroupWithCourseByTime(10003L, 10003L, LocalDate.parse("01-01-0001", pattern), 2));
    }
    
    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindFreeWithTimeWithGroupId() {
        List<Teacher> expected = Arrays.asList(new Teacher(10002L, "teacherFirstName2", "teacherLastName2"));
        assertEquals(expected, teacherCustRepImpl.findFreeWithTimeWithGroupId(10001L, LocalDate.parse("01-01-0001", pattern), 1));
    }

}

package ua.com.foxminded.asharov.universityschedule.repository.custom.impl;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import ua.com.foxminded.asharov.universityschedule.entity.Course;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomCourseRepositoryImplTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    CustomCourseRepositoryImpl courseImpl;

    DateTimeFormatter pattern;

    @BeforeEach
    void setUp() throws Exception {
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindWithGroup() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10005L, "Course5", "Description5"));

        assertEquals(expected, courseImpl.findWithGroup(10001L));
    }

    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindWithTeacher() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"));

        assertEquals(expected, courseImpl.findWithTeacher(10001L));
    }

    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindWithAccreditedTeacherForGroup() {
        List<Course> expected = Arrays.asList(new Course(10004L, "Course4", "Description4"));

        assertEquals(expected, courseImpl.findWithAccreditedTeacherForGroup(10002L, 10003L));
    }
  //TODO delete after all work
    @Test
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindFreeWithGroupWithFreeTeachersByTime() {
        List<Course> expected = Arrays.asList(new Course(10003L, "Course3", "Description3"),
                new Course(10004L, "Course4", "Description4"));
        List<Course> actual = courseImpl.findFreeWithGroupWithFreeTeachersByTime(10003L, LocalDate.parse("01-01-0001", pattern), 2); 
        
        Collections.sort(actual, (a, b) -> a.getId().compareTo(b.getId()));
        assertEquals(expected, actual);
    }
//TODO delete after all work
    @Test 
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void testFindFreeWithTeacherWithAllFreeGroupsByTime() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3"), new Course(10004L, "Course4", "Description4"));
        List<Course> actual = courseImpl.findFreeWithTeacherWithAllFreeGroupsByTime(10002L, LocalDate.parse("01-01-0001", pattern), 2);
                
        Collections.sort(actual, (a, b) -> a.getId().compareTo(b.getId()));
        assertEquals(expected, actual);
    }

    @Test 
    @Sql(scripts = { "/db/clean_db.sql", "/db/TestData.sql" })
    void findByFreeAccreditationTeachersWhoCanTeachTheGroup() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3"), new Course(10004L, "Course4", "Description4"));
        List<Course> actual = courseImpl.findFreeWithTeacherWithAllFreeGroupsByTime(10002L, LocalDate.parse("01-01-0001", pattern), 2);
        
        Collections.sort(actual, (a, b) -> a.getId().compareTo(b.getId()));
        assertEquals(expected, actual);
    }
    
    
}

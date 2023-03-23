package ua.com.foxminded.asharov.universityschedule.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Lecture;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LectureRepositoryTest {
    
    @Autowired
    EntityManager entityManager;

    @Autowired
    LectureRepository lectureRep;

    DateTimeFormatter pattern;
    Room rooms[];
    Teacher teachers[];
    Course courses[];
    Group groups[];
    
    @BeforeEach
    void setUp() throws Exception {
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        rooms = new Room[] { new Room(10001L, "Str, bldg1, fl1, off1", 60),
                new Room(10002L, "Str, bldg1, fl1, off2", 20), 
                new Room(10003L, "Str, bldg1, fl2, off3", 30),
                new Room(10004L, "Str, bldg2, fl1, off1", 10), 
                new Room(10005L, "address5", 50) };
        teachers = new Teacher[] { new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "teacherFirstName2", "teacherLastName2"),
                new Teacher(10003L, "teacherFirstName3", "teacherLastName3"),
                new Teacher(10004L, "teacherFirstName4", "teacherLastName4")};
        courses = new Course[] { new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"),
                new Course(10003L, "Course3", "Description3"),
                new Course(10004L, "Course4", "Description4"),
                new Course(10005L, "Course5", "Description5") };
        groups = new Group[] { new Group(10001L, "AA-11"),
                new Group(10002L, "AA-22"),
                new Group(10003L, "AA-33") };
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindAllByGroupAndDateBetween() {
        LocalDate startDay = LocalDate.parse("01-01-0001", pattern);
        LocalDate finishDay = LocalDate.parse("02-01-0001", pattern);
        List<Lecture> expected = Arrays.asList(
                new Lecture(10001L, 1, LocalDate.of(0001, 01, 01), rooms[0], teachers[0], courses[0], groups[0]),
                new Lecture(10002L, 2, LocalDate.of(0001, 01, 01), rooms[1], teachers[1], courses[1], groups[0]),
                new Lecture(10003L, 3, LocalDate.of(0001, 01, 01), rooms[2], teachers[2], courses[2], groups[0]),
                new Lecture(10007L, 1, LocalDate.of(0001, 01, 02), rooms[3], teachers[0], courses[0], groups[0]),
                new Lecture(10008L, 2, LocalDate.of(0001, 01, 02), rooms[3], teachers[1], courses[1], groups[0]),
                new Lecture(10009L, 3, LocalDate.of(0001, 01, 02), rooms[3], teachers[2], courses[2], groups[0]));
        
            List<Lecture> actual = lectureRep.findAllByDateBetweenAndGroup(startDay, finishDay, groups[0]);

            Collections.sort(expected, (o1, o2) -> o1.getId().compareTo(o2.getId()));
            Collections.sort(actual, (o1, o2) -> o1.getId().compareTo(o2.getId()));
            assertEquals(expected, actual);
        }
    
    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindAllByDateBetweenAndTeacher() {
        LocalDate startDay = LocalDate.parse("01-01-0001", pattern);
        LocalDate finishDay = LocalDate.parse("02-01-0001", pattern);
        List<Lecture> expected = Arrays.asList(
                new Lecture(10001L, 1, LocalDate.of(0001, 01, 01), rooms[0], teachers[0], courses[0], groups[0]),
                new Lecture(10005L, 2, LocalDate.of(0001, 01, 01), rooms[3], teachers[0], courses[0], groups[1]),
                new Lecture(10007L, 1, LocalDate.of(0001, 01, 02), rooms[3], teachers[0], courses[0], groups[0]),
                new Lecture(10011L, 2, LocalDate.of(0001, 01, 02), rooms[1], teachers[0], courses[0], groups[1]));
        
        List<Lecture> actual = lectureRep.findAllByDateBetweenAndTeacher(startDay, finishDay, teachers[0]);
        
        Collections.sort(expected, (o1, o2) -> o1.getId().compareTo(o2.getId()));
        Collections.sort(actual, (o1, o2) -> o1.getId().compareTo(o2.getId()));
        assertEquals(expected, actual);
    }
    
    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindAllByDateBetweenAndRoom() {
        LocalDate startDay = LocalDate.parse("01-01-0001", pattern);
        LocalDate finishDay = LocalDate.parse("02-01-0001", pattern);
        List<Lecture> expected = Arrays.asList(
                new Lecture(10001L, 1, LocalDate.of(0001, 01, 01), rooms[0], teachers[0], courses[0], groups[0]),
                new Lecture(10010L, 1, LocalDate.of(0001, 01, 02), rooms[0], teachers[2], courses[2], groups[1]));
        
        List<Lecture> actual = lectureRep.findAllByDateBetweenAndRoom(startDay, finishDay, rooms[0]);
        
        Collections.sort(expected, (o1, o2) -> o1.getId().compareTo(o2.getId()));
        Collections.sort(actual, (o1, o2) -> o1.getId().compareTo(o2.getId()));
        assertEquals(expected, actual);
    }
    

    
    



}

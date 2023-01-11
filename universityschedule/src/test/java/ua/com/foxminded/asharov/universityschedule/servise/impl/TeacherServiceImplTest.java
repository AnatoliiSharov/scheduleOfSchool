package ua.com.foxminded.asharov.universityschedule.servise.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.dao.CourseDao;
import ua.com.foxminded.asharov.universityschedule.dao.TeacherDao;
import ua.com.foxminded.asharov.universityschedule.model.Course;
import ua.com.foxminded.asharov.universityschedule.model.Teacher;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

@SpringBootTest(classes = {TeacherServiceImpl.class})
class TeacherServiceImplTest {

    @MockBean
    TeacherDao teacherDao;
    @MockBean
    CourseDao courseDao;

    @Autowired
    TeacherService teacherServ;

    @Test
    void testRetrieveAllTeachers() {
        List<Teacher> teachers = Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "teacherFirstName2", "teacherLastName2"),
                new Teacher(10003L, "teacherFirstName3", "teacherLastName3"));

        when(teacherDao.findAll()).thenReturn(teachers);
        assertEquals(teachers, teacherServ.retrieveAll());
        verify(teacherDao).findAll();
    }

    @Test
    void testRetrieveAccreditedCourseTeachers() {
        List<Teacher> teachers = Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "teacherFirstName2", "teacherLastName2"),
                new Teacher(10003L, "teacherFirstName3", "teacherLastName3"));
        Long courseId = 10001L;

        when(teacherDao.findByCoureId(courseId)).thenReturn(teachers);
        assertEquals(teachers, teacherServ.retrieveAccreditedTeachers(courseId));
        verify(teacherDao).findByCoureId(courseId);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTestRetrieveTeachersWithSimilarNames")
    void testRetrieveTeachersWithSimilarNames(String namePart, String surnamePart, List<Teacher> teachers) {

        when(teacherDao.findByFullName(namePart, surnamePart)).thenReturn(teachers);
        assertEquals(teachers, teacherServ.retrieveWithSimilarNames(namePart, surnamePart));
        verify(teacherDao).findByFullName(namePart, surnamePart);
    }

    private static Stream<Arguments> provideStringsForTestRetrieveTeachersWithSimilarNames() {
        Teacher firstT = new Teacher(10001L, "teacherFirstName1", "teacherLastName1");
        Teacher secondT = new Teacher(10002L, "teacherFirstName12", "teacherLastName32");
        Teacher therdT = new Teacher(10003L, "teacherFirstName3", "teacherLastName3");
        return Stream.of(Arguments.of("", "", Arrays.asList(firstT, secondT, therdT)),
                Arguments.of("teach", "Last", Arrays.asList(firstT, secondT, therdT)),
                Arguments.of("", "3", Arrays.asList(secondT, therdT)),
                Arguments.of("teach", "3", Arrays.asList(secondT, therdT)),
                Arguments.of("1", "", Arrays.asList(firstT, secondT)),
                Arguments.of("1", "Last", Arrays.asList(firstT, secondT)),
                Arguments.of("no one", "Last", Arrays.asList(new Teacher())),
                Arguments.of("teach", "no one", Arrays.asList(new Teacher())));
    }

    @Test
    void testRetrieveById() {
        Teacher expected = new Teacher(10001L, "teacherFirstName1", "teacherLastName1");
        Long teacherId = 10001L;

        when(teacherDao.findById(teacherId)).thenReturn(Optional.of(expected));

        assertEquals(expected, teacherServ.retrieveById(teacherId));
        verify(teacherDao).findById(teacherId);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTestEnterTeacher")
    void testEnterTeacher(Teacher start, Teacher expected) {
        when(teacherDao.save(start)).thenReturn(expected);
        Teacher actual = teacherServ.enter(start);

        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getId(), actual.getId());
        verify(teacherDao).save(start);
    }

    private static Stream<Arguments> provideStringsForTestEnterTeacher() {
        return Stream.of(
                Arguments.of(new Teacher(10001L, "firstName1", "lastName1"),
                        new Teacher(10001L, "firstName1", "lastName1")),
                Arguments.of(new Teacher("firstName1", "lastName1"), new Teacher(99999L, "firstName1", "lastName1")));
    }

    @Test
    void testRemoveById() {
        teacherServ.removeById(10002L);
        verify(teacherDao).deleteById(10002L);
    }

    @Test
    void testRetrieveFreeByTimeForCourse() {
        List<Teacher> expected = Arrays.asList(new Teacher(10002L, "teacherFirstName2", "teacherLastName2"));

        when(teacherDao.findFreeOnesByTimeForCourse(10004L, LocalDate.of(0001, 01, 01), 1)).thenReturn(expected);

        assertEquals(expected, teacherServ.retrieveFreeByTimeForCourse(10004L, LocalDate.of(0001, 01, 01), 1));
        verify(teacherDao).findFreeOnesByTimeForCourse(10004L, LocalDate.of(0001, 01, 01), 1);
    }
    
    @Test
    void testRetrieveFreeByGroupByCourseByTime() {
        List<Teacher> expected = Arrays.asList(new Teacher(10003L, "teacherFirstName3", "teacherLastName3"));
        
        when(teacherDao.findFreeByGroupByCourseByTime(10003L, 10003L, LocalDate.of(0001, 01, 01), 1)).thenReturn(expected);
        
        assertEquals(expected, teacherServ.retrieveFreeByGroupByCourseByTime(10003L, 10003L, LocalDate.of(0001, 01, 01), 1));
        verify(teacherDao).findFreeByGroupByCourseByTime(10003L, 10003L, LocalDate.of(0001, 01, 01), 1);
    }
    
    @Test
    void testRetrieveFreeLinkedCoursesByTimeByGroupId() {
        when(teacherDao.findFreeByTimeByGroupId(10002L, LocalDate.of(0001, 01, 01), 2))
        .thenReturn(Arrays.asList(new Teacher(10003L, "teacherFirstName3", "teacherLastName3")));
        when(courseDao.findByAccreditedTeacherForGroup(10002L, 10003L))
                .thenReturn(Arrays.asList(new Course(10004L, "Course4", "Description4"), new Course(10001L, "Course1", "Description1")));
        String expected = "[{Teacher [firstName=teacherFirstName3, lastName=teacherLastName3, Id=10003]=Course [ id=10004, name=Course4, description=Description4]\n"
                + "}, {Teacher [firstName=teacherFirstName3, lastName=teacherLastName3, Id=10003]=Course [ id=10001, name=Course1, description=Description1]\n"
                + "}]";
        List<Map<Teacher, Course>> actual = teacherServ.retrieveFreeLinkedCoursesByTimeByGroupId(10002L, LocalDate.of(0001, 01, 01), 2);
        
        assertEquals(expected, actual.toString());
        verify(teacherDao).findFreeByTimeByGroupId(10002L, LocalDate.of(0001, 01, 01), 2);
        verify(courseDao).findByAccreditedTeacherForGroup(10002L, 10003L);
    }

}

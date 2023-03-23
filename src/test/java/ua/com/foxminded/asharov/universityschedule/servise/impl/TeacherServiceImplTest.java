package ua.com.foxminded.asharov.universityschedule.servise.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.repository.TeacherRepository;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;
import ua.com.foxminded.asharov.universityschedule.service.impl.TeacherServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { TeacherServiceImpl.class })
class TeacherServiceImplTest {

    @MockBean
    TeacherRepository teacherRep;

    @Autowired
    TeacherService teacherServ;

    DateTimeFormatter pattern;

    @BeforeEach
    void setUp() throws Exception {
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    @Test
    void testRetrieveAllTeachers() {
        List<Teacher> teachers = Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "teacherFirstName2", "teacherLastName2"),
                new Teacher(10003L, "teacherFirstName3", "teacherLastName3"));

        when(teacherRep.findAll()).thenReturn(teachers);
        assertEquals(teachers, teacherServ.retrieveAll());
        verify(teacherRep).findAll();
    }

    @Test
    void testRetrieveAccreditedCourseTeachers() {
        List<Teacher> teachers = Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "teacherFirstName2", "teacherLastName2"),
                new Teacher(10003L, "teacherFirstName3", "teacherLastName3"));
        Long courseId = 10001L;

        when(teacherRep.findWithCoureId(courseId)).thenReturn(teachers);
        assertEquals(teachers, teacherServ.retrieveAccreditedTeachers(courseId));
        verify(teacherRep).findWithCoureId(courseId);
    }

    @Test
    void testRetrieveById() {
        Teacher expected = new Teacher(10001L, "teacherFirstName1", "teacherLastName1");
        Long teacherId = 10001L;

        when(teacherRep.findById(teacherId)).thenReturn(Optional.of(expected));
        assertEquals(expected, teacherServ.retrieveById(teacherId));
        verify(teacherRep).findById(teacherId);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTestEnterTeacher")
    void testEnterTeacher(Teacher start, Teacher expected) {
        when(teacherRep.save(start)).thenReturn(expected);
        assertEquals(expected, teacherServ.enter(start));
        verify(teacherRep).save(start);
    }

    private static Stream<Arguments> provideStringsForTestEnterTeacher() {
        return Stream.of(
                Arguments.of(new Teacher(10001L, "firstName1", "lastName1"), new Teacher(10001L, "firstName1", "lastName1")),
                Arguments.of(new Teacher("firstName1", "lastName1"),
                        new Teacher(99999L, "firstName1", "lastName1")));
    }

    @Test
    void testRemoveById() {
        teacherServ.removeById(10002L);
        verify(teacherRep).deleteById(10002L);
    }

    @Test
    void testRetrieveFreeByTimeForCourse() {
        List<Teacher> expected = Arrays.asList(new Teacher(10002L, "teacherFirstName2", "teacherLastName2"));

        when(teacherRep.findFreeOnesWithTimeForCourse(10004L, LocalDate.parse("01-01-0001", pattern), 1))
                .thenReturn(expected);
        assertEquals(expected,
                teacherServ.retrieveFreeByTimeForCourse(10004L, LocalDate.parse("01-01-0001", pattern), 1));
        verify(teacherRep).findFreeOnesWithTimeForCourse(10004L, LocalDate.parse("01-01-0001", pattern), 1);
    }

    @Test
    void testRetrieveFreeByGroupByCourseByTime() {
        List<Teacher> expected = Arrays.asList(new Teacher(10003L, "teacherFirstName3", "teacherLastName3"));

        when(teacherRep.findFreeWithGroupWithCourseByTime(10003L, 10003L, LocalDate.parse("01-01-0001", pattern), 1))
                .thenReturn(expected);
        assertEquals(expected, teacherServ.retrieveFreeByGroupByCourseByTime(10003L, 10003L,
                LocalDate.parse("01-01-0001", pattern), 1));
        verify(teacherRep).findFreeWithGroupWithCourseByTime(10003L, 10003L, LocalDate.parse("01-01-0001", pattern), 1);
    }

}

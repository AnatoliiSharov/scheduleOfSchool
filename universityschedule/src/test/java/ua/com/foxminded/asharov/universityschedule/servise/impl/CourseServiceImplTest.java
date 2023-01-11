package ua.com.foxminded.asharov.universityschedule.servise.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.dao.impl.CourseDaoImpl;
import ua.com.foxminded.asharov.universityschedule.model.Course;

@SpringBootTest(classes = { CourseServiceImpl.class })
class CourseServiceImplTest {

    @MockBean
    CourseDaoImpl courseDao;

    @Autowired
    CourseServiceImpl courseServ;

    @Test
    void testRetrieveByAccreditedTeacher() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"));

        when(courseDao.findByAccreditedTeacher(10001L)).thenReturn(expected);
        assertEquals(expected, courseServ.retrieveByAccreditedTeacher(10001L));
        verify(courseDao).findByAccreditedTeacher(10001L);
    }

    @Test
    void testRetrieveById() {
        Course expected = new Course(10001L, "Course1", "Description1");

        when(courseDao.findById(expected.getId())).thenReturn(Optional.of(expected));
        assertEquals(expected, courseServ.retrieveById(expected.getId()));
        verify(courseDao).findById(expected.getId());
    }

    @Test
    void testRetrieveAllCourses() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3"));

        when(courseDao.findAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        assertEquals(expected, courseServ.retrieveAll());
        verify(courseDao).findAll();

    }

    @Test
    void testAddAccreditedCourse() {
        Long teacherId = 10001L;
        Long courseId = 10005L;

        courseServ.addAccreditedCourse(teacherId, courseId);
        verify(courseDao).linkCourseAndTeacher(teacherId, courseId);
    }

    @Test
    void testRemoveAccreditedCourse() {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        courseServ.removeAccreditedCourse(teacherId, courseId);
        verify(courseDao).ripCourseAndTeacher(teacherId, courseId);
    }

    @Test
    void testRetrieveByGroupId() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"));

        when(courseDao.findByGroupId(10001L)).thenReturn(expected);
        assertEquals(expected, courseServ.retrieveByGroupId(10001L));
        verify(courseDao).findByGroupId(10001L);

    }

    @Test
    void testAddToGroup() {
        Long courseId = 10001L;
        Long groupId = 10005L;

        courseServ.addToGroup(courseId, groupId);
        verify(courseDao).linkCourseAndGroup(courseId, groupId);
    }

    @Test
    void testRemoveFromGroup() {
        Long courseId = 10003L;
        Long groupId = 10001L;

        courseServ.removeFromGroup(courseId, groupId);
        verify(courseDao).ripCourseAndGroup(courseId, groupId);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTestEnter")
    void testEnter(Course start, Course expected) {

        when(courseDao.save(start)).thenReturn(expected);
        Course actual = courseServ.enter(start);

        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getId(), actual.getId());
        verify(courseDao).save(start);
    }

    private static Stream<Arguments> provideStringsForTestEnter() {
        return Stream.of(
                Arguments.of(new Course(10001L, "Name", "Description"), new Course(10001L, "Name", "Description")),
                Arguments.of(new Course(null, "Name", "Description"), new Course(10001L, "Name", "Description")));
    }

    @Test
    void testRemoveById() {
        courseServ.removeById(10002L);
        verify(courseDao).deleteById(10002L);
    }

    @Test
    void testRetrieveFreeByGroupWithFreeTeachersByTime() {
        List<Course> courses = Arrays.asList(new Course(1L, "CourseName1", "Description1"));

        when(courseDao.findFreeByGroupWithFreeTeachersByTime(1L, LocalDate.of(0001, 01, 01), 1)).thenReturn(courses);

        assertEquals(courses, courseServ.retrieveFreeByGroupWithFreeTeachersByTime(1L, LocalDate.of(0001, 01, 01), 1));

        verify(courseDao).findFreeByGroupWithFreeTeachersByTime(1L, LocalDate.of(0001, 01, 01), 1);

    }

    @Test
    void testRetrieveFreeByTeacherWithFreeGroupsByTime() {
        List<Course> courses = Arrays.asList(new Course(1L, "CourseName1", "Description1"));

        when(courseDao.findFreeByTeacherWithAllFreeGroupsByTime(1L, LocalDate.of(0001, 01, 01), 1))
                .thenReturn(Arrays.asList(new Course(1L, "CourseName1", "Description1")));

        assertEquals(courses, courseServ.retrieveFreeByTeacherWithFreeGroupsByTime(1L, LocalDate.of(0001, 01, 01), 1));

        verify(courseDao).findFreeByTeacherWithAllFreeGroupsByTime(1L, LocalDate.of(0001, 01, 01), 1);

    }

}

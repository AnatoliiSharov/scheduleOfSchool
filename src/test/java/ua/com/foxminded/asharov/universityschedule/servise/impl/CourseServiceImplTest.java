package ua.com.foxminded.asharov.universityschedule.servise.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.dto.CourseDto;
import ua.com.foxminded.asharov.universityschedule.entity.*;
import ua.com.foxminded.asharov.universityschedule.repository.AccreditationRepository;
import ua.com.foxminded.asharov.universityschedule.repository.CourseRepository;
import ua.com.foxminded.asharov.universityschedule.repository.EducationRepository;
import ua.com.foxminded.asharov.universityschedule.repository.GroupRepository;
import ua.com.foxminded.asharov.universityschedule.repository.TeacherRepository;
import ua.com.foxminded.asharov.universityschedule.service.impl.CourseServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CourseServiceImpl.class})
class CourseServiceImplTest {

    @MockBean
    CourseRepository courseRep;
    @MockBean
    EducationRepository educationRep;
    @MockBean
    TeacherRepository teacherRep;
    @MockBean
    GroupRepository groupRep;
    @MockBean
    AccreditationRepository accreditationRep;
    
    @Autowired
    CourseServiceImpl courseServ;
    
    DateTimeFormatter pattern;

    @BeforeEach
    void setUp() throws Exception {
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }
    
    @Test
    void testRetrieveByAccreditedTeacher() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
            new Course(10002L, "Course2", "Description2"));

        when(courseRep.findWithTeacher(10001L)).thenReturn(expected);
        assertEquals(expected, courseServ.retrieveByAccreditedTeacher(10001L));
        verify(courseRep).findWithTeacher(10001L);
    }

    @Test
    void testRetrieveById() {
        Course expected = new Course(10001L, "Course1", "Description1");

        when(courseRep.findById(expected.getId())).thenReturn(Optional.of(expected));
        assertEquals(expected, courseServ.retrieveById(expected.getId()));
        verify(courseRep).findById(expected.getId());
    }

    @Test
    void testRetrieveAllCourses() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
            new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3"));

        when(courseRep.findAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
            new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        assertEquals(expected, courseServ.retrieveAll());
        verify(courseRep).findAll();

    }

   @Test
    void testAddAccreditedCourse() {
        Course course = new Course(10001L, "Course1", "Description1");
        Teacher teacher = new Teacher(10001L, "Name1", "Surname1");
        
        when(teacherRep.findById(10001L)).thenReturn(Optional.of(teacher));
        when(courseRep.findById(10001L)).thenReturn(Optional.of(course));
        courseServ.addAccreditedCourse(teacher.getId(), course.getId());
        verify(accreditationRep).save(new Accreditation(course, teacher));
    }

    @Test
    void testRemoveAccreditedCourse() {
        Course course = new Course(10001L, "Course1", "Description1");
        Teacher teacher = new Teacher(10001L, "Name1", "Surname1");
        
        when(teacherRep.findById(10001L)).thenReturn(Optional.of(teacher));
        when(courseRep.findById(10001L)).thenReturn(Optional.of(course));
        courseServ.removeAccreditedCourse(teacher.getId(), course.getId());
        verify(accreditationRep).delete(new Accreditation(course, teacher));
    }

    @Test
    void testRetrieveByGroupId() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"),
            new Course(10002L, "Course2", "Description2"));

        when(courseRep.findWithGroup(10001L)).thenReturn(expected);
        assertEquals(expected, courseServ.retrieveByGroupId(10001L));
        verify(courseRep).findWithGroup(10001L);

    }

    @Test
    void testAddToGroup() {
        Course course = new Course(10001L, "Course1", "Description1");
        Group group = new Group(10001L, "Group1");
        
        when(groupRep.findById(10001L)).thenReturn(Optional.of(group));
        when(courseRep.findById(10001L)).thenReturn(Optional.of(course));
        courseServ.addToGroup(course.getId(), group.getId());
        verify(educationRep).save(new Education(group, course));
    }

    @Test
    void testRemoveFromGroup() {
        Course course = new Course(10001L, "Course1", "Description1");
        Group group = new Group(10001L, "Group1");
        
        when(groupRep.findById(10001L)).thenReturn(Optional.of(group));
        when(courseRep.findById(10001L)).thenReturn(Optional.of(course));
        courseServ.removeFromGroup(course.getId(), group.getId());
        verify(educationRep).delete(new Education(group, course));
    }
        

    @ParameterizedTest
    @MethodSource("provideStringsForTestEnter")
    void testEnter(CourseDto startDto, Course start, Course expected) {

        when(courseRep.save(start)).thenReturn(expected);
        when(courseRep.findById(startDto.getId())).thenReturn(Optional.of(start));
        assertEquals(expected, courseServ.enter(start));
        verify(courseRep).save(start);
    }

    private static Stream<Arguments> provideStringsForTestEnter() {
        return Stream.of(
            Arguments.of(new CourseDto(10001L, "Name", "Description"), new Course(10001L, "Name", "Description"), new Course(10001L, "Name", "Description")),
            Arguments.of(new CourseDto(null, "Name", "Description"), new Course(null, "Name", "Description"), new Course(10001L, "Name", "Description")));
    }

    @Test
    void testRemoveById() {
        courseServ.removeById(10002L);
        verify(courseRep).deleteById(10002L);
    }

    @Test
    void testRetrieveFreeByGroupWithFreeTeachersByTime() {
        List<Course> expected = Arrays.asList(new Course(10003L, "Course3", "Description3"),
                new Course(10004L, "Course4", "Description4"));

        when(courseRep.findFreeWithGroupWithFreeTeachersByTime(10003L, LocalDate.parse("01-01-0001", pattern), 2)).thenReturn(Arrays.asList(new Course(10003L, "Course3", "Description3"),
                new Course(10004L, "Course4", "Description4")));
        List<Course> actual = courseServ.retrieveFreeByGroupWithFreeTeachersByTime(10003L, LocalDate.parse("01-01-0001", pattern), 2);
        
        assertEquals(expected, actual);
        verify(courseRep).findFreeWithGroupWithFreeTeachersByTime(10003L, LocalDate.parse("01-01-0001", pattern), 2);

    }

    @Test
    void testRetrieveFreeByTeacherWithFreeGroupsByTime() {
        List<Course> expected = Arrays.asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3"), new Course(10004L, "Course4", "Description4"));

        when(courseRep.findFreeWithTeacherWithAllFreeGroupsByTime(10002L, LocalDate.parse("01-01-0001", pattern), 2))
            .thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3"), new Course(10004L, "Course4", "Description4")));
        List<Course> actual = courseServ.retrieveFreeByTeacherWithFreeGroupsByTime(10002L, LocalDate.parse("01-01-0001", pattern), 2);
        
        assertEquals(expected, actual);
        verify(courseRep).findFreeWithTeacherWithAllFreeGroupsByTime(10002L, LocalDate.parse("01-01-0001", pattern), 2);

    }

}

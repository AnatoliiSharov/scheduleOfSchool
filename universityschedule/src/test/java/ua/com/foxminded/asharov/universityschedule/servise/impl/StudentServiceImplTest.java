package ua.com.foxminded.asharov.universityschedule.servise.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import ua.com.foxminded.asharov.universityschedule.dao.StudentDao;
import ua.com.foxminded.asharov.universityschedule.model.Student;
import ua.com.foxminded.asharov.universityschedule.service.StudentService;

@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {

    @MockBean
    StudentDao studentDao;

    @Autowired
    StudentService studentServ;

    @Test
    void testRetrieveAll() {
        List<Student> expected = Arrays.asList(new Student(10001L, 10001L, "FirstName1", "FirstName1"),
                new Student(10002L, 10002L, "FirstName2", "FirstName2"),
                new Student(10003L, 10002L, "FirstName3", "FirstName3"));

        when(studentDao.findAll()).thenReturn(expected);
        assertEquals(expected, studentServ.retrieveAll());
        verify(studentDao).findAll();
    }

    @Test
    void testRetrieveByGroupId() {
        List<Student> expected = Arrays.asList(new Student(10001L, 10001L, "FirstName1", "FirstName1"),
                new Student(10002L, 10002L, "FirstName2", "FirstName2"),
                new Student(10003L, 10002L, "FirstName3", "FirstName3"));
        Long groupId = 10001L;

        when(studentDao.findListByGroupId(groupId)).thenReturn(expected);
        assertEquals(expected, studentServ.retrieveByGroupId(groupId));
        verify(studentDao).findListByGroupId(groupId);
    }

    @Test
    void testRetrieveById() {
        Student student = new Student(10001L, 10001L, "FirstName1", "FirstName1");
        Long studentId = 10001L;

        when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
        assertEquals(student, studentServ.retrieveById(studentId));
        verify(studentDao).findById(studentId);
    }

    @ParameterizedTest
    @MethodSource("provideStringsFortestRetrieveByName")
    void testRetrieveByName(String firstName, String lastName, List<Student> students) {

        when(studentDao.findByFullName(firstName, lastName)).thenReturn(students);
        assertEquals(students, studentServ.retrieveByName(firstName, lastName));
        verify(studentDao).findByFullName(firstName, lastName);
    }

    private static Stream<Arguments> provideStringsFortestRetrieveByName() {
        return Stream.of(
                Arguments.of("firstName", "lastName",
                        Arrays.asList(new Student(10001L, 10002L, "firstName", "lastName"))),
                Arguments.of("something", "lastName", Arrays.asList(new Student())),
                Arguments.of("firstName", "something", Arrays.asList(new Student())),
                Arguments.of("", "", Arrays.asList(new Student())));
    }

    @ParameterizedTest
    @MethodSource("provideStringsForTestEnter")
    void testEnter(Student start, Student expected) {
        when(studentDao.save(start)).thenReturn(expected);
        Student actual = studentServ.enter(start);

        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getGroupId(), actual.getGroupId());
        assertEquals(expected.getId(), actual.getId());
        verify(studentDao).save(start);
    }

    private static Stream<Arguments> provideStringsForTestEnter() {
        return Stream.of(
                Arguments.of(new Student(10001L, 10006L, "firstName1", "lastName1"),
                        new Student(10001L, 10006L, "firstName1", "lastName1")),
                Arguments.of(new Student(null, 10006L, "firstName1", "lastName1"),
                        new Student(9999L, 10006L, "firstName1", "lastName1")));
    }

    @Test
    void testRemoveById() {
        studentServ.removeById(10002L);
        verify(studentDao).deleteById(10002L);
    }

}

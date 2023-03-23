package ua.com.foxminded.asharov.universityschedule.servise.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.dto.StudentDto;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Student;
import ua.com.foxminded.asharov.universityschedule.repository.GroupRepository;
import ua.com.foxminded.asharov.universityschedule.repository.StudentRepository;
import ua.com.foxminded.asharov.universityschedule.service.StudentService;
import ua.com.foxminded.asharov.universityschedule.service.impl.StudentServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {

    @MockBean
    StudentRepository studentRep;
    @MockBean
    GroupRepository groupRep;

    @Autowired
    StudentService studentServ;
    
    static Group[] groups;

    @BeforeEach
    void setUp() throws Exception {
        groups = new Group[]{new Group(1L, "group1"), new Group(2L, "group2"), new Group(3L, "group3")}; 
    }
    
    @Test
    void testRetrieveAll() {
        List<Student> expected = Arrays.asList(new Student(10001L, groups[1], "FirstName1", "FirstName1"),
            new Student(10002L, groups[2], "FirstName2", "FirstName2"),
            new Student(10003L, groups[2], "FirstName3", "FirstName3"));

        when(studentRep.findAll()).thenReturn(expected);
        assertEquals(expected, studentServ.retrieveAll());
        verify(studentRep).findAll();
    }

    @Test
    void testRetrieveByGroupId() {
        List<Student> expected = Arrays.asList(new Student(10001L, groups[1], "FirstName1", "FirstName1"),
            new Student(10002L, groups[2], "FirstName2", "FirstName2"),
            new Student(10003L, groups[2], "FirstName3", "FirstName3"));
        Group group = groups[1];

        when(groupRep.findById(group.getId())).thenReturn(Optional.of(group));
        when(studentRep.findByGroup(group)).thenReturn(expected);
        assertEquals(expected, studentServ.retrieveByGroupId(group.getId()));
        verify(groupRep).findById(group.getId());
        verify(studentRep).findByGroup(group);
    }

    @Test
    void testRetrieveById() {
        Student student = new Student(10001L, groups[1], "FirstName1", "FirstName1");
        Long studentId = 10001L;
        
        when(studentRep.findById(studentId)).thenReturn(Optional.of(student));
        assertEquals(student, studentServ.retrieveById(studentId));
        verify(studentRep).findById(studentId);
    }

    @Test
    void testEnterSpare() {
        Student start = new Student(groups[2], "firstName1", "lastName1");
        Student expected = new Student(10001L, groups[2], "firstName2", "lastName2");
        
        when(studentRep.save(new Student(start.getId(), groups[2], start.getFirstName(), start.getLastName()))).thenReturn(expected);
        Student actual = studentServ.enter(start);
        
        assertEquals(expected, actual);
        verify(studentRep).save(new Student(start.getId(), groups[2], start.getFirstName(), start.getLastName()));
    }
    
//TODO ask master - when I delete testEnterSpare the method testRnter collapses with NPE
    @ParameterizedTest
    @MethodSource("provideStringsForTestEnter")
    void testEnter(Student start) {
        Student expected = new Student(10001L, groups[2], "firstName1", "lastName1");
       
        when(studentRep.save(start)).thenReturn(expected);
        Student actual = studentServ.enter(start);
        assertEquals(expected, actual);
        verify(studentRep).save(start);
    }

    private static Stream<Arguments> provideStringsForTestEnter() {
        return Stream.of(
            Arguments.of(new Student(10001L, groups[2], "firstName1", "lastName1")),
            Arguments.of(new Student(groups[2], "firstName1", "lastName1")));
    }
    @Test
    void testRemoveById() {
        when(studentRep.existsById(10002L)).thenReturn(true);
        studentServ.removeById(10002L);
        verify(studentRep).existsById(10002L);
        verify(studentRep).deleteById(10002L);
    }

}

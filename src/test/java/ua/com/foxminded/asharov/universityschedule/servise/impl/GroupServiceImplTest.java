package ua.com.foxminded.asharov.universityschedule.servise.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Student;
import ua.com.foxminded.asharov.universityschedule.repository.GroupRepository;
import ua.com.foxminded.asharov.universityschedule.repository.StudentRepository;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.impl.GroupServiceImpl;

@SpringBootTest(classes = {GroupServiceImpl.class})
class GroupServiceImplTest {

    @MockBean
    GroupRepository groupRep;
    @MockBean
    StudentRepository studentRep;
    
    @Autowired
    GroupService groupServ;

    List<Group> expected;
    
    DateTimeFormatter pattern;

    @BeforeEach
    void setUp() throws Exception {
        pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        expected = Arrays.asList(new Group(10001L, "AA-11"), new Group(10002L, "AA-22"));
    }

    @Test
    void testRetrieveGroupByStudentId() {
        when(studentRep.findById(1L)).thenReturn(Optional.of(new Student(1L, expected.get(0), "name", "surname")));
        
        assertEquals(expected.get(0), groupServ.retrieveGroupByStudentId(1L));
        verify(studentRep).findById(1L);
    }

    @Test
    void testRetrieveAllGroups() {
        when(groupRep.findAll()).thenReturn(Arrays.asList(new Group(10001L, "AA-11"), new Group(10002L, "AA-22")));
        assertEquals(expected, groupServ.retrieveAll());
        verify(groupRep).findAll();
    }

    @Test
    void testRetrieveById() {
        when(groupRep.findById(10001L)).thenReturn(Optional.of(new Group(10001L, "AA-11")));
        assertEquals(expected.get(0), groupServ.retrieveById(10001L));
        verify(groupRep).findById(10001L);
    }

    @Test
    void testRetrieveFreeByTime() {
        List<Group> expected = Arrays.asList(new Group(10003L, "AA-33"));

        when(groupRep.findFreeWithTime(LocalDate.parse("01-01-0001", pattern), 2)).thenReturn(Arrays.asList(new Group(10003L, "AA-33")));
        assertEquals(expected, groupServ.retrieveFreeByTime(LocalDate.parse("01-01-0001", pattern), 2));
        verify(groupRep).findFreeWithTime(LocalDate.parse("01-01-0001", pattern), 2);
    }

    @Test
    void testFreeByTeacherByCourseByTime() {
        when(groupRep.findFreeWithTeacherWithCourseWithTime(10002L, 10001L, LocalDate.of(01, 01, 0001), 2))
            .thenReturn(expected);
        assertEquals(expected, groupServ.retrieveFreeByTeacherByCourseByTime(10002L, 10001L, LocalDate.parse("01-01-0001", pattern), 2));
        verify(groupRep).findFreeWithTeacherWithCourseWithTime(10002L, 10001L, LocalDate.parse("01-01-0001", pattern), 2);
    }

}

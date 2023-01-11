package ua.com.foxminded.asharov.universityschedule.servise.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.asharov.universityschedule.dao.CourseDao;
import ua.com.foxminded.asharov.universityschedule.dao.GroupDao;
import ua.com.foxminded.asharov.universityschedule.model.Course;
import ua.com.foxminded.asharov.universityschedule.model.Group;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;

@SpringBootTest(classes = {GroupServiceImpl.class})
class GroupServiceImplTest {

    @MockBean
    GroupDao groupDao;
    @MockBean
    CourseDao courseDao;
    @Autowired
    GroupService groupServ;

    List<Group> expected;

    @BeforeEach
    void setUp() {
        expected = Arrays.asList(new Group(10001L, "AA-11"), new Group(10002L, "AA-22"));
    }

    @Test
    void testRetrieveGroupByStudentId() {
        when(groupDao.findByStudentId(10006L)).thenReturn(Optional.of(new Group(10001L, "AA-11")));
        assertEquals(expected.get(0), groupServ.retrieveGroupByStudentId(10006L));
        verify(groupDao).findByStudentId(10006L);
    }

    @Test
    void testRetrieveAllGroups() {
        when(groupDao.findAll()).thenReturn(Arrays.asList(new Group(10001L, "AA-11"), new Group(10002L, "AA-22")));
        assertEquals(expected, groupServ.retrieveAll());
        verify(groupDao).findAll();
    }

    @Test
    void testRetrieveById() {
        when(groupDao.findById(10001L)).thenReturn(Optional.of(new Group(10001L, "AA-11")));
        assertEquals(expected.get(0), groupServ.retrieveById(10001L));
        verify(groupDao).findById(10001L);
    }
    
    @Test
    void testRetrieveFreeByTime() {
        List<Group> expected = Arrays.asList(new Group(10003L, "AA-33"));
        
        when(groupDao.findFreeByTime(LocalDate.of(0001, 01, 01), 2)).thenReturn(Arrays.asList(new Group(10003L, "AA-33")));
        assertEquals(expected, groupServ.retrieveFreeByTime(LocalDate.of(0001, 01, 01), 2));
        verify(groupDao).findFreeByTime(LocalDate.of(0001, 01, 01), 2);
    }
    
    @Test
    void testRetrieveFreeLinkedCoursesByTimeByTeacherId() {
        String expected = "[{Group [name=AA-33, id=10003]"
                + "=Course [ id=10004, name=Course4, description=Description4]\n"
                + "}, {Group [name=AA-33, id=10003]"
                + "=Course [ id=10001, name=Course1, description=Description1]\n"
                + "}]";
        
        when(groupDao.findFreeByTimeByTeacherId(10002L, LocalDate.of(01, 01, 0001), 2))
        .thenReturn(Arrays.asList(new Group(10003L, "AA-33")));
        when(courseDao.findByAccreditedTeacherForGroup(10003L, 10002L))
        .thenReturn(Arrays.asList(new Course(10004L, "Course4", "Description4"), new Course(10001L, "Course1", "Description1")));
        
        assertEquals(expected, groupServ.retrieveFreeLinkedCoursesByTimeByTeacherId(10002L, LocalDate.of(01, 01, 0001), 2).toString());
        
        verify(groupDao).findFreeByTimeByTeacherId(10002L, LocalDate.of(01, 01, 0001), 2);
        verify(courseDao).findByAccreditedTeacherForGroup(10003L, 10002L);
    }

    @Test
    void testFreeByTeacherByCourseByTime() {
        List<Group>groups = Arrays.asList(new Group(10001L, "AA-11"));
        
        when(groupDao.findFreeFreeByTeacherByCourseByTime(10002L, 10001L, LocalDate.of(01, 01, 0001), 2))
        .thenReturn(groups);
        
        assertEquals(groups, groupDao.findFreeFreeByTeacherByCourseByTime(10002L, 10001L, LocalDate.of(01, 01, 0001), 2));
        
        verify(groupDao).findFreeFreeByTeacherByCourseByTime(10002L, 10001L, LocalDate.of(01, 01, 0001), 2);
    }
    
}

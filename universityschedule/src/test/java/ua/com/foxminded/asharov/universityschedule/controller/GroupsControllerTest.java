package ua.com.foxminded.asharov.universityschedule.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.asharov.universityschedule.model.Course;
import ua.com.foxminded.asharov.universityschedule.model.Group;
import ua.com.foxminded.asharov.universityschedule.model.Student;
import ua.com.foxminded.asharov.universityschedule.model.Adapter;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.LectureService;
import ua.com.foxminded.asharov.universityschedule.service.RoomService;
import ua.com.foxminded.asharov.universityschedule.service.StudentService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

@WebMvcTest(controllers = GroupsController.class)
class GroupsControllerTest {

    @MockBean
    LectureService lectureServ;
    @MockBean
    TeacherService teacherServ;
    @MockBean
    GroupService groupServ;
    @MockBean
    RoomService roomServ;
    @MockBean
    CourseService courseServ;
    @MockBean
    StudentService studentServ;
    @MockBean
    Adapter lectureAdapt;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    GroupsController groupsController;

    @Test
    void testShowAll_shouldShowList_whenRun() throws Exception {

        when(groupServ.retrieveAll())
                .thenReturn(Arrays.asList(new Group(10001L, "AA-11"),
                        new Group(10002L, "AA-22"),
                        new Group(10003L, "AA-33")));

        mockMvc.perform(get("/groups")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/groups/selection"))
                .andExpect(content().string(containsString("AA-11")))
                .andExpect(content().string(containsString("AA-22")))
                .andExpect(content().string(containsString("AA-33")));
    }

    @Test
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        when(groupServ.retrieveById(10001L))
                .thenReturn(new Group(10001L, "AA-11"));
        when(courseServ.retrieveByGroupId(10001L)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10002L, "Course2", "Description2")));
        when(studentServ.retrieveByGroupId(10001L)).thenReturn(Arrays
                .asList(new Student(10001L, 10001L, "FirstName1", "LastName1"),
                        new Student(10002L, 10002L, "FirstName2", "LastName2")));

        mockMvc.perform(get("/groups/{id}", "10001")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/groups/dashboard"))
                .andExpect(content().string(containsString("AA-11")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")))
                .andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("LastName1")))
                .andExpect(content().string(containsString("FirstName2")))
                .andExpect(content().string(containsString("LastName2")));
    }

    @Test
    void testInviteNew() throws Exception {
        mockMvc.perform(get("/groups/new")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/groups/newbie"));
    }

    @Test
    void testLoad() throws Exception {
        when(groupServ.enter(new Group(null, "AA-11")))
                .thenReturn(new Group(10001L, "AA-11"));
        mockMvc.perform(post("/groups").param("name", "AA-11")).andExpect(status().is(302))
                .andExpect(view().name("redirect:groups/" + 10001L));
    }
    
    @Test
    void testModify() throws Exception {
        Long id = 10001L;

        when(groupServ.retrieveById(id)).thenReturn(new Group(10001L, "AA-11"));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        when(courseServ.retrieveByAccreditedTeacher(id)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3")));
        mockMvc.perform(get("/groups/{id}/modify", id)).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/groups/modification"))
                .andExpect(content().string(containsString("AA-11")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")))
                .andExpect(content().string(containsString("Course3")));
    }

    @Test
    void testReload() throws Exception {
        when(groupServ.enter(new Group(10001L, "AA-11")))
        .thenReturn(new Group(10001L, "AA-11"));
        
        mockMvc.perform(patch("/groups/")
                .param("id", "10001")
                .param("name", "AA-11"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:groups/10001"));
    }
    @Test
    void testAddAccreditedCourse() throws Exception {
        Long groupId = 10001L; 
        Long courseId = 10001L;

        mockMvc.perform(patch("/groups/{id}/add", groupId, courseId)
                .param("groupId", groupId.toString())
                .param("courseId", courseId.toString()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:modify"));
        verify(courseServ).addToGroup(courseId, groupId);
    }
    
    @Test
    void testRemoveAccreditedCourse() throws Exception {
        Long groupId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/groups/{id}/remove", groupId, courseId)
                .param("groupId", groupId.toString())
                .param("courseId", courseId.toString()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:modify"));
        verify(courseServ).removeFromGroup(courseId, groupId);
    }

    void testDelete() throws Exception {
        mockMvc.perform(delete("/groups/{id}", 10001L))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/groups"));
        
        verify(groupServ).removeById(10001L);
    }
}

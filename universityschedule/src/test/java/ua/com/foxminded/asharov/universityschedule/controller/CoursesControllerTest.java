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
import ua.com.foxminded.asharov.universityschedule.model.Teacher;
import ua.com.foxminded.asharov.universityschedule.model.Adapter;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.LectureService;
import ua.com.foxminded.asharov.universityschedule.service.RoomService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

@WebMvcTest(controllers = CoursesController.class)
class CoursesControllerTest {

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
    Adapter lectureAdapt;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CoursesController courseController;

    @Test
    void testShowAll_shouldShowList_whenRun() throws Exception {

        when(courseServ.retrieveAll())
                .thenReturn(Arrays.asList(new Course(10001L, "name1", "description1"),
                        new Course(10001L, "name2", "description2"),
                        new Course(10001L, "name3", "description3")));

        mockMvc.perform(get("/courses")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/selection"))
                .andExpect(content().string(containsString("name1")))
                .andExpect(content().string(containsString("name2")))
                .andExpect(content().string(containsString("name3")));
    }

    @Test
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        when(courseServ.retrieveById(10001L))
                .thenReturn(new Course(10001L, "Course1", "Description1"));
        when(teacherServ.retrieveAccreditedTeachers(10001L)).thenReturn(Arrays
                .asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"), new Teacher(10002L, "teacherFirstName2", "teacherLastName2")));

        mockMvc.perform(get("/courses/{id}", "10001")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/dashboard"))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("teacherFirstName1")))
                .andExpect(content().string(containsString("teacherLastName1")))
        .andExpect(content().string(containsString("teacherFirstName2")))
        .andExpect(content().string(containsString("teacherLastName2")));
    }

    @Test
    void testInviteNew() throws Exception {
        mockMvc.perform(get("/courses/new")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/newbie"));
    }

    @Test
    void testLoad() throws Exception {
        when(courseServ.enter(new Course(null, "Course1", "Description1")))
                .thenReturn(new Course(10001L, "Course1", "Description1"));

        mockMvc.perform(post("/courses").param("name", "Course1")
                .param("description", "Description1")).andExpect(status().is(302))
                .andExpect(view().name("redirect:courses/" + 10001L));
    }

    @Test
    void testModify() throws Exception {
        Long id = 10001L;

        when(courseServ.retrieveById(id)).thenReturn(new Course(10001L, "Course1", "Description1"));
        
        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "teacherFirstName2", "teacherLastName2"),
                new Teacher(10003L, "teacherFirstName3", "teacherLastName3")));
        when(teacherServ.retrieveAccreditedTeachers(id)).thenReturn(Arrays
                .asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                        new Teacher(10002L, "teacherFirstName2", "teacherLastName2")));

        mockMvc.perform(get("/courses/{id}/modify", id)).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/modification"))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Description1")))
                .andExpect(content().string(containsString("t.teacherLastName2")))
                .andExpect(content().string(containsString("t.teacherLastName2")))
                .andExpect(content().string(containsString("t.teacherLastName3")));
    }

    @Test
    void testReload() throws Exception {
        when(courseServ.enter(new Course(10001L, "Course1", "Description1")))
                .thenReturn(new Course(10001L, "Course1", "Description1"));
        
        mockMvc.perform(patch("/courses")
                .param("id", "10001")
                .param("name", "Course1")
                .param("description", "Description1"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:courses/10001"));
    }
    @Test
    void testAddAccreditedCourse() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/courses/{id}/add", teacherId, courseId)
                .param("teacherId", teacherId.toString())
                .param("courseId", courseId.toString()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:modify"));
        
        verify(courseServ).addAccreditedCourse(teacherId, courseId);
    }
    
    @Test
    void testRemoveAccreditedCourse() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/courses/{id}/remove", teacherId, courseId)
                .param("teacherId", teacherId.toString())
                .param("courseId", courseId.toString()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:modify"));
        
        verify(courseServ).removeAccreditedCourse(teacherId, courseId);
    }
    
    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/courses/{id}", 10001L))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/courses"));
        verify(courseServ).removeById(10001L);
    }
}

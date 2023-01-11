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

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.asharov.universityschedule.model.Course;
import ua.com.foxminded.asharov.universityschedule.model.Group;
import ua.com.foxminded.asharov.universityschedule.model.Lecture;
import ua.com.foxminded.asharov.universityschedule.model.Room;
import ua.com.foxminded.asharov.universityschedule.model.Student;
import ua.com.foxminded.asharov.universityschedule.model.Teacher;
import ua.com.foxminded.asharov.universityschedule.model.Adapter;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.LectureService;
import ua.com.foxminded.asharov.universityschedule.service.RoomService;
import ua.com.foxminded.asharov.universityschedule.service.StudentService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

@WebMvcTest(controllers = StudentsController.class)
class StudentsControllerTest {

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
    StudentsController studentController;

    @Test
    void testShowAll_shouldShowList_whenRun() throws Exception {

        when(studentServ.retrieveAll())
                .thenReturn(Arrays.asList(new Student(10001L, 10001L, "FirstName1", "FirstName1"),
                        new Student(10002L, 10002L, "FirstName2", "FirstName2"),
                        new Student(10003L, 10002L, "FirstName3", "FirstName3")));

        mockMvc.perform(get("/students")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/selection"))
                .andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("FirstName2")))
                .andExpect(content().string(containsString("FirstName3")));
    }

    @Test
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        when(studentServ.retrieveById(10001L))
                .thenReturn(new Student(10001L, 10001L, "FirstName1", "FirstName1"));
        when(groupServ.retrieveGroupByStudentId(10001L)).thenReturn(new Group(10001L, "Group1"));

        mockMvc.perform(get("/students/{id}", "10001")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/dashboard"))
                .andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("Group1")));
    }



    @Test
    void testInviteNew() throws Exception {
        mockMvc.perform(get("/students/new")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/newbie"));
    }

    @Test
    void testLoad() throws Exception {
        when(studentServ.enter(new Student(null, 10001L, "name", "surname")))
                .thenReturn(new Student(9999L, 10001L, "name", "surname"));

        mockMvc.perform(post("/students").param("firstName", "name")
                .param("lastName", "surname").param("groupId", "10001")).andExpect(status().is(302))
                .andExpect(view().name("redirect:students/" + 9999L));
    }
    
    @Test
    void testModify() throws Exception {
        Long id = 10001L;

        when(studentServ.retrieveById(id)).thenReturn(new Student(id, 10001L, "name", "surname"));

        mockMvc.perform(get("/students/{id}/modify", id)).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/modification"))
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("surname")));
    }

    @Test
    void testReload() throws Exception {
        when(studentServ.enter(new Student(10001L, 10001L, "name", "surname")))
                .thenReturn(new Student(10001L, 10001L, "name", "surname"));
        
        mockMvc.perform(patch("/students")
                .param("id", "10001")
                .param("groupId", "10001")
                .param("firstName", "name")
                .param("lastName", "surname"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:students/"+10001L));
    }
    
    void testDelete() throws Exception {
        mockMvc.perform(delete("/students/{id}", 10001L))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/students"));
        verify(teacherServ).removeById(10001L);
    }
}

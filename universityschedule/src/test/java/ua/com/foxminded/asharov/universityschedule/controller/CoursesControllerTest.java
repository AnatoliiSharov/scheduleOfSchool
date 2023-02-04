package ua.com.foxminded.asharov.universityschedule.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.asharov.universityschedule.dto.CourseDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.service.*;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    MapperUtil mapperUtil;
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CoursesController courseController;

    @Test
    void testShowAll_shouldShowList_whenRun() throws Exception {

        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "name1", "description1"),
                new Course(10001L, "name2", "description2"), new Course(10001L, "name3", "description3")));

        mockMvc.perform(get("/courses")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/selection")).andExpect(content().string(containsString("name1")))
                .andExpect(content().string(containsString("name2")))
                .andExpect(content().string(containsString("name3")));
    }

    @Test
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        when(courseServ.retrieveById(10001L)).thenReturn(new Course(10001L, "Course1", "Description1"));
        when(teacherServ.retrieveAccreditedTeachers(10001L))
                .thenReturn(Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                        new Teacher(10002L, "teacherFirstName2", "teacherLastName2")));

        mockMvc.perform(get("/courses/{id}", "10001")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/dashboard")).andExpect(content().string(containsString("Course1")))
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
    void testLoad_shouldOk_whenCorrectValidation() throws Exception {
        when(courseServ.enter(new Course(null, "Course1", "Description1")))
                .thenReturn(new Course(10001L, "Course1", "Description1"));
        when(mapperUtil.toEntity(new CourseDto(null, "Course1", "Description1")))
        .thenReturn(new Course(null, "Course1", "Description1"));

        mockMvc.perform(post("/courses").param("name", "Course1").param("description", "Description1"))
                .andExpect(status().is(302)).andExpect(view().name("redirect:courses/" + 10001L));
    }
    
    @ParameterizedTest
    @CsvSource(value = {"'1', '' , The length of Courses cannot be less than 5 and more than 30 symbols., Courses name cannot be empty., DESCRIPTION_OF_COURSE , '', /courses/modification",
            "'1', 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' , '', The length of Courses cannot be less than 5 and more than 30 symbols, DESCRIPTION_OF_COURSE , '', /courses/modification",
            "'1', 'NAME_OF_COURSE' , '', '', '', Courses description cannot be empty., /courses/modification",
            "'', '' , The length of Courses cannot be less than 5 and more than 30 symbols., Courses name cannot be empty., DESCRIPTION_OF_COURSE , '', /courses/newbie",
            "'', 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' , '', The length of Courses cannot be less than 5 and more than 30 symbols, DESCRIPTION_OF_COURSE , '', /courses/newbie",
            "'', 'NAME_OF_COURSE' , '', '', '', Courses description cannot be empty., /courses/newbie"})
    void _shouldTryAgain_whenWrongValidation(String courseId, String enteredName, String nameMessadgeOne, String nameMessadgeTwo, String enteredDescription, String descriptionMessadge, String path) throws Exception {
        
        mockMvc.perform(post("/courses").param("id", courseId).param("name", enteredName).param("description", enteredDescription))
        .andExpect(status().isOk()).andExpect(view().name(path))
        .andExpect(content().string(containsString(enteredName)))
        .andExpect(content().string(containsString(nameMessadgeOne)))
        .andExpect(content().string(containsString(nameMessadgeTwo)))
        .andExpect(content().string(containsString(nameMessadgeOne)))
        .andExpect(content().string(containsString(descriptionMessadge)));
    }

    @Test
    void testModify() throws Exception {
        Long id = 10001L;

        when(courseServ.retrieveById(id)).thenReturn(new Course(10001L, "Course1", "Description1"));

        when(teacherServ.retrieveAll())
                .thenReturn(Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                        new Teacher(10002L, "teacherFirstName2", "teacherLastName2"),
                        new Teacher(10003L, "teacherFirstName3", "teacherLastName3")));
        when(teacherServ.retrieveAccreditedTeachers(id))
                .thenReturn(Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                        new Teacher(10002L, "teacherFirstName2", "teacherLastName2")));

        mockMvc.perform(get("/courses/{id}/modify", id)).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/modification")).andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Description1")))
                .andExpect(content().string(containsString("t.teacherLastName2")))
                .andExpect(content().string(containsString("t.teacherLastName2")))
                .andExpect(content().string(containsString("t.teacherLastName3")));
    }

    @Test
    void testAddAccreditedCourse() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/courses/{id}/add", teacherId, courseId).param("teacherId", teacherId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(302))
                .andExpect(view().name("redirect:modify"));

        verify(courseServ).addAccreditedCourse(teacherId, courseId);
    }

    @Test
    void testRemoveAccreditedCourse() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/courses/{id}/remove", teacherId, courseId).param("teacherId", teacherId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(302))
                .andExpect(view().name("redirect:modify"));

        verify(courseServ).removeAccreditedCourse(teacherId, courseId);
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/courses/{id}", 10001L)).andExpect(status().is(302))
                .andExpect(view().name("redirect:/courses"));
        verify(courseServ).removeById(10001L);
    }
}

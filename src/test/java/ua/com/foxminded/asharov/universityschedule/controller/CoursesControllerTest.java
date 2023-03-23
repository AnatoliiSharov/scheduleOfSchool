package ua.com.foxminded.asharov.universityschedule.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.asharov.universityschedule.dto.CourseDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.exception.GlobalExceptionHandler;
import ua.com.foxminded.asharov.universityschedule.secur.conf.SecurityConfigTest;
import ua.com.foxminded.asharov.universityschedule.service.*;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = { CoursesController.class, GlobalExceptionHandler.class })
@Import(SecurityConfigTest.class)
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
    @WithUserDetails("testAdmin")
    void testShowAll_shouldShowList_whenRun() throws Exception {

        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "name1", "description1"),
                new Course(10001L, "name2", "description2"), new Course(10001L, "name3", "description3")));

        mockMvc.perform(get("/courses").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/selection")).andExpect(content().string(containsString("name1")))
                .andExpect(content().string(containsString("name2")))
                .andExpect(content().string(containsString("name3")));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        when(courseServ.retrieveById(10001L)).thenReturn(new Course(10001L, "Course1", "Description1"));
        when(teacherServ.retrieveAccreditedTeachers(10001L))
                .thenReturn(Arrays.asList(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                        new Teacher(10002L, "teacherFirstName2", "teacherLastName2")));

        mockMvc.perform(get("/courses/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/dashboard")).andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("teacherFirstName1")))
                .andExpect(content().string(containsString("teacherLastName1")))
                .andExpect(content().string(containsString("teacherFirstName2")))
                .andExpect(content().string(containsString("teacherLastName2")));
    }

    @Test
    @WithUserDetails("testStudent")
    void testInviteNew_shouldDenied_whenUserIsStudent() throws Exception {
        mockMvc.perform(get("/courses/new").with(csrf()).with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails("testStaff")
    void testInviteNew_shouldDenied_whenUserIsStaff() throws Exception {
        mockMvc.perform(get("/courses/new").with(csrf()).with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));
    }

    @Test
    @WithAnonymousUser
    void testInviteNew_shouldDenied_whenUserIsAnonymus() throws Exception {
        mockMvc.perform(get("/courses/new").with(csrf()).with(csrf())).andDo(print()).andExpect(status().is(401));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testInviteNew_shouldApproved_whenUserIsAdmin() throws Exception {
        mockMvc.perform(get("/courses/new").with(csrf()).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/newbie"));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testLoad_shouldOk_whenCorrectValidation() throws Exception {
        when(courseServ.enter(new Course(null, "Course1", "Description1")))
                .thenReturn(new Course(10001L, "Course1", "Description1"));
        when(mapperUtil.toEntity(new CourseDto(null, "Course1", "Description1")))
                .thenReturn(new Course(null, "Course1", "Description1"));

        mockMvc.perform(post("/courses").with(csrf()).param("name", "Course1").param("description", "Description1"))
                .andExpect(status().is(302)).andExpect(view().name("redirect:courses/" + 10001L));
    }

    @Test
    @WithAnonymousUser
    void testLoad_shouldDenied_whenUserIsAnonymousAndAnyValidation() throws Exception {
        String courseId = "any";
        String enteredName = "any";
        String enteredDescription = "any";

        mockMvc.perform(post("/courses").with(csrf()).param("id", courseId).param("name", enteredName)
                .param("description", enteredDescription)).andExpect(status().is(401));
    }

    @Test
    @WithUserDetails("testStudent")
    void testLoad_shouldDenied_whenUserIsStudentAndAnyValidation() throws Exception {
        String courseId = "any";
        String enteredName = "any";
        String enteredDescription = "any";

        mockMvc.perform(post("/courses").with(csrf()).param("id", courseId).param("name", enteredName)
                .param("description", enteredDescription)).andExpect(status().is(403)).andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails("testStaff")
    void testLoad_shouldDenied_whenUserIsStaffAndAnyValidation() throws Exception {
        String courseId = "any";
        String enteredName = "any";
        String enteredDescription = "any";

        mockMvc.perform(post("/courses").with(csrf()).param("id", courseId).param("name", enteredName)
                .param("description", enteredDescription)).andExpect(status().is(403)).andExpect(view().name("error"));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'1', '' , The length of Courses cannot be less than 5 and more than 30 symbols., Courses name cannot be empty., DESCRIPTION_OF_COURSE , '', /courses/modification",
            "'1', 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' , '', The length of Courses cannot be less than 5 and more than 30 symbols, DESCRIPTION_OF_COURSE , '', /courses/modification",
            "'1', 'NAME_OF_COURSE' , '', '', '', Courses description cannot be empty., /courses/modification",
            "'', '' , The length of Courses cannot be less than 5 and more than 30 symbols., Courses name cannot be empty., DESCRIPTION_OF_COURSE , '', /courses/newbie",
            "'', 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' , '', The length of Courses cannot be less than 5 and more than 30 symbols, DESCRIPTION_OF_COURSE , '', /courses/newbie",
            "'', 'NAME_OF_COURSE' , '', '', '', Courses description cannot be empty., /courses/newbie" })
    @WithUserDetails("testAdmin")
    void testLoad_shouldTryAgain_whenWrongValidation(String courseId, String enteredName, String nameMessadgeOne,
            String nameMessadgeTwo, String enteredDescription, String descriptionMessadge, String path)
            throws Exception {

        mockMvc.perform(post("/courses").with(csrf()).param("id", courseId).param("name", enteredName)
                .param("description", enteredDescription)).andExpect(status().isOk()).andExpect(view().name(path))
                .andExpect(content().string(containsString(enteredName)))
                .andExpect(content().string(containsString(nameMessadgeOne)))
                .andExpect(content().string(containsString(nameMessadgeTwo)))
                .andExpect(content().string(containsString(nameMessadgeOne)))
                .andExpect(content().string(containsString(descriptionMessadge)));
    }

    @Test
    @WithUserDetails("testAdmin")
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

        mockMvc.perform(get("/courses/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/courses/modification")).andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Description1")))
                .andExpect(content().string(containsString("t.teacherLastName2")))
                .andExpect(content().string(containsString("t.teacherLastName2")))
                .andExpect(content().string(containsString("t.teacherLastName3")));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testAddAccreditedCourse_shouldDenied_whenUserIsAnonymus() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/courses/{id}/add", teacherId, courseId).with(csrf())
                .param("teacherId", teacherId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(302)).andExpect(view().name("redirect:modify"));

        verify(courseServ).addAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithAnonymousUser
    void testRemoveAccreditedCourse_shouldDenied_whenUserAnonymous() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/courses/{id}/remove", teacherId, courseId).param("teacherId", teacherId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(403));

        verify(courseServ, times(0)).removeAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithMockUser(username = "userStudent", roles = "VIEWER")
    void testRemoveAccreditedCourse_shouldDenied_whenUserStudent() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/courses/{id}/remove", teacherId, courseId).param("teacherId", teacherId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(403));

        verify(courseServ, times(0)).removeAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithMockUser(username = "userStaff", roles = { "VIEWER", "MODERATOR" })
    void testRemoveAccreditedCourse_shouldDenied_whenUserStaff() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/courses/{id}/remove", teacherId, courseId).param("teacherId", teacherId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(403));

        verify(courseServ, times(0)).removeAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithMockUser(username = "userAdmin", roles = { "VIEWER", "MODERATOR", "CREATOR" })
    void testRemoveAccreditedCourse_shouldApproved_whenUserAdmin() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/courses/{id}/remove", teacherId, courseId).with(csrf())
                .param("teacherId", teacherId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(302)).andExpect(view().name("redirect:modify"));

        verify(courseServ).removeAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithAnonymousUser
    void testDelete_shouldDenided_whenAnonymous() throws Exception {
        mockMvc.perform(delete("/courses/{id}", 10001L)).andExpect(status().is(403));
        verify(courseServ, times(0)).removeById(10001L);
    }

    @Test
    @WithMockUser(username = "userStudent", roles = "VIEWER")
    void testDelete_shouldDenied_whenUserStudent() throws Exception {
        mockMvc.perform(delete("/courses/{id}", 10001L)).andExpect(status().is(403));
        verify(courseServ, times(0)).removeById(10001L);
    }

    @Test
    @WithMockUser(username = "userStaff", roles = { "VIEWER", "MODERATOR" })
    void testDelete_shouldDenied_whenUserStaff() throws Exception {
        mockMvc.perform(delete("/courses/{id}", 10001L)).andExpect(status().is(403));
        verify(courseServ, times(0)).removeById(10001L);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testDelete_shouldApproved_whenUserAdmin() throws Exception {
        mockMvc.perform(delete("/courses/{id}", 10001L).with(csrf())).andExpect(status().is(302))
                .andExpect(view().name("redirect:/courses"));
        verify(courseServ).removeById(10001L);
    }

}

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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.asharov.universityschedule.dto.TeacherDto;
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

import static ua.com.foxminded.asharov.universityschedule.dto.TeacherDto.MAX_LENGH_TEACHER_NAME;
import static ua.com.foxminded.asharov.universityschedule.dto.TeacherDto.MAX_LENGH_TEACHER_SURNAME;

@WebMvcTest(controllers = { TeachersController.class, GlobalExceptionHandler.class })
@Import(SecurityConfigTest.class)
class TeachersControllerTest {

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
    TeachersController teachersController;

    @Test
    @WithUserDetails("testAdmin")
    void testShowAll_shouldShowList_whenRunAsAdmin() throws Exception {

        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(
                new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "FirstName2", "LastName2"), new Teacher(10003L, "Name3", "teacherLastName3")));

        mockMvc.perform(get("/teachers").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/selection"))
                .andExpect(content().string(containsString("t.teacherLastName1")))
                .andExpect(content().string(containsString("F.LastName2")))
                .andExpect(content().string(containsString("N.teacherLastName3")));

        verify(teacherServ, times(1)).retrieveAll();
    }

    @Test
    @WithAnonymousUser
    void testShowAll_shouldShowList_whenRunAsAnonymous() throws Exception {

        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(
                new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "FirstName2", "LastName2"), new Teacher(10003L, "Name3", "teacherLastName3")));

        mockMvc.perform(get("/teachers").with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(teacherServ, times(0)).retrieveAll();
    }

    @Test
    @WithUserDetails("testStudent")
    void testShowAll_shouldShowList_whenRunAsStudent() throws Exception {

        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(
                new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "FirstName2", "LastName2"), new Teacher(10003L, "Name3", "teacherLastName3")));

        mockMvc.perform(get("/teachers").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/selection"))
                .andExpect(content().string(containsString("t.teacherLastName1")))
                .andExpect(content().string(containsString("F.LastName2")))
                .andExpect(content().string(containsString("N.teacherLastName3")));

        verify(teacherServ, times(1)).retrieveAll();
    }

    @Test
    @WithUserDetails("testStaff")
    void testShowAll_shouldShowList_whenRunAsStaff() throws Exception {

        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(
                new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "FirstName2", "LastName2"), new Teacher(10003L, "Name3", "teacherLastName3")));

        mockMvc.perform(get("/teachers").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/selection"))
                .andExpect(content().string(containsString("t.teacherLastName1")))
                .andExpect(content().string(containsString("F.LastName2")))
                .andExpect(content().string(containsString("N.teacherLastName3")));

        verify(teacherServ, times(1)).retrieveAll();
    }

    @Test
    @WithUserDetails("testAdmin")
    void testshowSelected_shouldSelected_whenRunAsAdmin() throws Exception {
        when(teacherServ.retrieveById(10001L)).thenReturn(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"));
        when(courseServ.retrieveByAccreditedTeacher(10001L)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10002L, "Course2", "Description2")));

        mockMvc.perform(get("/teachers/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/dashboard"))
                .andExpect(content().string(containsString("teacherFirstName1")))
                .andExpect(content().string(containsString("teacherLastName1")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")));

        verify(teacherServ, times(1)).retrieveById(10001L);
        verify(courseServ, times(1)).retrieveByAccreditedTeacher(10001L);
    }

    @Test
    @WithAnonymousUser
    void testshowSelected_shouldSelected_whenRunAsAnonymous() throws Exception {
        when(teacherServ.retrieveById(10001L)).thenReturn(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"));
        when(courseServ.retrieveByAccreditedTeacher(10001L)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10002L, "Course2", "Description2")));

        mockMvc.perform(get("/teachers/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(teacherServ, times(0)).retrieveById(10001L);
        verify(courseServ, times(0)).retrieveByAccreditedTeacher(10001L);
    }

    @Test
    @WithUserDetails("testStudent")
    void testshowSelected_shouldSelected_whenRunAsStudent() throws Exception {
        when(teacherServ.retrieveById(10001L)).thenReturn(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"));
        when(courseServ.retrieveByAccreditedTeacher(10001L)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10002L, "Course2", "Description2")));

        mockMvc.perform(get("/teachers/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/dashboard"))
                .andExpect(content().string(containsString("teacherFirstName1")))
                .andExpect(content().string(containsString("teacherLastName1")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")));

        verify(teacherServ, times(1)).retrieveById(10001L);
        verify(courseServ, times(1)).retrieveByAccreditedTeacher(10001L);
    }

    @Test
    @WithUserDetails("testStaff")
    void testshowSelected_shouldSelected_whenRunAsStaff() throws Exception {
        when(teacherServ.retrieveById(10001L)).thenReturn(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"));
        when(courseServ.retrieveByAccreditedTeacher(10001L)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10002L, "Course2", "Description2")));

        mockMvc.perform(get("/teachers/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/dashboard"))
                .andExpect(content().string(containsString("teacherFirstName1")))
                .andExpect(content().string(containsString("teacherLastName1")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")));

        verify(teacherServ, times(1)).retrieveById(10001L);
        verify(courseServ, times(1)).retrieveByAccreditedTeacher(10001L);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testInviteNew_shouldAccessed_whenAdmin() throws Exception {
        mockMvc.perform(get("/teachers/new").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/newbie"));
    }

    @Test
    @WithAnonymousUser
    void testInviteNew_shouldDenied_whenAnonymous() throws Exception {
        mockMvc.perform(get("/teachers/new").with(csrf())).andDo(print()).andExpect(status().is(401));
    }

    @Test
    @WithUserDetails("testStudent")
    void testInviteNew_shouldDenied_whenStudent() throws Exception {
        mockMvc.perform(get("/teachers/new").with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails("testStaff")
    void testInviteNew_shouldDenied_whenStaff() throws Exception {
        mockMvc.perform(get("/teachers/new").with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testLoad_shouldOk_whenCorrectValidation() throws Exception {
        when(teacherServ.enter(new Teacher(null, "newName", "newSurname")))
                .thenReturn(new Teacher(9999L, "newName", "newSurname"));
        when(mapperUtil.toEntity(new TeacherDto(null, "newName", "newSurname")))
                .thenReturn(new Teacher(null, "newName", "newSurname"));

        mockMvc.perform(post("/teachers").with(csrf()).param("firstName", "newName").param("lastName", "newSurname"))
                .andExpect(status().is(302)).andExpect(view().name("redirect:teachers/" + 9999L));

        verify(teacherServ, times(1)).enter(new Teacher(null, "newName", "newSurname"));
        verify(mapperUtil, times(1)).toEntity(new TeacherDto(null, "newName", "newSurname"));
    }

    @Test
    @WithAnonymousUser
    void testLoad_shouldDenied_whenAnyValidationAndAnonymous() throws Exception {
        when(teacherServ.enter(new Teacher(null, "newName", "newSurname")))
                .thenReturn(new Teacher(9999L, "newName", "newSurname"));
        when(mapperUtil.toEntity(new TeacherDto(null, "newName", "newSurname")))
                .thenReturn(new Teacher(null, "newName", "newSurname"));

        mockMvc.perform(post("/teachers").with(csrf()).param("firstName", "newName").param("lastName", "newSurname"))
                .andExpect(status().is(401));

        verify(teacherServ, times(0)).enter(new Teacher(null, "newName", "newSurname"));
        verify(mapperUtil, times(0)).toEntity(new TeacherDto(null, "newName", "newSurname"));
    }

    @Test
    @WithUserDetails("testStudent")
    void testLoad_shouldDenied_whenAnyValidationAndStudent() throws Exception {
        when(teacherServ.enter(new Teacher(null, "newName", "newSurname")))
                .thenReturn(new Teacher(9999L, "newName", "newSurname"));
        when(mapperUtil.toEntity(new TeacherDto(null, "newName", "newSurname")))
                .thenReturn(new Teacher(null, "newName", "newSurname"));

        mockMvc.perform(post("/teachers").with(csrf()).param("firstName", "newName").param("lastName", "newSurname"))
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(teacherServ, times(0)).enter(new Teacher(null, "newName", "newSurname"));
        verify(mapperUtil, times(0)).toEntity(new TeacherDto(null, "newName", "newSurname"));
    }

    @Test
    @WithUserDetails("testStaff")
    void testLoad_shouldDenied_whenAnyValidationAndStaff() throws Exception {
        when(teacherServ.enter(new Teacher(null, "newName", "newSurname")))
                .thenReturn(new Teacher(9999L, "newName", "newSurname"));
        when(mapperUtil.toEntity(new TeacherDto(null, "newName", "newSurname")))
                .thenReturn(new Teacher(null, "newName", "newSurname"));

        mockMvc.perform(post("/teachers").with(csrf()).param("firstName", "newName").param("lastName", "newSurname"))
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(teacherServ, times(0)).enter(new Teacher(null, "newName", "newSurname"));
        verify(mapperUtil, times(0)).toEntity(new TeacherDto(null, "newName", "newSurname"));
    }

    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @CsvSource(value = {
            "'1', '', 'Teachers first name cannot be empty', '', 'TEACHERS_LAST_NAME', '', '', '/teachers/modification' ",
            "'1', 'TEACHERS_NAME_MORE_THEN_MAX', '', 'The length of Teachers first name cannot be more than 1478', 'TEACHERS_LAST_NAME', '', '', '/teachers/modification' ",
            "'1', 'CORRECT_TEACHERS_NAME', '', '', '', 'Teachers last name cannot be empty', '', '/teachers/modification' ",
            "'1', 'CORRECT_TEACHERS_NAME', '', '', 'TEACHERS_SURNAME_MORE_THEN_MAX', '', 'The length of Teachers last name cannot be more than 700', '/teachers/modification' ",
            "'', '', 'Teachers first name cannot be empty', '', 'TEACHERS_LAST_NAME', '', '', '/teachers/newbie' ",
            "'', 'TEACHERS_NAME_MORE_THEN_MAX', '', 'The length of Teachers first name cannot be more than 1478', 'TEACHERS_LAST_NAME', '', '', '/teachers/newbie' ",
            "'', 'CORRECT_TEACHERS_NAME', '', '', '', 'Teachers last name cannot be empty', '', '/teachers/newbie' ",
            "'', 'CORRECT_TEACHERS_NAME', '', '', 'TEACHERS_SURNAME_MORE_THEN_MAX', '', 'The length of Teachers last name cannot be more than 700', '/teachers/newbie' " })
    void testLoad_shouldTryAgain_whenWrongValidationAndAdmin(String teacherId, String enteredName,
            String nameEmptyMessage, String nameLengthMessage, String enteredSurname, String surnameEmptyMessadge,
            String surnameLengthMessadge, String path) throws Exception {

        if (enteredName != null && enteredName.equals("TEACHERS_NAME_MORE_THEN_MAX")) {
            enteredName = makeLongName(MAX_LENGH_TEACHER_NAME);
        }

        if (enteredSurname != null && enteredSurname.equals("TEACHERS_SURNAME_MORE_THEN_MAX")) {
            enteredSurname = makeLongName(MAX_LENGH_TEACHER_SURNAME);
        }

        mockMvc.perform(post("/teachers").with(csrf()).param("id", teacherId).param("firstName", enteredName)
                .param("lastName", enteredSurname)).andExpect(status().isOk()).andExpect(view().name(path))
                .andExpect(content().string(containsString(enteredName)))
                .andExpect(content().string(containsString(nameEmptyMessage)))
                .andExpect(content().string(containsString(nameLengthMessage)))
                .andExpect(content().string(containsString(enteredSurname)))
                .andExpect(content().string(containsString(surnameEmptyMessadge)))
                .andExpect(content().string(containsString(surnameLengthMessadge)));
    }

    private String makeLongName(int length) {
        String name = "A";

        while (name.length() <= length) {
            name = name.concat("A");
        }
        return name;
    }

    @Test
    @WithUserDetails("testAdmin")
    void testModify_shouldAccessed_when() throws Exception {
        Long id = 10001L;

        when(teacherServ.retrieveById(id)).thenReturn(new Teacher(id, "name", "surname"));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        when(courseServ.retrieveByAccreditedTeacher(id)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3")));

        mockMvc.perform(get("/teachers/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/modification")).andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("surname")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")))
                .andExpect(content().string(containsString("Course3")));

        verify(teacherServ, times(1)).retrieveById(id);
        verify(courseServ, times(1)).retrieveAll();
        verify(courseServ, times(1)).retrieveByAccreditedTeacher(id);
    }

    @Test
    @WithAnonymousUser
    void testModify_shouldDenied_whenAnonymous() throws Exception {
        Long id = 10001L;

        when(teacherServ.retrieveById(id)).thenReturn(new Teacher(id, "name", "surname"));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        when(courseServ.retrieveByAccreditedTeacher(id)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3")));

        mockMvc.perform(get("/teachers/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(teacherServ, times(0)).retrieveById(id);
        verify(courseServ, times(0)).retrieveAll();
        verify(courseServ, times(0)).retrieveByAccreditedTeacher(id);
    }

    @Test
    @WithUserDetails("testStudent")
    void testModify_shouldDenied_whenStudent() throws Exception {
        Long id = 10001L;

        when(teacherServ.retrieveById(id)).thenReturn(new Teacher(id, "name", "surname"));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        when(courseServ.retrieveByAccreditedTeacher(id)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3")));

        mockMvc.perform(get("/teachers/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(teacherServ, times(0)).retrieveById(id);
        verify(courseServ, times(0)).retrieveAll();
        verify(courseServ, times(0)).retrieveByAccreditedTeacher(id);
    }

    @Test
    @WithUserDetails("testStaff")
    void testModify_shouldDenied_whenStaff() throws Exception {
        Long id = 10001L;

        when(teacherServ.retrieveById(id)).thenReturn(new Teacher(id, "name", "surname"));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        when(courseServ.retrieveByAccreditedTeacher(id)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3")));

        mockMvc.perform(get("/teachers/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(teacherServ, times(0)).retrieveById(id);
        verify(courseServ, times(0)).retrieveAll();
        verify(courseServ, times(0)).retrieveByAccreditedTeacher(id);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testAddAccreditedCourse_shouldAdmin() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/teachers/{id}/add", teacherId, courseId).with(csrf())
                .param("teacherId", teacherId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(302)).andExpect(view().name("redirect:modify"));

        verify(courseServ, times(1)).addAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithAnonymousUser
    void testAddAccreditedCourse_shouldDenied_whenAnonymous() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/teachers/{id}/add", teacherId, courseId).with(csrf())
                .param("teacherId", teacherId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(401));

        verify(courseServ, times(0)).addAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithUserDetails("testStudent")
    void testAddAccreditedCourse_shouldDenied_whenStudent() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/teachers/{id}/add", teacherId, courseId).with(csrf())
                .param("teacherId", teacherId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(courseServ, times(0)).addAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithUserDetails("testStaff")
    void testAddAccreditedCourse_shouldDenied_whenStaff() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/teachers/{id}/add", teacherId, courseId).with(csrf())
                .param("teacherId", teacherId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(courseServ, times(0)).addAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testRemoveAccreditedCourse_shouldAccessed_whenAdmin() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/teachers/{id}/remove", teacherId, courseId).with(csrf())
                .param("teacherId", teacherId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(302)).andExpect(view().name("redirect:modify"));

        verify(courseServ, times(1)).removeAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithAnonymousUser
    void testRemoveAccreditedCourse_shouldDenied_whenAnonymous() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/teachers/{id}/remove", teacherId, courseId).with(csrf())
                .param("teacherId", teacherId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(401));

        verify(courseServ, times(0)).removeAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithUserDetails("testStudent")
    void testRemoveAccreditedCourse_shouldDenied_whenStudent() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/teachers/{id}/remove", teacherId, courseId).with(csrf())
                .param("teacherId", teacherId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(courseServ, times(0)).removeAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithUserDetails("testStaff")
    void testRemoveAccreditedCourse_shouldDenied_whenStaff() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/teachers/{id}/remove", teacherId, courseId).with(csrf())
                .param("teacherId", teacherId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(courseServ, times(0)).removeAccreditedCourse(teacherId, courseId);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testDelete_shouldAccessed_whenAdmin() throws Exception {
        mockMvc.perform(delete("/teachers/{id}", 10001L).with(csrf())).andExpect(status().is(302))
                .andExpect(view().name("redirect:/teachers"));

        verify(teacherServ, times(1)).removeById(10001L);
    }

    @Test
    @WithAnonymousUser
    void testDelete_shouldDenied_whenAnonymous() throws Exception {
        mockMvc.perform(delete("/teachers/{id}", 10001L).with(csrf())).andExpect(status().is(401));

        verify(teacherServ, times(0)).removeById(10001L);
    }

    @Test
    @WithUserDetails("testStudent")
    void testDelete_shouldDenied_whenStudent() throws Exception {
        mockMvc.perform(delete("/teachers/{id}", 10001L).with(csrf())).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(teacherServ, times(0)).removeById(10001L);
    }

    @Test
    @WithUserDetails("testStaff")
    void testDelete_shouldDenied_whenStaff() throws Exception {
        mockMvc.perform(delete("/teachers/{id}", 10001L).with(csrf())).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(teacherServ, times(0)).removeById(10001L);
    }
}

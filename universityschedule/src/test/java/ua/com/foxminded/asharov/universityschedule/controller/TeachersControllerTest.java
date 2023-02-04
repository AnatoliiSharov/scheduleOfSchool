package ua.com.foxminded.asharov.universityschedule.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.asharov.universityschedule.dto.TeacherDto;
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

import static ua.com.foxminded.asharov.universityschedule.dto.TeacherDto.MAX_LENGH_TEACHER_NAME;
import static ua.com.foxminded.asharov.universityschedule.dto.TeacherDto.MAX_LENGH_TEACHER_SURNAME;

@WebMvcTest(controllers = TeachersController.class)
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
    void testShowAll_shouldShowList_whenRun() throws Exception {

        when(teacherServ.retrieveAll()).thenReturn(Arrays.asList(
                new Teacher(10001L, "teacherFirstName1", "teacherLastName1"),
                new Teacher(10002L, "FirstName2", "LastName2"), new Teacher(10003L, "Name3", "teacherLastName3")));

        mockMvc.perform(get("/teachers")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/selection"))
                .andExpect(content().string(containsString("t.teacherLastName1")))
                .andExpect(content().string(containsString("F.LastName2")))
                .andExpect(content().string(containsString("N.teacherLastName3")));
    }

    @Test
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        when(teacherServ.retrieveById(10001L)).thenReturn(new Teacher(10001L, "teacherFirstName1", "teacherLastName1"));
        when(courseServ.retrieveByAccreditedTeacher(10001L)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10002L, "Course2", "Description2")));

        mockMvc.perform(get("/teachers/{id}", "10001")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/dashboard"))
                .andExpect(content().string(containsString("teacherFirstName1")))
                .andExpect(content().string(containsString("teacherLastName1")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")));
    }

    @Test
    void testInviteNew() throws Exception {
        mockMvc.perform(get("/teachers/new")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/newbie"));
    }

    @Test
    void testLoad_shouldOk_whenCorrectValidation() throws Exception {
        when(teacherServ.enter(new Teacher(null, "newName", "newSurname")))
                .thenReturn(new Teacher(9999L, "newName", "newSurname"));
        when(mapperUtil.toEntity(new TeacherDto(null, "newName", "newSurname")))
                .thenReturn(new Teacher(null, "newName", "newSurname"));

        mockMvc.perform(post("/teachers").param("firstName", "newName").param("lastName", "newSurname"))
                .andExpect(status().is(302)).andExpect(view().name("redirect:teachers/" + 9999L));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'1', '', 'Teachers first name cannot be empty', '', 'TEACHERS_LAST_NAME', '', '', '/teachers/modification' ",
            "'1', 'TEACHERS_NAME_MORE_THEN_MAX', '', 'The length of Teachers first name cannot be more than 1478', 'TEACHERS_LAST_NAME', '', '', '/teachers/modification' ",
            "'1', 'CORRECT_TEACHERS_NAME', '', '', '', 'Teachers last name cannot be empty', '', '/teachers/modification' ",
            "'1', 'CORRECT_TEACHERS_NAME', '', '', 'TEACHERS_SURNAME_MORE_THEN_MAX', '', 'The length of Teachers last name cannot be more than 700', '/teachers/modification' ",
            "'', '', 'Teachers first name cannot be empty', '', 'TEACHERS_LAST_NAME', '', '', '/teachers/newbie' ",
            "'', 'TEACHERS_NAME_MORE_THEN_MAX', '', 'The length of Teachers first name cannot be more than 1478', 'TEACHERS_LAST_NAME', '', '', '/teachers/newbie' ",
            "'', 'CORRECT_TEACHERS_NAME', '', '', '', 'Teachers last name cannot be empty', '', '/teachers/newbie' ",
            "'', 'CORRECT_TEACHERS_NAME', '', '', 'TEACHERS_SURNAME_MORE_THEN_MAX', '', 'The length of Teachers last name cannot be more than 700', '/teachers/newbie' " })
    void testLoad_shouldTryAgain_whenWrongValidation(String teacherId, String enteredName, String nameEmptyMessage,
            String nameLengthMessage, String enteredSurname, String surnameEmptyMessadge, String surnameLengthMessadge,
            String path) throws Exception {

        if (enteredName != null && enteredName.equals("TEACHERS_NAME_MORE_THEN_MAX")) {
            enteredName = makeLongName(MAX_LENGH_TEACHER_NAME);
        }

        if (enteredSurname != null && enteredSurname.equals("TEACHERS_SURNAME_MORE_THEN_MAX")) {
            enteredSurname = makeLongName(MAX_LENGH_TEACHER_SURNAME);
        }

        mockMvc.perform(post("/teachers").param("id", teacherId).param("firstName", enteredName).param("lastName",
                enteredSurname)).andExpect(status().isOk()).andExpect(view().name(path))
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
    void testModify() throws Exception {
        Long id = 10001L;

        when(teacherServ.retrieveById(id)).thenReturn(new Teacher(id, "name", "surname"));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        when(courseServ.retrieveByAccreditedTeacher(id)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3")));

        mockMvc.perform(get("/teachers/{id}/modify", id)).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/teachers/modification")).andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("surname")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")))
                .andExpect(content().string(containsString("Course3")));
    }

    @Test
    void testAddAccreditedCourse() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/teachers/{id}/add", teacherId, courseId).param("teacherId", teacherId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(302))
                .andExpect(view().name("redirect:modify"));

        verify(courseServ).addAccreditedCourse(teacherId, courseId);
    }

    @Test
    void testRemoveAccreditedCourse() throws Exception {
        Long teacherId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/teachers/{id}/remove", teacherId, courseId).param("teacherId", teacherId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(302))
                .andExpect(view().name("redirect:modify"));

        verify(courseServ).removeAccreditedCourse(teacherId, courseId);
    }

    void testDelete() throws Exception {
        mockMvc.perform(delete("/teachers/{id}", 10001L)).andExpect(status().is(302))
                .andExpect(view().name("redirect:/teachers"));
        verify(teacherServ).removeById(10001L);
    }
}

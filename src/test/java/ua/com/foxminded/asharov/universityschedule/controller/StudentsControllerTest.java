package ua.com.foxminded.asharov.universityschedule.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.jupiter.api.BeforeEach;
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

import ua.com.foxminded.asharov.universityschedule.dto.StudentDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.Adapter;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Student;
import ua.com.foxminded.asharov.universityschedule.exception.GlobalExceptionHandler;
import ua.com.foxminded.asharov.universityschedule.secur.conf.SecurityConfigTest;
import ua.com.foxminded.asharov.universityschedule.service.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.asharov.universityschedule.dto.StudentDto.MAX_LENGH_STUDENTS_NAME;
import static ua.com.foxminded.asharov.universityschedule.dto.StudentDto.MAX_LENGH_STUDENTS_SURNAME;

@WebMvcTest(controllers = { StudentsController.class, GlobalExceptionHandler.class })
@Import(SecurityConfigTest.class)
class StudentsControllerTest {
    @MockBean
    GroupService groupServ;
    @MockBean
    TeacherService teacherServ;
    @MockBean
    StudentService studentServ;
    @MockBean
    Adapter lectureAdapt;
    @MockBean
    MapperUtil mapperUtil;
    @MockBean
    StudentDto studentDto;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    StudentsController studentController;

    Group[] groups;

    @BeforeEach
    void setUp() throws Exception {
        groups = new Group[] { new Group(1L, "Group1"), new Group(2L, "Group2") };
    }

    @Test
    @WithUserDetails("testAdmin")
    void testShowAll_shouldShowList_whenRunWithAdmin() throws Exception {
        List<Student> students = Arrays.asList(new Student(10001L, groups[0], "FirstName1", "LastName1"),
                new Student(10002L, groups[1], "FirstName2", "LastName2"),
                new Student(10003L, groups[1], "FirstName3", "LastName3"));

        when(studentServ.retrieveAll()).thenReturn(students);

        mockMvc.perform(get("/students").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/selection")).andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("FirstName2")))
                .andExpect(content().string(containsString("FirstName3")));

        verify(studentServ, times(1)).retrieveAll();
    }

    @Test
    @WithAnonymousUser
    void testShowAll_shouldShowList_whenRunWithAnonymous() throws Exception {
        List<Student> students = Arrays.asList(new Student(10001L, groups[0], "FirstName1", "LastName1"),
                new Student(10002L, groups[1], "FirstName2", "LastName2"),
                new Student(10003L, groups[1], "FirstName3", "LastName3"));

        when(studentServ.retrieveAll()).thenReturn(students);

        mockMvc.perform(get("/students").with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(studentServ, times(0)).retrieveAll();
    }

    @Test
    @WithUserDetails("testStudent")
    void testShowAll_shouldShowList_whenRunWithStudent() throws Exception {
        List<Student> students = Arrays.asList(new Student(10001L, groups[0], "FirstName1", "LastName1"),
                new Student(10002L, groups[1], "FirstName2", "LastName2"),
                new Student(10003L, groups[1], "FirstName3", "LastName3"));

        when(studentServ.retrieveAll()).thenReturn(students);

        mockMvc.perform(get("/students").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/selection")).andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("FirstName2")))
                .andExpect(content().string(containsString("FirstName3")));

        verify(studentServ, times(1)).retrieveAll();
    }

    @Test
    @WithUserDetails("testStaff")
    void testShowAll_shouldShowList_whenRunWithStaff() throws Exception {
        List<Student> students = Arrays.asList(new Student(10001L, groups[0], "FirstName1", "LastName1"),
                new Student(10002L, groups[1], "FirstName2", "LastName2"),
                new Student(10003L, groups[1], "FirstName3", "LastName3"));

        when(studentServ.retrieveAll()).thenReturn(students);

        mockMvc.perform(get("/students").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/selection")).andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("FirstName2")))
                .andExpect(content().string(containsString("FirstName3")));

        verify(studentServ, times(1)).retrieveAll();
    }

    @Test
    @WithUserDetails("testStudent")
    void testshowSelected_shouldSelected_whenRunWithStudent() throws Exception {
        Group group = new Group(99L, "Group1");
        Student student = new Student(10001L, group, "FirstName1", "LastName1");

        when(studentServ.retrieveById(10001L)).thenReturn(student);
        when(groupServ.retrieveGroupByStudentId(10001L)).thenReturn(group);

        mockMvc.perform(get("/students/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/dashboard")).andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("LastName1")))
                .andExpect(content().string(containsString("Group1")));

        verify(studentServ, times(1)).retrieveById(10001L);
        verify(groupServ, times(1)).retrieveGroupByStudentId(10001L);
    }

    @Test
    @WithUserDetails("testStaff")
    void testshowSelected_shouldSelected_whenRunWithStaff() throws Exception {
        Group group = new Group(99L, "Group1");
        Student student = new Student(10001L, group, "FirstName1", "LastName1");

        when(studentServ.retrieveById(10001L)).thenReturn(student);
        when(groupServ.retrieveGroupByStudentId(10001L)).thenReturn(group);

        mockMvc.perform(get("/students/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/dashboard")).andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("LastName1")))
                .andExpect(content().string(containsString("Group1")));

        verify(studentServ, times(1)).retrieveById(10001L);
        verify(groupServ, times(1)).retrieveGroupByStudentId(10001L);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testshowSelected_shouldSelected_whenRunWithAdmin() throws Exception {
        Group group = new Group(99L, "Group1");
        Student student = new Student(10001L, group, "FirstName1", "LastName1");

        when(studentServ.retrieveById(10001L)).thenReturn(student);
        when(groupServ.retrieveGroupByStudentId(10001L)).thenReturn(group);

        mockMvc.perform(get("/students/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/dashboard")).andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("LastName1")))
                .andExpect(content().string(containsString("Group1")));

        verify(studentServ, times(1)).retrieveById(10001L);
        verify(groupServ, times(1)).retrieveGroupByStudentId(10001L);
    }

    @Test
    @WithAnonymousUser
    void testshowSelected_shouldSelected_whenRunWithAnonymous() throws Exception {
        Group group = new Group(99L, "Group1");
        Student student = new Student(10001L, group, "FirstName1", "LastName1");

        when(studentServ.retrieveById(10001L)).thenReturn(student);
        when(groupServ.retrieveGroupByStudentId(10001L)).thenReturn(group);
        when(mapperUtil.toDto(student)).thenReturn(StudentDto.builder().id(10001L).groupId(group.getId())
                .firstName("FirstName1").lastName("LastName1").build());

        mockMvc.perform(get("/students/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(studentServ, times(0)).retrieveById(10001L);
        verify(groupServ, times(0)).retrieveGroupByStudentId(10001L);
        verify(mapperUtil, times(0)).toDto(student);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testInviteNew_shouldAccessed_when() throws Exception {
        mockMvc.perform(get("/students/new").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/newbie"));
    }

    @Test
    @WithAnonymousUser
    void testInviteNew_shouldDenied_whenAnonymous() throws Exception {
        mockMvc.perform(get("/students/new").with(csrf())).andDo(print()).andExpect(status().is(401));
    }

    @Test
    @WithUserDetails("testStudent")
    void testInviteNew_shouldDenied_whenStudent() throws Exception {
        mockMvc.perform(get("/students/new").with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails("testStaff")
    void testInviteNew_shouldDenied_whenStaff() throws Exception {
        mockMvc.perform(get("/students/new").with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testLoad_shouldOk_whenCorrectValidationAndAdmin() throws Exception {
        when(studentServ.enter(new Student(null, groups[0], "name", "surname")))
                .thenReturn(new Student(9999L, groups[0], "name", "surname"));
        when(mapperUtil.toEntity(
                StudentDto.builder().groupId(groups[0].getId()).firstName("name").lastName("surname").build()))
                .thenReturn(new Student(null, groups[0], "name", "surname"));

        mockMvc.perform(post("/students").with(csrf()).param("firstName", "name").param("lastName", "surname")
                .param("groupId", "1")).andExpect(status().is(302))
                .andExpect(view().name("redirect:students/" + 9999L));

        verify(studentServ, times(1)).enter(new Student(null, groups[0], "name", "surname"));
        verify(mapperUtil, times(1)).toEntity(
                StudentDto.builder().groupId(groups[0].getId()).firstName("name").lastName("surname").build());
    }

    @Test
    @WithAnonymousUser
    void testLoad_shouldOk_whenAnyValidationAndAnonymous() throws Exception {
        when(studentServ.enter(new Student(null, groups[0], "name", "surname")))
                .thenReturn(new Student(9999L, groups[0], "name", "surname"));
        when(mapperUtil.toEntity(
                StudentDto.builder().groupId(groups[0].getId()).firstName("name").lastName("surname").build()))
                .thenReturn(new Student(null, groups[0], "name", "surname"));

        mockMvc.perform(post("/students").with(csrf()).param("firstName", "name").param("lastName", "surname")
                .param("groupId", "1")).andExpect(status().is(401));

        verify(studentServ, times(0)).enter(new Student(null, groups[0], "name", "surname"));
        verify(mapperUtil, times(0)).toEntity(
                StudentDto.builder().groupId(groups[0].getId()).firstName("name").lastName("surname").build());
    }

    @Test
    @WithUserDetails("testStudent")
    void testLoad_shouldOk_whenAnyValidationAndStudent() throws Exception {
        when(studentServ.enter(new Student(null, groups[0], "name", "surname")))
                .thenReturn(new Student(9999L, groups[0], "name", "surname"));
        when(mapperUtil.toEntity(
                StudentDto.builder().groupId(groups[0].getId()).firstName("name").lastName("surname").build()))
                .thenReturn(new Student(null, groups[0], "name", "surname"));

        mockMvc.perform(post("/students").with(csrf()).param("firstName", "name").param("lastName", "surname")
                .param("groupId", "1")).andExpect(status().is(403)).andExpect(view().name("error"));

        verify(studentServ, times(0)).enter(new Student(null, groups[0], "name", "surname"));
        verify(mapperUtil, times(0)).toEntity(
                StudentDto.builder().groupId(groups[0].getId()).firstName("name").lastName("surname").build());
    }

    @Test
    @WithUserDetails("testStaff")
    void testLoad_shouldOk_whenAnyValidationAndStaff() throws Exception {
        when(studentServ.enter(new Student(null, groups[0], "name", "surname")))
                .thenReturn(new Student(9999L, groups[0], "name", "surname"));
        when(mapperUtil.toEntity(
                StudentDto.builder().groupId(groups[0].getId()).firstName("name").lastName("surname").build()))
                .thenReturn(new Student(null, groups[0], "name", "surname"));

        mockMvc.perform(post("/students").with(csrf()).param("firstName", "name").param("lastName", "surname")
                .param("groupId", "1")).andExpect(status().is(403)).andExpect(view().name("error"));

        verify(studentServ, times(0)).enter(new Student(null, groups[0], "name", "surname"));
        verify(mapperUtil, times(0)).toEntity(
                StudentDto.builder().groupId(groups[0].getId()).firstName("name").lastName("surname").build());
    }

    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @CsvSource(value = {
            "'1', '1', '', '', 'Students first name cannot be empty', '', 'STUDENTS_LAST_NAME', '', '', '/students/modification' ",
            "'1', '1', '', 'STUDENTS_NAME_MORE_THEN_MAX', '', 'The length of Students first name cannot be more than 1478', 'STUDENTS_LAST_NAME', '', '', '/students/modification' ",
            "'1', '1', '', 'CORRECT_STUDENTS_NAME', '', '', '', 'Students last name cannot be empty', '', '/students/modification' ",
            "'1', '1', '', 'CORRECT_STUDENTS_NAME', '', '', 'STUDENTS_SURNAME_MORE_THEN_MAX', '', 'The length of Students last name cannot be more than 700', '/students/modification' ",
            "'1', '', 'Rooms capacity cannot be null', 'CORRECT_STUDENTS_NAME', '', '', 'CORRECT_STUDENTS_SURNAME', '', '', '/students/modification' ",
            "'', '1', '', '', 'Students first name cannot be empty', '', 'STUDENTS_LAST_NAME', '', '', '/students/newbie' ",
            "'', '1', '', 'STUDENTS_NAME_MORE_THEN_MAX', '', 'The length of Students first name cannot be more than 1478', 'TEACHERS_LAST_NAME', '', '', '/students/newbie' ",
            "'', '1', '', 'CORRECT_STUDENTS_NAME', '', '', '', 'Students last name cannot be empty', '', '/students/newbie' ",
            "'', '1', '', 'CORRECT_STUDENTS_NAME', '', '', 'STUDENTS_SURNAME_MORE_THEN_MAX', '', 'The length of Students last name cannot be more than 700', '/students/newbie' ",
            "'', '', 'Rooms capacity cannot be null', 'CORRECT_STUDENTS_NAME', '', '', 'CORRECT_STUDENTS_SURNAME', '', '', '/students/newbie' " })
    void testLoad_shouldTryAgain_whenWrongValidationAndAdmin(String teacherId, String groupId,
            String groupIdNullMessage, String enteredName, String nameEmptyMessage, String nameLengthMessage,
            String enteredSurname, String surnameEmptyMessadge, String surnameLengthMessadge, String path)
            throws Exception {

        if (enteredName != null && enteredName.equals("STUDENTS_NAME_MORE_THEN_MAX")) {
            enteredName = makeLongName(MAX_LENGH_STUDENTS_NAME);
        }

        if (enteredSurname != null && enteredSurname.equals("STUDENTS_SURNAME_MORE_THEN_MAX")) {
            enteredSurname = makeLongName(MAX_LENGH_STUDENTS_SURNAME);
        }

        mockMvc.perform(post("/students").with(csrf()).param("id", teacherId).param("groupId", groupId)
                .param("firstName", enteredName).param("lastName", enteredSurname)).andExpect(status().isOk())
                .andExpect(view().name(path)).andExpect(content().string(containsString(groupId)))
                .andExpect(content().string(containsString(groupIdNullMessage)))
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
    void testModify_shouldAccessed_whenAdmin() throws Exception {
        Student student = new Student(1L, groups[0], "name", "surname");

        when(studentServ.retrieveById(student.getId())).thenReturn(student);
        when(mapperUtil.toDto(student)).thenReturn(
                StudentDto.builder().id(1L).groupId(groups[0].getId()).firstName("name").lastName("surname").build());

        mockMvc.perform(get("/students/{id}/modify", student.getId()).with(csrf())).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("/students/modification"))
                .andExpect(content().string(containsString("name"))).andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("surname")));

        verify(studentServ, times(2)).retrieveById(student.getId());
        verify(mapperUtil, times(1)).toDto(student);
    }

    @Test
    @WithAnonymousUser
    void testModify_shouldDenied_whenAnonymous() throws Exception {
        Student student = new Student(1L, groups[0], "name", "surname");

        when(studentServ.retrieveById(student.getId())).thenReturn(student);
        when(mapperUtil.toDto(student)).thenReturn(
                StudentDto.builder().id(1L).groupId(groups[0].getId()).firstName("name").lastName("surname").build());

        mockMvc.perform(get("/students/{id}/modify", student.getId()).with(csrf())).andDo(print())
                .andExpect(status().is(401));

        verify(studentServ, times(0)).retrieveById(student.getId());
        verify(mapperUtil, times(0)).toDto(student);
    }

    @Test
    @WithUserDetails("testStudent")
    void testModify_shouldDenied_whenStudent() throws Exception {
        Student student = new Student(1L, groups[0], "name", "surname");

        when(studentServ.retrieveById(student.getId())).thenReturn(student);
        when(mapperUtil.toDto(student)).thenReturn(
                StudentDto.builder().id(1L).groupId(groups[0].getId()).firstName("name").lastName("surname").build());

        mockMvc.perform(get("/students/{id}/modify", student.getId()).with(csrf())).andDo(print())
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(studentServ, times(0)).retrieveById(student.getId());
        verify(mapperUtil, times(0)).toDto(student);
    }

    @Test
    @WithUserDetails("testStaff")
    void testModify_shouldDenied_whenStudentStaff() throws Exception {
        Student student = new Student(1L, groups[0], "name", "surname");

        when(studentServ.retrieveById(student.getId())).thenReturn(student);
        when(mapperUtil.toDto(student)).thenReturn(
                StudentDto.builder().id(1L).groupId(groups[0].getId()).firstName("name").lastName("surname").build());

        mockMvc.perform(get("/students/{id}/modify", student.getId()).with(csrf())).andDo(print())
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(studentServ, times(0)).retrieveById(student.getId());
        verify(mapperUtil, times(0)).toDto(student);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testDelete_shouldAccessed_whenAdmin() throws Exception {
        mockMvc.perform(delete("/students/{id}", 10001L).with(csrf())).andExpect(status().is(302))
                .andExpect(view().name("redirect:/students"));
    }

    @Test
    @WithAnonymousUser
    void testDelete_shouldDenied_whenAnonymous() throws Exception {
        mockMvc.perform(delete("/students/{id}", 10001L).with(csrf())).andExpect(status().is(401));

        verify(teacherServ, times(0)).removeById(10001L);
    }

    @Test
    @WithUserDetails("testStudent")
    void testDelete_shouldDenied_whenStudent() throws Exception {
        mockMvc.perform(delete("/students/{id}", 10001L).with(csrf())).andExpect(status().is(403))
                .andExpect(view().name("error"));
        verify(teacherServ, times(0)).removeById(10001L);
    }

    @Test
    @WithUserDetails("testStaff")
    void testDelete_shouldDenied_whenStaff() throws Exception {
        mockMvc.perform(delete("/students/{id}", 10001L).with(csrf())).andExpect(status().is(403))
                .andExpect(view().name("error"));
        verify(teacherServ, times(0)).removeById(10001L);
    }

}

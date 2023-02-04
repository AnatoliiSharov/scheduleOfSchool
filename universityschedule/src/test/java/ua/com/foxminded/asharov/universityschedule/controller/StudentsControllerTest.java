package ua.com.foxminded.asharov.universityschedule.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.asharov.universityschedule.dto.StudentDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.Adapter;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Student;
import ua.com.foxminded.asharov.universityschedule.service.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.asharov.universityschedule.dto.StudentDto.MAX_LENGH_STUDENTS_NAME;
import static ua.com.foxminded.asharov.universityschedule.dto.StudentDto.MAX_LENGH_STUDENTS_SURNAME;

@WebMvcTest(controllers = StudentsController.class)
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
    void testShowAll_shouldShowList_whenRun() throws Exception {
        List<Student> students = Arrays.asList(new Student(10001L, groups[0], "FirstName1", "LastName1"),
                new Student(10002L, groups[1], "FirstName2", "LastName2"),
                new Student(10003L, groups[1], "FirstName3", "LastName3"));

        when(studentServ.retrieveAll()).thenReturn(students);
        when(mapperUtil.toDto(students.get(0))).thenReturn(StudentDto.builder().id(10001L).groupId(groups[0].getId()).firstName("FirstName1").lastName("LastName1").build());
        when(mapperUtil.toDto(students.get(1))).thenReturn(StudentDto.builder().id(10002L).groupId(groups[1].getId()).firstName("FirstName2").lastName("LastName2").build());
        when(mapperUtil.toDto(students.get(2))).thenReturn(StudentDto.builder().id(10003L).groupId(groups[1].getId()).firstName("FirstName3").lastName("LastName3").build());

        mockMvc.perform(get("/students")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/selection")).andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("FirstName2")))
                .andExpect(content().string(containsString("FirstName3")));
    }

    @Test
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        Group group = new Group(99L, "Group1");
        Student student = new Student(10001L, group, "FirstName1", "LastName1");

        when(studentServ.retrieveById(10001L)).thenReturn(student);
        when(groupServ.retrieveGroupByStudentId(10001L)).thenReturn(group);
        when(mapperUtil.toDto(student)).thenReturn(StudentDto.builder().id(10001L).groupId(group.getId())
                .firstName("FirstName1").lastName("LastName1").build());

        mockMvc.perform(get("/students/{id}", "10001")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/dashboard")).andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("LastName1")))
                .andExpect(content().string(containsString("Group1")));
    }

    @Test
    void testInviteNew() throws Exception {
        mockMvc.perform(get("/students/new")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/newbie"));
    }

    @Test
    void testLoad_shouldOk_whenCorrectValidation() throws Exception {
        when(studentServ.enter(new Student(null, groups[0], "name", "surname")))
                .thenReturn(new Student(9999L, groups[0], "name", "surname"));
        when(mapperUtil.toEntity(
                StudentDto.builder().groupId(groups[0].getId()).firstName("name").lastName("surname").build()))
                .thenReturn(new Student(null, groups[0], "name", "surname"));

        mockMvc.perform(post("/students").param("firstName", "name").param("lastName", "surname").param("groupId", "1"))
                .andExpect(status().is(302)).andExpect(view().name("redirect:students/" + 9999L));
    }

    @ParameterizedTest
    @CsvSource(value = {"'1', '1', '', '', 'Students first name cannot be empty', '', 'STUDENTS_LAST_NAME', '', '', '/students/modification' ",
            "'1', '1', '', 'STUDENTS_NAME_MORE_THEN_MAX', '', 'The length of Students first name cannot be more than 1478', 'STUDENTS_LAST_NAME', '', '', '/students/modification' ",
            "'1', '1', '', 'CORRECT_STUDENTS_NAME', '', '', '', 'Students last name cannot be empty', '', '/students/modification' ",
            "'1', '1', '', 'CORRECT_STUDENTS_NAME', '', '', 'STUDENTS_SURNAME_MORE_THEN_MAX', '', 'The length of Students last name cannot be more than 700', '/students/modification' ",
            "'1', '', 'Rooms capacity cannot be null', 'CORRECT_STUDENTS_NAME', '', '', 'CORRECT_STUDENTS_SURNAME', '', '', '/students/modification' ",
            "'', '1', '', '', 'Students first name cannot be empty', '', 'STUDENTS_LAST_NAME', '', '', '/students/newbie' ",
            "'', '1', '', 'STUDENTS_NAME_MORE_THEN_MAX', '', 'The length of Students first name cannot be more than 1478', 'TEACHERS_LAST_NAME', '', '', '/students/newbie' ",
            "'', '1', '', 'CORRECT_STUDENTS_NAME', '', '', '', 'Students last name cannot be empty', '', '/students/newbie' ",
            "'', '1', '', 'CORRECT_STUDENTS_NAME', '', '', 'STUDENTS_SURNAME_MORE_THEN_MAX', '', 'The length of Students last name cannot be more than 700', '/students/newbie' ",
            "'', '', 'Rooms capacity cannot be null', 'CORRECT_STUDENTS_NAME', '', '', 'CORRECT_STUDENTS_SURNAME', '', '', '/students/newbie' "})
    void testLoad_shouldTryAgain_whenWrongValidation(String teacherId, String groupId, String groupIdNullMessage, String enteredName, String nameEmptyMessage, String nameLengthMessage, String enteredSurname, String surnameEmptyMessadge, String surnameLengthMessadge, String path) throws Exception {
 
        if(enteredName != null && enteredName.equals("STUDENTS_NAME_MORE_THEN_MAX")) {
         enteredName = makeLongName(MAX_LENGH_STUDENTS_NAME);
     }
     
     if(enteredSurname != null &&  enteredSurname.equals("STUDENTS_SURNAME_MORE_THEN_MAX")) {
         enteredSurname = makeLongName(MAX_LENGH_STUDENTS_SURNAME);
     }
     
        mockMvc.perform(post("/students").param("id", teacherId).param("groupId", groupId).param("firstName", enteredName).param("lastName", enteredSurname))
        .andExpect(status().isOk()).andExpect(view().name(path))
        .andExpect(content().string(containsString(groupId)))
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
        
        while(name.length() <= length){
            name = name.concat("A");
        }
        return name;
    }
    
    
    @Test
    void testModify() throws Exception {
        Student student = new Student(1L, groups[0], "name", "surname");

        when(studentServ.retrieveById(student.getId())).thenReturn(student);
        when(mapperUtil.toDto(student)).thenReturn(
                StudentDto.builder().id(1L).groupId(groups[0].getId()).firstName("name").lastName("surname").build());

        mockMvc.perform(get("/students/{id}/modify", student.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/students/modification")).andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("name")))
                .andExpect(content().string(containsString("surname")));
    }

    void testDelete() throws Exception {
        mockMvc.perform(delete("/students/{id}", 10001L)).andExpect(status().is(302))
                .andExpect(view().name("redirect:/students"));
        verify(teacherServ).removeById(10001L);
    }
}

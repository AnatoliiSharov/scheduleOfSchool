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

import ua.com.foxminded.asharov.universityschedule.dto.GroupDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Student;
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

@WebMvcTest(controllers = { GroupsController.class, GlobalExceptionHandler.class })
@Import(SecurityConfigTest.class)
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
    MapperUtil mapperUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    GroupsController groupsController;

    @Test
    @WithAnonymousUser
    void testShowAll_shouldShowList_whenAnonymous() throws Exception {

        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(10001L, "AA-11"), new Group(10002L, "AA-22"), new Group(10003L, "AA-33")));

        mockMvc.perform(get("/groups").with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(groupServ, times(0)).retrieveAll();
    }

    @Test
    @WithUserDetails("testStudent")
    void testShowAll_shouldShowList_whenStudent() throws Exception {

        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(10001L, "AA-11"), new Group(10002L, "AA-22"), new Group(10003L, "AA-33")));

        mockMvc.perform(get("/groups").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/groups/selection")).andExpect(content().string(containsString("AA-11")))
                .andExpect(content().string(containsString("AA-22")))
                .andExpect(content().string(containsString("AA-33")));
        verify(groupServ).retrieveAll();
    }

    @Test
    @WithUserDetails("testStaff")
    void testShowAll_shouldShowList_whenStaff() throws Exception {

        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(10001L, "AA-11"), new Group(10002L, "AA-22"), new Group(10003L, "AA-33")));

        mockMvc.perform(get("/groups").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/groups/selection")).andExpect(content().string(containsString("AA-11")))
                .andExpect(content().string(containsString("AA-22")))
                .andExpect(content().string(containsString("AA-33")));
        verify(groupServ).retrieveAll();
    }

    @Test
    @WithUserDetails("testAdmin")
    void testShowAll_shouldShowList_whenAdmin() throws Exception {

        when(groupServ.retrieveAll()).thenReturn(
                Arrays.asList(new Group(10001L, "AA-11"), new Group(10002L, "AA-22"), new Group(10003L, "AA-33")));

        mockMvc.perform(get("/groups").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/groups/selection")).andExpect(content().string(containsString("AA-11")))
                .andExpect(content().string(containsString("AA-22")))
                .andExpect(content().string(containsString("AA-33")));
        verify(groupServ).retrieveAll();
    }

    @Test
    @WithAnonymousUser
    void testshowSelected_shouldSelected_whenAnonymous() throws Exception {
        Group group = new Group(10001L, "group1");

        when(groupServ.retrieveById(10001L)).thenReturn(new Group(10001L, "AA-11"));
        when(courseServ.retrieveByGroupId(10001L)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10002L, "Course2", "Description2")));
        when(studentServ.retrieveByGroupId(10001L))
                .thenReturn(Arrays.asList(new Student(10001L, group, "FirstName1", "LastName1"),
                        new Student(10002L, group, "FirstName2", "LastName2")));

        mockMvc.perform(get("/groups/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(groupServ, times(0)).retrieveById(10001L);
        verify(courseServ, times(0)).retrieveById(10001L);
        verify(studentServ, times(0)).retrieveByGroupId(10001L);
    }

    @Test
    @WithUserDetails("testStudent")
    void testshowSelected_shouldSelected_whenStudent() throws Exception {
        Group group = new Group(10001L, "group1");

        when(groupServ.retrieveById(10001L)).thenReturn(new Group(10001L, "AA-11"));
        when(courseServ.retrieveByGroupId(10001L)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10002L, "Course2", "Description2")));
        when(studentServ.retrieveByGroupId(10001L))
                .thenReturn(Arrays.asList(new Student(10001L, group, "FirstName1", "LastName1"),
                        new Student(10002L, group, "FirstName2", "LastName2")));

        mockMvc.perform(get("/groups/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().is(200))
                .andExpect(view().name("/groups/dashboard")).andExpect(content().string(containsString("AA-11")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")))
                .andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("LastName1")))
                .andExpect(content().string(containsString("FirstName2")))
                .andExpect(content().string(containsString("LastName2")));

        verify(groupServ, times(1)).retrieveById(10001L);
        verify(studentServ, times(1)).retrieveByGroupId(10001L);
    }

    @Test
    @WithUserDetails("testStaff")
    void testshowSelected_shouldSelected_whenStaff() throws Exception {
        Group group = new Group(10001L, "group1");

        when(groupServ.retrieveById(10001L)).thenReturn(new Group(10001L, "AA-11"));
        when(courseServ.retrieveByGroupId(10001L)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10002L, "Course2", "Description2")));
        when(studentServ.retrieveByGroupId(10001L))
                .thenReturn(Arrays.asList(new Student(10001L, group, "FirstName1", "LastName1"),
                        new Student(10002L, group, "FirstName2", "LastName2")));

        mockMvc.perform(get("/groups/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().is(200))
                .andExpect(view().name("/groups/dashboard")).andExpect(content().string(containsString("AA-11")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")))
                .andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("LastName1")))
                .andExpect(content().string(containsString("FirstName2")))
                .andExpect(content().string(containsString("LastName2")));

        verify(groupServ, times(1)).retrieveById(10001L);
        verify(studentServ, times(1)).retrieveByGroupId(10001L);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        Group group = new Group(10001L, "group1");

        when(groupServ.retrieveById(10001L)).thenReturn(new Group(10001L, "AA-11"));
        when(courseServ.retrieveByGroupId(10001L)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10002L, "Course2", "Description2")));
        when(studentServ.retrieveByGroupId(10001L))
                .thenReturn(Arrays.asList(new Student(10001L, group, "FirstName1", "LastName1"),
                        new Student(10002L, group, "FirstName2", "LastName2")));

        mockMvc.perform(get("/groups/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/groups/dashboard")).andExpect(content().string(containsString("AA-11")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")))
                .andExpect(content().string(containsString("FirstName1")))
                .andExpect(content().string(containsString("LastName1")))
                .andExpect(content().string(containsString("FirstName2")))
                .andExpect(content().string(containsString("LastName2")));

        verify(groupServ, times(1)).retrieveById(10001L);
        verify(studentServ, times(1)).retrieveByGroupId(10001L);
    }

    @Test
    @WithAnonymousUser
    void testInviteNew_shouldDenied_whenAnonymous() throws Exception {
        mockMvc.perform(get("/groups/new").with(csrf())).andDo(print()).andExpect(status().is(401));
    }

    @Test
    @WithUserDetails("testStudent")
    void testInviteNew_shouldDenied_whenStudent() throws Exception {
        mockMvc.perform(get("/groups/new").with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails("testStaff")
    void testInviteNew_shouldDenied_whenStaff() throws Exception {
        mockMvc.perform(get("/groups/new").with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testInviteNew_shouldAccessed_whenAdmin() throws Exception {
        mockMvc.perform(get("/groups/new").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/groups/newbie"));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testLoad_shouldOk_whenCorrectValidationAndAdmin() throws Exception {
        when(groupServ.enter(new Group(null, "AA-11"))).thenReturn(new Group(10001L, "AA-11"));
        when(mapperUtil.toEntity(new GroupDto(null, "AA-11"))).thenReturn(new Group(null, "AA-11"));

        mockMvc.perform(post("/groups").with(csrf()).param("name", "AA-11")).andExpect(status().is(302))
                .andExpect(view().name("redirect:groups/" + 10001L));

        verify(groupServ, times(1)).enter(new Group(null, "AA-11"));
        verify(mapperUtil, times(1)).toEntity(new GroupDto(null, "AA-11"));
    }

    @Test
    @WithAnonymousUser
    void testLoad_shouldDenied_whenAnyAndAnonymous() throws Exception {
        when(groupServ.enter(new Group(null, "AA-11"))).thenReturn(new Group(10001L, "AA-11"));
        when(mapperUtil.toEntity(new GroupDto(null, "AA-11"))).thenReturn(new Group(null, "AA-11"));

        mockMvc.perform(post("/groups").with(csrf()).param("name", "AA-11")).andExpect(status().is(401));

        verify(groupServ, times(0)).enter(new Group(null, "AA-11"));
        verify(mapperUtil, times(0)).toEntity(new GroupDto(null, "AA-11"));
    }

    @Test
    @WithUserDetails("testStudent")
    void testLoad_shouldDenied_whenAnyAndStudent() throws Exception {
        when(groupServ.enter(new Group(null, "AA-11"))).thenReturn(new Group(10001L, "AA-11"));
        when(mapperUtil.toEntity(new GroupDto(null, "AA-11"))).thenReturn(new Group(null, "AA-11"));

        mockMvc.perform(post("/groups").with(csrf()).param("name", "AA-11")).andExpect(status().is(403));

        verify(groupServ, times(0)).enter(new Group(null, "AA-11"));
        verify(mapperUtil, times(0)).toEntity(new GroupDto(null, "AA-11"));
    }

    @Test
    @WithUserDetails("testStaff")
    void testLoad_shouldDenied_whenAnyAndStaff() throws Exception {
        when(groupServ.enter(new Group(null, "AA-11"))).thenReturn(new Group(10001L, "AA-11"));
        when(mapperUtil.toEntity(new GroupDto(null, "AA-11"))).thenReturn(new Group(null, "AA-11"));

        mockMvc.perform(post("/groups").with(csrf()).param("name", "AA-11")).andExpect(status().is(403));

        verify(groupServ, times(0)).enter(new Group(null, "AA-11"));
        verify(mapperUtil, times(0)).toEntity(new GroupDto(null, "AA-11"));
    }

    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @CsvSource(value = { "1 , '', Groups name cannot be empty, '', /groups/modification",
            "'1' , 'NAME_MORE_THAN_THIRTY_AAAAAAAAAAA', '', 'The length of Groups name cannot be more then 30 symbols', '/groups/modification'",
            "'' , '', 'Groups name cannot be empty', '', '/groups/newbie'",
            "'' , 'NAME_MORE_THAN_THIRTY_AAAAAAAAAAA', '', 'The length of Groups name cannot be more then 30 symbols', '/groups/newbie'" })
    void _shouldTryAgain_whenWrongValidation(String groupId, String enteredName, String nameMessadgeOne,
            String nameMessadgeTwo, String path) throws Exception {

        mockMvc.perform(post("/groups").with(csrf()).param("id", groupId).param("name", enteredName))
                .andExpect(status().isOk()).andExpect(view().name(path))
                .andExpect(content().string(containsString(enteredName)))
                .andExpect(content().string(containsString(nameMessadgeOne)))
                .andExpect(content().string(containsString(nameMessadgeTwo)));
    }

    @Test
    @WithAnonymousUser
    void testModify_shouldDenied_whenAnonymous() throws Exception {
        Long id = 10001L;

        when(groupServ.retrieveById(id)).thenReturn(new Group(10001L, "AA-11"));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        when(courseServ.retrieveByAccreditedTeacher(id)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3")));
        mockMvc.perform(get("/groups/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(groupServ, times(0)).retrieveById(id);
        verify(groupServ, times(0)).retrieveAll();
        verify(courseServ, times(0)).retrieveByAccreditedTeacher(id);
    }

    @Test
    @WithUserDetails("testStudent")
    void testModify_shouldDenied_whenStudent() throws Exception {
        Long id = 10001L;

        when(groupServ.retrieveById(id)).thenReturn(new Group(10001L, "AA-11"));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        when(courseServ.retrieveByAccreditedTeacher(id)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3")));
        mockMvc.perform(get("/groups/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().is(403));

        verify(groupServ, times(0)).retrieveById(id);
        verify(groupServ, times(0)).retrieveAll();
        verify(courseServ, times(0)).retrieveByAccreditedTeacher(id);
    }

    @Test
    @WithUserDetails("testStaff")
    void testModify_shouldDenied_whenStaff() throws Exception {
        Long id = 10001L;

        when(groupServ.retrieveById(id)).thenReturn(new Group(10001L, "AA-11"));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        when(courseServ.retrieveByAccreditedTeacher(id)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3")));
        mockMvc.perform(get("/groups/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(groupServ, times(0)).retrieveById(id);
        verify(groupServ, times(0)).retrieveAll();
        verify(courseServ, times(0)).retrieveByAccreditedTeacher(id);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testModify_shouldAccessed_whenAdmin() throws Exception {
        Long id = 10001L;

        when(groupServ.retrieveById(id)).thenReturn(new Group(10001L, "AA-11"));
        when(courseServ.retrieveAll()).thenReturn(Arrays.asList(new Course(10001L, "Course1", "Description1"),
                new Course(10002L, "Course2", "Description2"), new Course(10003L, "Course3", "Description3")));
        when(courseServ.retrieveByAccreditedTeacher(id)).thenReturn(Arrays
                .asList(new Course(10001L, "Course1", "Description1"), new Course(10003L, "Course3", "Description3")));
        mockMvc.perform(get("/groups/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/groups/modification")).andExpect(content().string(containsString("AA-11")))
                .andExpect(content().string(containsString("Course1")))
                .andExpect(content().string(containsString("Course2")))
                .andExpect(content().string(containsString("Course3")));

        verify(groupServ, times(1)).retrieveById(id);
        verify(courseServ).retrieveAll();
    }

    @Test
    @WithUserDetails("testAdmin")
    void testAddAccreditedCourse_shouldAccessed_whenAdmin() throws Exception {
        Long groupId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/groups/{id}/add", groupId, courseId).with(csrf()).param("groupId", groupId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(302))
                .andExpect(view().name("redirect:modify"));
        verify(courseServ, times(1)).addToGroup(courseId, groupId);
    }

    @Test
    @WithAnonymousUser
    void testAddAccreditedCourse_shouldDenied_whenAnonymous() throws Exception {
        Long groupId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/groups/{id}/add", groupId, courseId).param("groupId", groupId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(403));

        verify(courseServ, times(0)).addToGroup(courseId, groupId);
    }

    @Test
    @WithUserDetails("testStudent")
    void testAddAccreditedCourse_shouldDenied_whenStudent() throws Exception {
        Long groupId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/groups/{id}/add", groupId, courseId).param("groupId", groupId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(403));

        verify(courseServ, times(0)).addToGroup(courseId, groupId);
    }

    @Test
    @WithUserDetails("testStaff")
    void testAddAccreditedCourse_shouldDenied_whenStaff() throws Exception {
        Long groupId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/groups/{id}/add", groupId, courseId).param("groupId", groupId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(403));

        verify(courseServ, times(0)).addToGroup(courseId, groupId);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testRemoveAccreditedCourse_shouldAccessed_whenAdmin() throws Exception {
        Long groupId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/groups/{id}/remove", groupId, courseId).with(csrf())
                .param("groupId", groupId.toString()).param("courseId", courseId.toString()))
                .andExpect(status().is(302)).andExpect(view().name("redirect:modify"));
        verify(courseServ, times(1)).removeFromGroup(courseId, groupId);
    }

    @Test
    @WithAnonymousUser
    void testRemoveAccreditedCourse_shouldDenied_whenAnonymous() throws Exception {
        Long groupId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/groups/{id}/remove", groupId, courseId).param("groupId", groupId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(403));

        verify(courseServ, times(0)).removeFromGroup(courseId, groupId);
    }

    @Test
    @WithUserDetails("testStudent")
    void testRemoveAccreditedCourse_shouldDenied_whenStudent() throws Exception {
        Long groupId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/groups/{id}/remove", groupId, courseId).param("groupId", groupId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(403));

        verify(courseServ, times(0)).removeFromGroup(courseId, groupId);
    }

    @Test
    @WithUserDetails("testStaff")
    void testRemoveAccreditedCourse_shouldDenied_whenStaff() throws Exception {
        Long groupId = 10001L;
        Long courseId = 10001L;

        mockMvc.perform(patch("/groups/{id}/remove", groupId, courseId).param("groupId", groupId.toString())
                .param("courseId", courseId.toString())).andExpect(status().is(403));

        verify(courseServ, times(0)).removeFromGroup(courseId, groupId);
    }

    @Test
    @WithAnonymousUser
    void testDelete_shouldDenied_whenUserIsAnonymus() throws Exception {
        mockMvc.perform(delete("/groups/{id}", 10001L)).andExpect(status().is(403));

        verify(groupServ, times(0)).removeById(10001L);
    }

    @Test
    @WithMockUser(username = "testStudent")
    void testDelete_shouldDenied_whenUserStudent() throws Exception {
        mockMvc.perform(delete("/groups/{id}", 10001L)).andExpect(status().is(403));

        verify(groupServ, times(0)).removeById(10001L);
    }

    @Test
    @WithMockUser(username = "testStaff")
    void testDelete_shouldDenied_whenUserStaff() throws Exception {
        mockMvc.perform(delete("/groups/{id}", 10001L)).andExpect(status().is(403));

        verify(groupServ, times(0)).removeById(10001L);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testDelete_shouldOk_whenUserAdmin() throws Exception {
        mockMvc.perform(delete("/groups/{id}", 10001L).with(csrf())).andExpect(status().is(302))
                .andExpect(view().name("redirect:/groups"));
        verify(groupServ, times(1)).removeById(10001L);
    }

}

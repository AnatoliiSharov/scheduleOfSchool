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

import ua.com.foxminded.asharov.universityschedule.dto.RoomDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.exception.GlobalExceptionHandler;
import ua.com.foxminded.asharov.universityschedule.secur.conf.SecurityConfigTest;
import ua.com.foxminded.asharov.universityschedule.service.*;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = { RoomsController.class, GlobalExceptionHandler.class })
@Import(SecurityConfigTest.class)
class RoomsControllerTest {

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
    RoomsController roomsController;

    @Test
    @WithAnonymousUser
    void testShowAll_shouldShowList_whenRunAnonymous() throws Exception {

        when(roomServ.retrieveAll()).thenReturn(Arrays.asList(new Room(10001L, "address1", 10),
                new Room(10002L, "address2", 20), new Room(10003L, "address3", 30)));

        mockMvc.perform(get("/rooms").with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(roomServ, times(0)).retrieveAll();
    }

    @Test
    @WithUserDetails("testStudent")
    void testShowAll_shouldShowList_whenRunStudent() throws Exception {

        when(roomServ.retrieveAll()).thenReturn(Arrays.asList(new Room(10001L, "address1", 10),
                new Room(10002L, "address2", 20), new Room(10003L, "address3", 30)));

        mockMvc.perform(get("/rooms").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/selection")).andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("address2")))
                .andExpect(content().string(containsString("address3")));

        verify(roomServ, times(1)).retrieveAll();
    }

    @Test
    @WithUserDetails("testStaff")
    void testShowAll_shouldShowList_whenRunStaff() throws Exception {

        when(roomServ.retrieveAll()).thenReturn(Arrays.asList(new Room(10001L, "address1", 10),
                new Room(10002L, "address2", 20), new Room(10003L, "address3", 30)));

        mockMvc.perform(get("/rooms").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/selection")).andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("address2")))
                .andExpect(content().string(containsString("address3")));

        verify(roomServ, times(1)).retrieveAll();
    }

    @Test
    @WithUserDetails("testAdmin")
    void testShowAll_shouldShowList_whenRun() throws Exception {

        when(roomServ.retrieveAll()).thenReturn(Arrays.asList(new Room(10001L, "address1", 10),
                new Room(10002L, "address2", 20), new Room(10003L, "address3", 30)));

        mockMvc.perform(get("/rooms").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/selection")).andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("address2")))
                .andExpect(content().string(containsString("address3")));

        verify(roomServ, times(1)).retrieveAll();
    }

    @Test
    @WithAnonymousUser
    void testshowSelected_shouldSelected_whenRunAnonymous() throws Exception {
        when(roomServ.retrieveById(10001L)).thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(roomServ, times(0)).retrieveById(10001L);
    }

    @Test
    @WithUserDetails("testStudent")
    void testshowSelected_shouldSelected_whenRunStudent() throws Exception {
        when(roomServ.retrieveById(10001L)).thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/dashboard")).andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("10")));

        verify(roomServ, times(1)).retrieveById(10001L);
    }

    @Test
    @WithUserDetails("testStaff")
    void testshowSelected_shouldSelected_whenRunStaff() throws Exception {
        when(roomServ.retrieveById(10001L)).thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/dashboard")).andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("10")));

        verify(roomServ, times(1)).retrieveById(10001L);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        when(roomServ.retrieveById(10001L)).thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}", "10001").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/dashboard")).andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("10")));

        verify(roomServ, times(1)).retrieveById(10001L);
    }

    @Test
    @WithAnonymousUser
    void testInviteNew_shouldDenied_whenAnonymous() throws Exception {
        mockMvc.perform(get("/rooms/new").with(csrf())).andDo(print()).andExpect(status().is(401));
    }

    @Test
    @WithUserDetails("testStudent")
    void testInviteNew_shouldDenied_whenStudent() throws Exception {
        mockMvc.perform(get("/rooms/new").with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails("testStaff")
    void testInviteNew_shouldDenied_whenStaff() throws Exception {
        mockMvc.perform(get("/rooms/new").with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testInviteNew_shouldDenied_whenAdmin() throws Exception {
        mockMvc.perform(get("/rooms/new").with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/newbie"));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testLoad_shouldOk_whenCorrectValidationAdmin() throws Exception {
        when(roomServ.enter(new Room(null, "addressNew", 10))).thenReturn(new Room(99999L, "addressNew", 10));
        when(mapperUtil.toEntity(new RoomDto(null, "addressNew", 10))).thenReturn(new Room(null, "addressNew", 10));

        mockMvc.perform(post("/rooms").with(csrf()).param("address", "addressNew").param("capacity", "10"))
                .andExpect(status().is(302)).andExpect(view().name("redirect:rooms/" + 99999L));

        verify(roomServ, times(1)).enter(new Room(null, "addressNew", 10));
        verify(mapperUtil, times(1)).toEntity(new RoomDto(null, "addressNew", 10));
    }

    @Test
    @WithAnonymousUser
    void testLoad_shouldDenied_whenAnyValidationAnonymous() throws Exception {
        when(roomServ.enter(new Room(null, "addressNew", 10))).thenReturn(new Room(99999L, "addressNew", 10));
        when(mapperUtil.toEntity(new RoomDto(null, "addressNew", 10))).thenReturn(new Room(null, "addressNew", 10));

        mockMvc.perform(post("/rooms").with(csrf()).param("address", "addressNew").param("capacity", "10"))
                .andExpect(status().is(401));

        verify(roomServ, times(0)).enter(new Room(null, "addressNew", 10));
        verify(mapperUtil, times(0)).toEntity(new RoomDto(null, "addressNew", 10));
    }

    @Test
    @WithUserDetails("testStudent")
    void testLoad_shouldDenied_whenAnyValidationStudent() throws Exception {
        when(roomServ.enter(new Room(null, "addressNew", 10))).thenReturn(new Room(99999L, "addressNew", 10));
        when(mapperUtil.toEntity(new RoomDto(null, "addressNew", 10))).thenReturn(new Room(null, "addressNew", 10));

        mockMvc.perform(post("/rooms").with(csrf()).param("address", "addressNew").param("capacity", "10"))
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(roomServ, times(0)).enter(new Room(null, "addressNew", 10));
        verify(mapperUtil, times(0)).toEntity(new RoomDto(null, "addressNew", 10));
    }

    @Test
    @WithUserDetails("testStaff")
    void testLoad_shouldDenied_whenAnyValidationStaff() throws Exception {
        when(roomServ.enter(new Room(null, "addressNew", 10))).thenReturn(new Room(99999L, "addressNew", 10));
        when(mapperUtil.toEntity(new RoomDto(null, "addressNew", 10))).thenReturn(new Room(null, "addressNew", 10));

        mockMvc.perform(post("/rooms").with(csrf()).param("address", "addressNew").param("capacity", "10"))
                .andExpect(status().is(403)).andExpect(view().name("error"));

        verify(roomServ, times(0)).enter(new Room(null, "addressNew", 10));
        verify(mapperUtil, times(0)).toEntity(new RoomDto(null, "addressNew", 10));
    }

    @ParameterizedTest
    @WithUserDetails("testAdmin")
    @CsvSource(value = { "'1', '' , Rooms address cannot be empty, '10', '', '', '/rooms/modification'",
            "'1', 'ADDRES_OF_ROOM' , '', '', 'Rooms capacity cannot be null', '', '/rooms/modification'",
            "'1', 'ADDRES_OF_ROOM' , '', '1001', '', 'Rooms capacity is number with value less than 1000', '/rooms/modification'",
            "'', '' , Rooms address cannot be empty, '10', '', '', '/rooms/newbie'",
            "'', 'ADDRES_OF_ROOM' , '', '', 'Rooms capacity cannot be null', '', '/rooms/newbie'",
            "'', 'ADDRES_OF_ROOM' , '', '1001', '', 'Rooms capacity is number with value less than 1000', '/rooms/newbie'" })
    void testLoad_shouldTryAgain_whenWrongValidationAndAdmin(String roomId, String enteredAddress,
            String addressEmptyMessadge, String capacity, String capacityNullMessadge, String capacityValueMessadge,
            String path) throws Exception {

        mockMvc.perform(post("/rooms").with(csrf()).param("id", roomId).param("address", enteredAddress)
                .param("capacity", capacity)).andExpect(status().isOk()).andExpect(view().name(path))
                .andExpect(content().string(containsString(enteredAddress)))
                .andExpect(content().string(containsString(addressEmptyMessadge)))
                .andExpect(content().string(containsString(capacity)))
                .andExpect(content().string(containsString(capacityNullMessadge)));
    }

    @Test
    @WithUserDetails("testAdmin")
    void testModify_shouldAccessed_when() throws Exception {
        Long id = 10001L;

        when(roomServ.retrieveById(id)).thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/modification")).andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("10")));

        verify(roomServ, times(1)).retrieveById(id);
    }

    @Test
    @WithAnonymousUser
    void testModify_shouldDenied_whenAnonymous() throws Exception {
        Long id = 10001L;

        when(roomServ.retrieveById(id)).thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().is(401));

        verify(roomServ, times(0)).retrieveById(id);
    }

    @Test
    @WithUserDetails("testStudent")
    void testModify_shouldDenied_whenStudent() throws Exception {
        Long id = 10001L;

        when(roomServ.retrieveById(id)).thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(roomServ, times(0)).retrieveById(id);
    }

    @Test
    @WithUserDetails("testStaff")
    void testModify_shouldDenied_whenStaff() throws Exception {
        Long id = 10001L;

        when(roomServ.retrieveById(id)).thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}/modify", id).with(csrf())).andDo(print()).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(roomServ, times(0)).retrieveById(id);
    }

    @Test
    @WithUserDetails("testAdmin")
    void testDelete_shouldAccessed_when() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 10001L).with(csrf())).andExpect(status().is(302))
                .andExpect(view().name("redirect:/rooms"));

        verify(roomServ, times(1)).removeById(10001L);
    }

    @Test
    @WithAnonymousUser
    void testDelete_shouldDenied_whenAnonymous() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 10001L).with(csrf())).andExpect(status().is(401));

        verify(roomServ, times(0)).removeById(10001L);
    }

    @Test
    @WithUserDetails("testStudent")
    void testDelete_shouldDenied_whenStudent() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 10001L).with(csrf())).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(roomServ, times(0)).removeById(10001L);
    }

    @Test
    @WithUserDetails("testStaff")
    void testDelete_shouldDenied_whenStaff() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 10001L).with(csrf())).andExpect(status().is(403))
                .andExpect(view().name("error"));

        verify(roomServ, times(0)).removeById(10001L);
    }

}

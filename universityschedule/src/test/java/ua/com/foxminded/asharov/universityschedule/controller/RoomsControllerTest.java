package ua.com.foxminded.asharov.universityschedule.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.asharov.universityschedule.dto.RoomDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.service.*;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RoomsController.class)
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
    void testShowAll_shouldShowList_whenRun() throws Exception {

        when(roomServ.retrieveAll()).thenReturn(Arrays.asList(new Room(10001L, "address1", 10),
                new Room(10002L, "address2", 20), new Room(10003L, "address3", 30)));

        mockMvc.perform(get("/rooms")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/selection")).andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("address2")))
                .andExpect(content().string(containsString("address3")));
    }

    @Test
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        when(roomServ.retrieveById(10001L)).thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}", "10001")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/dashboard")).andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("10")));
    }

    @Test
    void testInviteNew() throws Exception {
        mockMvc.perform(get("/rooms/new")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/newbie"));
    }

    @Test
    void testLoad_shouldOk_whenCorrectValidation() throws Exception {
        when(roomServ.enter(new Room(null, "addressNew", 10))).thenReturn(new Room(99999L, "addressNew", 10));
        when(mapperUtil.toEntity(new RoomDto(null, "addressNew", 10))).thenReturn(new Room(null, "addressNew", 10));

        mockMvc.perform(post("/rooms").param("address", "addressNew").param("capacity", "10"))
                .andExpect(status().is(302)).andExpect(view().name("redirect:rooms/" + 99999L));
    }
    
    @ParameterizedTest
    @CsvSource(value = {"'1', '' , Rooms address cannot be empty, '10', '', '', '/rooms/modification'",
            "'1', 'ADDRES_OF_ROOM' , '', '', 'Rooms capacity cannot be null', '', '/rooms/modification'",
            "'1', 'ADDRES_OF_ROOM' , '', '1001', '', 'Rooms capacity is number with value less than 1000', '/rooms/modification'",
            "'', '' , Rooms address cannot be empty, '10', '', '', '/rooms/newbie'",
            "'', 'ADDRES_OF_ROOM' , '', '', 'Rooms capacity cannot be null', '', '/rooms/newbie'",
            "'', 'ADDRES_OF_ROOM' , '', '1001', '', 'Rooms capacity is number with value less than 1000', '/rooms/newbie'"})
    void testLoad_shouldTryAgain_whenWrongValidation(String roomId, String enteredAddress, String addressEmptyMessadge, String capacity, String capacityNullMessadge, String capacityValueMessadge, String path) throws Exception {
        
        mockMvc.perform(post("/rooms").param("id", roomId).param("address", enteredAddress).param("capacity", capacity))
        .andExpect(status().isOk()).andExpect(view().name(path))
        .andExpect(content().string(containsString(enteredAddress)))
        .andExpect(content().string(containsString(addressEmptyMessadge)))
        .andExpect(content().string(containsString(capacity)))
        .andExpect(content().string(containsString(capacityNullMessadge)));
    }

    @Test
    void testModify() throws Exception {
        Long id = 10001L;

        when(roomServ.retrieveById(id)).thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}/modify", id)).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/modification")).andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("10")));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 10001L)).andExpect(status().is(302))
                .andExpect(view().name("redirect:/rooms"));
        verify(roomServ).removeById(10001L);
    }
}

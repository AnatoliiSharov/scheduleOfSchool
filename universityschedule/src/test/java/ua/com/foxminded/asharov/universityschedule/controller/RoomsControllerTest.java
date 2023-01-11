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

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.asharov.universityschedule.model.Room;
import ua.com.foxminded.asharov.universityschedule.model.Adapter;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.LectureService;
import ua.com.foxminded.asharov.universityschedule.service.RoomService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

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
    Adapter lectureAdapt;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    RoomsController roomsController;

    @Test
    void testShowAll_shouldShowList_whenRun() throws Exception {

        when(roomServ.retrieveAll())
                .thenReturn(Arrays.asList(new Room(10001L, "address1", 10),
                        new Room(10002L, "address2", 20),
                        new Room(10003L, "address3", 30)));

        mockMvc.perform(get("/rooms")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/selection"))
                .andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("address2")))
                .andExpect(content().string(containsString("address3")));
    }

    @Test
    void testshowSelected_shouldSelected_whenRun() throws Exception {
        when(roomServ.retrieveById(10001L))
                .thenReturn(new Room(10001L, "address1", 10));

        mockMvc.perform(get("/rooms/{id}", "10001")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/dashboard"))
                .andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("10")));
    }


    @Test
    void testInviteNew() throws Exception {
        mockMvc.perform(get("/rooms/new")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/newbie"));
    }

    @Test
    void testLoad() throws Exception {
        when(roomServ.enter(new Room(null, "addressNew", 10)))
                .thenReturn(new Room(99999L, "addressNew", 10));

        mockMvc.perform(post("/rooms").param("address", "addressNew")
                .param("capacity", "10")).andExpect(status().is(302))
                .andExpect(view().name("redirect:rooms/" + 99999L));
    }
    
    @Test
    void testModify() throws Exception {
        Long id = 10001L;

        when(roomServ.retrieveById(id)).thenReturn(new Room(10001L, "address1", 20));

        mockMvc.perform(get("/rooms/{id}/modify", id)).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("/rooms/modification"))
                .andExpect(content().string(containsString("address1")))
                .andExpect(content().string(containsString("20")));
    }

    @Test
    void testReload() throws Exception {
        when(roomServ.enter(new Room(10001L, "addressNew", 20)))
                .thenReturn(new Room(10001L, "addressNew", 20));
        
        mockMvc.perform(patch("/rooms")
                .param("id", "10001")
                .param("address", "addressNew")
                .param("capacity", "20"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:rooms/10001"));
    }
    
    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/rooms/{id}", 10001L))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rooms"));
        verify(roomServ).removeById(10001L);
    }
}

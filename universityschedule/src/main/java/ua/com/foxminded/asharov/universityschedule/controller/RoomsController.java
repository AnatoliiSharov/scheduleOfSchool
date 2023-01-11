 package ua.com.foxminded.asharov.universityschedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.asharov.universityschedule.model.Room;
import ua.com.foxminded.asharov.universityschedule.service.*;

@Controller
@RequestMapping("/rooms")
public class RoomsController {
    static final String LANDLORD = "room";
    static final String OWNER = "owner";
    static final String OWNERNAME = "ownername";
    
    private final RoomService roomServ;

    public RoomsController(RoomService roomServ) {
        this.roomServ = roomServ;
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute("owners", roomServ.retrieveAll());
        return "/"+ LANDLORD +"s/selection";
    }

    @GetMapping("/{id}")
    public String showSelected(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, roomServ.retrieveById(id));
        return "/"+LANDLORD+"s/dashboard";
    }

    @GetMapping("/new")
    public String inviteNew(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, new Room());
        return "/"+LANDLORD+"s/newbie";
    }

    @PostMapping()
    public String load(@ModelAttribute(LANDLORD) Room room, Model model) {
        return "redirect:" + LANDLORD + "s/" + (roomServ.enter(room)).getId();
    }

    @GetMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, roomServ.retrieveById(id));
        return "/"+LANDLORD+"s/modification";
    }

    @PatchMapping()
    public String reload(@ModelAttribute(LANDLORD) Room room) {
        return "redirect:" + LANDLORD + "s/" + roomServ.enter(room).getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        roomServ.removeById(id);
        return "redirect:/"+ LANDLORD +"s";
    }

}
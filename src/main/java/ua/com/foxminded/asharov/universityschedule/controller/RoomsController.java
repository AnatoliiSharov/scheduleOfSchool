package ua.com.foxminded.asharov.universityschedule.controller;

import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ua.com.foxminded.asharov.universityschedule.config.Role;
import ua.com.foxminded.asharov.universityschedule.dto.RoomDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.service.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomsController {
    static final String LANDLORD = "room";
    static final String OWNER = "owner";
    static final String OWNERNAME = "ownername";

    private final RoomService roomServ;
    private final MapperUtil mapperUtil;

    public RoomsController(RoomService roomServ, MapperUtil mapperUtil) {
        this.roomServ = roomServ;
        this.mapperUtil = mapperUtil;
    }

    @Secured(Role.Recall.VIEWER)
    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute("owners", roomServ.retrieveAll());
        return "/" + LANDLORD + "s/selection";
    }

    @Secured(Role.Recall.VIEWER)
    @GetMapping("/{id}")
    public String showSelected(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, roomServ.retrieveById(id));
        return "/" + LANDLORD + "s/dashboard";
    }

    @GetMapping("/new")
    @Secured(Role.Recall.CREATOR)
    public String inviteNew(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, new Room());
        return "/" + LANDLORD + "s/newbie";
    }

    @PostMapping()
    @Secured(Role.Recall.CREATOR)
    public String load(@Valid @ModelAttribute(OWNER) RoomDto roomDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute(OWNERNAME, LANDLORD);
            model.addAttribute(OWNER, roomDto);
            
            if (roomDto.getId() != null) {
                return "/" + LANDLORD + "s/modification";
            } else {
                return "/" + LANDLORD + "s/newbie";
            }
        }
        return "redirect:" + LANDLORD + "s/" + (roomServ.enter(mapperUtil.toEntity(roomDto))).getId();
    }

    @GetMapping("/{id}/modify")
    @Secured(Role.Recall.CREATOR)
    public String modify(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, roomServ.retrieveById(id));
        return "/" + LANDLORD + "s/modification";
    }

    @DeleteMapping("/{id}")
    @Secured(Role.Recall.CREATOR)
    public String delete(@PathVariable("id") Long id) {
        roomServ.removeById(id);
        return "redirect:/" + LANDLORD + "s";
    }

}

package ua.com.foxminded.asharov.universityschedule.controller;

import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ua.com.foxminded.asharov.universityschedule.config.Role;
import ua.com.foxminded.asharov.universityschedule.dto.GroupDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.StudentService;

@Controller
@RequestMapping("/groups")
public class GroupsController {
    static final String LANDLORD = "group";
    static final String OWNER = "owner";
    static final String OWNERNAME = "ownername";
    static final String ASSETS = "assets";
    static final String ASSETNAME = "assetname";
    static final String COURSE = "course";

    private final GroupService groupServ;
    private final CourseService courseServ;
    private final StudentService studentServ;
    private final MapperUtil mapperUtil;

    public GroupsController(GroupService groupServ, CourseService courseServ, StudentService studentServ,
            MapperUtil mapperUtil) {
        this.groupServ = groupServ;
        this.courseServ = courseServ;
        this.studentServ = studentServ;
        this.mapperUtil = mapperUtil;
    }

    @Secured(Role.Recall.VIEWER)
    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute("owners", groupServ.retrieveAll());
        return "/" + LANDLORD + "s/selection";
    }
    
    @Secured(Role.Recall.VIEWER)
    @GetMapping("/{id}")
    public String showSelected(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, groupServ.retrieveById(id));
        model.addAttribute(ASSETS, courseServ.retrieveByGroupId(id));
        model.addAttribute("twoassets", studentServ.retrieveByGroupId(id));
        return "/" + LANDLORD + "s/dashboard";
    }

    @Secured(Role.Recall.CREATOR)
    @GetMapping("/new")
    public String inviteNew(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, new Group());
        model.addAttribute(ASSETNAME, COURSE);
        model.addAttribute(ASSETS, courseServ.retrieveAll());
        return "/" + LANDLORD + "s/newbie";
    }

    @Secured(Role.Recall.CREATOR)
    @PostMapping()
    public String load(@Valid @ModelAttribute(OWNER) GroupDto groupDto, BindingResult result, Model model) {

        if(result.hasErrors()) {
            model.addAttribute(OWNERNAME, LANDLORD);
            model.addAttribute(ASSETNAME, COURSE);
            model.addAttribute(ASSETS, courseServ.retrieveAll());
            model.addAttribute(OWNER, groupDto);
            
            if(groupDto.getId()!=null) {
                model.addAttribute("ownedassets", courseServ.retrieveByGroupId(groupDto.getId()));
                return "/" + LANDLORD + "s/modification";
            }else {
                return "/" + LANDLORD + "s/newbie"; 
            }
        }
        return "redirect:" + LANDLORD + "s/" + groupServ.enter(mapperUtil.toEntity(groupDto)).getId();
    }

    @Secured(Role.Recall.CREATOR)
    @GetMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id, Model model) {

        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(ASSETNAME, COURSE);
        model.addAttribute(OWNER, groupServ.retrieveById(id));
        model.addAttribute(ASSETS, courseServ.retrieveAll());
        model.addAttribute("ownedassets", courseServ.retrieveByGroupId(id));
        return "/" + LANDLORD + "s/modification";
    }

    @Secured(Role.Recall.CREATOR)
    @PatchMapping("/{id}/add")
    public String addAccreditedCourse(@PathVariable("id") Long id,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        courseServ.addToGroup(courseId, id);
        return "redirect:modify";
    }

    @Secured(Role.Recall.CREATOR)
    @PatchMapping("/{id}/remove")
    public String removeAccreditedCourse(@PathVariable("id") Long id,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        courseServ.removeFromGroup(courseId, id);
        return "redirect:modify";
    }

    @Secured(Role.Recall.CREATOR)
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        groupServ.removeById(id);
        return "redirect:/" + LANDLORD + "s";
    }

}

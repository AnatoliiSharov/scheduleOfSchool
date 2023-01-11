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
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.asharov.universityschedule.model.Group;
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
    
    private final GroupService groupServ;
    private final CourseService courseServ;
    private final StudentService studentServ;

    public GroupsController(GroupService groupServ,
            CourseService courseServ, StudentService studentServ) {
        this.groupServ = groupServ;
        this.courseServ = courseServ;
        this.studentServ = studentServ;
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute("owners", groupServ.retrieveAll());
        return "/"+ LANDLORD +"s/selection";
    }

    @GetMapping("/{id}")
    public String showSelected(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, groupServ.retrieveById(id));
        model.addAttribute(ASSETS, courseServ.retrieveByGroupId(id));
        model.addAttribute("twoassets", studentServ.retrieveByGroupId(id));
        return "/"+LANDLORD+"s/dashboard";
    }

    @GetMapping("/new")
    public String inviteNew(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, new Group());
        model.addAttribute("assetname", "course");
        model.addAttribute(ASSETS, courseServ.retrieveAll());
        return "/"+LANDLORD+"s/newbie";
    }

    @PostMapping()
    public String load(@ModelAttribute(LANDLORD) Group group, Model model) {
        return "redirect:" + LANDLORD + "s/" + groupServ.enter(group).getId();
    }

    @GetMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id, Model model) {

        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute("assetname", "course");
        model.addAttribute(OWNER, groupServ.retrieveById(id));
        model.addAttribute(ASSETS, courseServ.retrieveAll());
        model.addAttribute("ownedassets", courseServ.retrieveByGroupId(id));
        return "/"+LANDLORD+"s/modification";
    }

    @PatchMapping()
    public String reload(@ModelAttribute(LANDLORD) Group group) {
        return "redirect:" + LANDLORD + "s/" + groupServ.enter(group).getId();
    }
    
    @PatchMapping("/{id}/add")
    public String addAccreditedCourse(@PathVariable("id") Long id,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        courseServ.addToGroup(courseId, id);
        return "redirect:modify";
    }

    @PatchMapping("/{id}/remove")
    public String removeAccreditedCourse(@PathVariable("id") Long id,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        courseServ.removeFromGroup(courseId, id);
        return "redirect:modify";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        groupServ.removeById(id);
        return "redirect:/"+ LANDLORD +"s";
    }

}
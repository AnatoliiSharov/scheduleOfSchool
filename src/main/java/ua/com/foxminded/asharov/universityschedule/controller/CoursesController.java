package ua.com.foxminded.asharov.universityschedule.controller;

import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ua.com.foxminded.asharov.universityschedule.config.Role;
import ua.com.foxminded.asharov.universityschedule.dto.CourseDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

@Controller
@RequestMapping("/courses")
public class CoursesController {
    
    private static final String OWNERNAME = "ownername";
    private static final String LANDLORD = "course";
    private static final String OWNERS = "owners";
    private static final String OWNER = "owner";
    private static final String ASSETS = "assets";

    private final TeacherService teacherServ;
    private final CourseService courseServ;
    private final MapperUtil mapperUtil;

    public CoursesController(TeacherService teacherServ, CourseService courseServ, MapperUtil mapperUtil) {
        this.teacherServ = teacherServ;
        this.courseServ = courseServ;
        this.mapperUtil = mapperUtil;
    }

    @Secured(Role.Recall.VIEWER)
    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNERS, courseServ.retrieveAll());
        return "/courses/selection";
    }

    @Secured(Role.Recall.VIEWER)
    @GetMapping("/{id}")
    public String showSelected(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, courseServ.retrieveById(id));
        model.addAttribute(ASSETS, teacherServ.retrieveAccreditedTeachers(id));
        return "/courses/dashboard";
    }

    @Secured(Role.Recall.CREATOR)
    @GetMapping("/new")
    public String inviteNew(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, new Course());
        return "/" + LANDLORD + "s/newbie";
    }

    @Secured(Role.Recall.CREATOR)
    @PostMapping()
    public String load(@Valid @ModelAttribute(OWNER) CourseDto courseDto, BindingResult result, Model model) {
        
        if(result.hasErrors()) {
            model.addAttribute(OWNERNAME, LANDLORD);
            if(courseDto.getId()!=null) {
                model.addAttribute("assetname", "teacher");
                model.addAttribute(OWNER, courseDto);
                model.addAttribute(ASSETS, teacherServ.retrieveAll());
                model.addAttribute("ownedassets", teacherServ.retrieveAccreditedTeachers(courseDto.getId()));
                return "/courses/modification";
            } else {
                model.addAttribute(OWNER, courseDto);
            return "/" + LANDLORD + "s/newbie";
            }
        }
        return "redirect:courses/" + courseServ.enter(mapperUtil.toEntity(courseDto)).getId();
    }

    @Secured(Role.Recall.CREATOR)
    @GetMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id, Model model) {

        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute("assetname", "teacher");
        model.addAttribute(OWNER, courseServ.retrieveById(id));
        model.addAttribute(ASSETS, teacherServ.retrieveAll());
        model.addAttribute("ownedassets", teacherServ.retrieveAccreditedTeachers(id));
        return "/courses/modification";
    }

    @Secured(Role.Recall.CREATOR)
    @PatchMapping("/{id}/add")
    public String addAccreditedCourse(@RequestParam(value = "courseId", required = false) Long courseId,
            @RequestParam(value = "teacherId", required = false) Long teacherId) {
        courseServ.addAccreditedCourse(teacherId, courseId);
        return "redirect:modify";
    }

    @Secured(Role.Recall.CREATOR)
    @PatchMapping("/{id}/remove")
    public String removeAccreditedCourse(@RequestParam(value = "teacherId", required = false) Long teacherId,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        courseServ.removeAccreditedCourse(teacherId, courseId);
        return "redirect:modify";
    }

    @Secured(Role.Recall.CREATOR)
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        courseServ.removeById(id);
        return "redirect:/courses";
    }

}

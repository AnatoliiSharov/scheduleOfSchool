package ua.com.foxminded.asharov.universityschedule.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ua.com.foxminded.asharov.universityschedule.config.Role;
import ua.com.foxminded.asharov.universityschedule.dto.TeacherDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

import java.util.stream.LongStream;

import javax.validation.Valid;

@Controller
@RequestMapping("/teachers")
public class TeachersController {
    static final String LANDLORD = "teacher";
    static final String OWNER = "owner";
    static final String OWNERNAME = "ownername";
    static final String ASSETS = "assets";
    static final String ASSETSNAME = "assetname";
    static final String COURSE = "course";

    private final TeacherService teacherServ;
    private final CourseService courseServ;
    private final MapperUtil mapperUtil;

    public TeachersController(TeacherService teacherServ, CourseService courseServ, MapperUtil mapperUtil) {
        this.teacherServ = teacherServ;
        this.courseServ = courseServ;
        this.mapperUtil = mapperUtil;
    }

    @Secured(Role.Recall.VIEWER)
    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute("owners", teacherServ.retrieveAll());
        return "/" + LANDLORD + "s/selection";
    }
    
    @Secured(Role.Recall.VIEWER)
    @GetMapping("/{id}")
    public String showSelected(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, teacherServ.retrieveById(id));
        model.addAttribute(ASSETS, courseServ.retrieveByAccreditedTeacher(id));
        return "/" + LANDLORD + "s/dashboard";
    }
    
    @Secured(Role.Recall.CREATOR)
    @GetMapping("/new")
    public String inviteNew(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, new Teacher());
        model.addAttribute(ASSETSNAME, COURSE);
        model.addAttribute(ASSETS, courseServ.retrieveAll());
        return "/" + LANDLORD + "s/newbie";
    }
    
    @Secured(Role.Recall.CREATOR)
    @PostMapping()
    public String load(@Valid @ModelAttribute(OWNER) TeacherDto teacherDto, BindingResult result,
            @RequestParam(value = "ownedCourses", required = false) long[] ownedCourses, Model model) {

        if (result.hasErrors()) {
            model.addAttribute(OWNERNAME, LANDLORD);
            model.addAttribute(ASSETSNAME, COURSE);
            model.addAttribute(ASSETS, courseServ.retrieveAll());
            model.addAttribute(OWNER, teacherDto);
            
            if (teacherDto.getId() != null) {
                model.addAttribute("ownedassets", courseServ.retrieveByAccreditedTeacher(teacherDto.getId()));
                return "/" + LANDLORD + "s/modification";
            } else {
                model.addAttribute(ASSETS, courseServ.retrieveAll());
                return "/" + LANDLORD + "s/newbie";
            }
        }
        Teacher freshTeacher = teacherServ.enter(mapperUtil.toEntity(teacherDto));
        
        if (ownedCourses != null) {
            LongStream.of(ownedCourses)
                    .forEach(courseId -> courseServ.addAccreditedCourse(freshTeacher.getId(), courseId));
        }
        return "redirect:" + LANDLORD + "s/" + freshTeacher.getId();
    }

    @Secured(Role.Recall.CREATOR)
    @GetMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(ASSETSNAME, COURSE);
        model.addAttribute(OWNER, teacherServ.retrieveById(id));
        model.addAttribute(ASSETS, courseServ.retrieveAll());
        model.addAttribute("ownedassets", courseServ.retrieveByAccreditedTeacher(id));
        return "/" + LANDLORD + "s/modification";
    }

    @PatchMapping("/{id}/add")
    @Secured(Role.Recall.CREATOR)
    public String addAccreditedCourse(@RequestParam(value = "teacherId", required = false) Long teacherId,
            @RequestParam(value = "courseId", required = false) Long courseId) {
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
        teacherServ.removeById(id);
        return "redirect:/" + LANDLORD + "s";
    }

}

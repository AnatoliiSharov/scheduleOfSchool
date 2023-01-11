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

import ua.com.foxminded.asharov.universityschedule.model.Teacher;
import ua.com.foxminded.asharov.universityschedule.service.*;

import java.util.stream.LongStream;

@Controller
@RequestMapping("/teachers")
public class TeachersController {
    static final String LANDLORD = "teacher";
    static final String OWNER = "owner";
    static final String OWNERNAME = "ownername";
    static final String ASSETS = "assets";
    
    private final TeacherService teacherServ;
    private final CourseService courseServ;

    public TeachersController(TeacherService teacherServ, CourseService courseServ) {
        this.teacherServ = teacherServ;
        this.courseServ = courseServ;
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute("owners", teacherServ.retrieveAll());
        return "/"+ LANDLORD +"s/selection";
    }

    @GetMapping("/{id}")
    public String showSelected(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, teacherServ.retrieveById(id));
        model.addAttribute(ASSETS, courseServ.retrieveByAccreditedTeacher(id));
        return "/"+LANDLORD+"s/dashboard";
    }
    
    @GetMapping("/new")
    public String inviteNew(Model model) {
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute(OWNER, new Teacher());
        model.addAttribute("assetname", "course");
        model.addAttribute(ASSETS, courseServ.retrieveAll());
        return "/"+LANDLORD+"s/newbie";
    }

    @PostMapping()
    public String load(@ModelAttribute(LANDLORD) Teacher teacher,
            @RequestParam(value = "ownedCourses", required = false) long[] ownedCourses, Model model) {
        Teacher freshTeacher = teacherServ.enter(teacher);
        
        if(ownedCourses!=null) {
        LongStream.of(ownedCourses).forEach(courseId -> courseServ.addAccreditedCourse(freshTeacher.getId(), courseId));
        }
        return "redirect:" + LANDLORD + "s/" + freshTeacher.getId();
    }

    @GetMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id, Model model) {
        
        model.addAttribute(OWNERNAME, LANDLORD);
        model.addAttribute("assetname", "course");
        model.addAttribute(OWNER, teacherServ.retrieveById(id));
        model.addAttribute(ASSETS, courseServ.retrieveAll());
        model.addAttribute("ownedassets", courseServ.retrieveByAccreditedTeacher(id));
        return "/"+LANDLORD+"s/modification";
    }

    @PatchMapping()
    public String reload(@ModelAttribute(LANDLORD) Teacher teacher) {
        return "redirect:" + LANDLORD + "s/" + (teacherServ.enter(teacher)).getId();
    }

    @PatchMapping("/{id}/add")
    public String addAccreditedCourse(@RequestParam(value = "teacherId", required = false) Long teacherId, @RequestParam(value = "courseId", required = false) Long courseId) {
        courseServ.addAccreditedCourse(teacherId, courseId);
        return "redirect:modify";
    }

    @PatchMapping("/{id}/remove")
    public String removeAccreditedCourse(@RequestParam(value = "teacherId", required = false) Long teacherId, @RequestParam(value = "courseId", required = false) Long courseId) {
        courseServ.removeAccreditedCourse(teacherId, courseId);
        return "redirect:modify";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        teacherServ.removeById(id);
        return "redirect:/"+ LANDLORD +"s";
    }

}

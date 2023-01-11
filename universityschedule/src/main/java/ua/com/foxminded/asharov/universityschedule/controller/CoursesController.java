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

import ua.com.foxminded.asharov.universityschedule.model.Course;
import ua.com.foxminded.asharov.universityschedule.service.CourseService;
import ua.com.foxminded.asharov.universityschedule.service.TeacherService;

@Controller
@RequestMapping("/courses")
public class CoursesController {
    private static final  String OWNERNAME = "ownername";
    private static final String COURSE = "course";
    private static final String OWNERS = "owners";
    private static final String OWNER = "owner";
    
    private final TeacherService teacherServ;
    private final CourseService courseServ;

    public CoursesController(TeacherService teacherServ, CourseService courseServ) {
        this.teacherServ = teacherServ;
        this.courseServ = courseServ;
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute(OWNERNAME, COURSE);
        model.addAttribute(OWNERS, courseServ.retrieveAll());
        return "/courses/selection";
    }

    @GetMapping("/{id}")
    public String showSelected(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, COURSE);
        model.addAttribute(OWNER, courseServ.retrieveById(id));
        model.addAttribute("assets", teacherServ.retrieveAccreditedTeachers(id));
        return "/courses/dashboard";
    }

    @GetMapping("/new")
    public String inviteNew(Model model) {
        model.addAttribute(OWNERNAME, COURSE);
        model.addAttribute(OWNER, new Course());
        return "/courses/newbie";
    }

    @PostMapping()
    public String load(@ModelAttribute(COURSE) Course course,
            @RequestParam(value = "ownedCourses", required = false) long[] ownedCourses, Model model) {
        return "redirect:courses/" + courseServ.enter(course).getId();
    }

    @GetMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id, Model model) {
        
        model.addAttribute(OWNERNAME, COURSE);
        model.addAttribute("assetname", "teacher");
        model.addAttribute(OWNER, courseServ.retrieveById(id));
        model.addAttribute("assets", teacherServ.retrieveAll());
        model.addAttribute("ownedassets", teacherServ.retrieveAccreditedTeachers(id));
        return "/courses/modification";
    }

    @PatchMapping()
    public String reload(@ModelAttribute(COURSE) Course course) {
        courseServ.enter(course);
        return "redirect:courses/" + course.getId();
    }

    @PatchMapping("/{id}/add")
    public String addAccreditedCourse(@RequestParam(value = "courseId", required = false) Long courseId, @RequestParam(value = "teacherId", required = false) Long teacherId) {
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
        courseServ.removeById(id);
        return "redirect:/courses";
    }

}
package ua.com.foxminded.asharov.universityschedule.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.asharov.universityschedule.model.Group;
import ua.com.foxminded.asharov.universityschedule.model.Student;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentsController {
    static final String OWNERNAME = "ownername";  
    static final String STUDENT = "student";  
    static final String OWNER = "owner";  
    static final String ASSETS = "assets";  
    
    private final GroupService groupServ;
    private final StudentService studentServ;

    public StudentsController(GroupService groupServ, StudentService studentServ) {
        this.groupServ = groupServ;
        this.studentServ = studentServ;
    }

    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute(OWNERNAME, STUDENT);
        model.addAttribute("owners", studentServ.retrieveAll());
        return "/students/selection";
    }

    @GetMapping("/{id}")
    public String showSelected(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, STUDENT);
        model.addAttribute(OWNER, studentServ.retrieveById(id));
        model.addAttribute(ASSETS, groupServ.retrieveGroupByStudentId(id));
        return "/students/dashboard";
    }
    
    @GetMapping("/new")
    public String inviteNew(Model model) {
        model.addAttribute(OWNERNAME, STUDENT);
        model.addAttribute(OWNER, new Student());
        model.addAttribute("assetname", "group");
        model.addAttribute(ASSETS, groupServ.retrieveAll());
        return "/students/newbie";
    }

    @PostMapping()
    public String load(@ModelAttribute(STUDENT) Student student, Model model) {
        return "redirect:students/" + studentServ.enter(student).getId();
    }

    @GetMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id, Model model) {
        Group ownedAsset = groupServ.retrieveGroupByStudentId(id);
        List<Group>assets = groupServ.retrieveAll();
        
        assets.remove(ownedAsset);
        model.addAttribute(OWNERNAME, STUDENT);
        model.addAttribute("assetname", "group");
        model.addAttribute(OWNER, studentServ.retrieveById(id));
        model.addAttribute(ASSETS, assets);
        model.addAttribute("ownedassets", ownedAsset);
        return "/students/modification";
    }

    @PatchMapping()
    public String reload(@ModelAttribute(STUDENT) Student student) {
        return "redirect:students/" + studentServ.enter(student).getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        studentServ.removeById(id);
        return "redirect:/students";
    }

}
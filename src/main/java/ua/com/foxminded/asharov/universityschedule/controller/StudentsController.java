package ua.com.foxminded.asharov.universityschedule.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.asharov.universityschedule.config.Role;
import ua.com.foxminded.asharov.universityschedule.dto.StudentDto;
import ua.com.foxminded.asharov.universityschedule.dto.util.MapperUtil;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.service.GroupService;
import ua.com.foxminded.asharov.universityschedule.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentsController {
    static final String OWNERNAME = "ownername";  
    static final String STUDENT = "student";  
    static final String OWNER = "owner";
    static final String ASSETS = "assets";  
    static final String ASSETNAME = "assetname";  
    static final String GROUP = "group";  
    
    private final GroupService groupServ;
    private final StudentService studentServ;
    private final MapperUtil mapperUtil;

    public StudentsController(GroupService groupServ, StudentService studentServ, MapperUtil mapperUtil) {
        this.groupServ = groupServ;
        this.studentServ = studentServ;
        this.mapperUtil = mapperUtil;
    }

    @Secured(Role.Recall.VIEWER)
    @GetMapping()
    public String showAll(Model model) {
        model.addAttribute(OWNERNAME, STUDENT);
        model.addAttribute("owners", studentServ.retrieveAll());
        return "/students/selection";
    }

    @Secured(Role.Recall.VIEWER)
    @GetMapping("/{id}")
    public String showSelected(@PathVariable("id") Long id, Model model) {
        model.addAttribute(OWNERNAME, STUDENT);
        model.addAttribute(OWNER, studentServ.retrieveById(id));
        model.addAttribute(ASSETS, groupServ.retrieveGroupByStudentId(id));
        return "/students/dashboard";
    }
    
    @Secured(Role.Recall.CREATOR)
    @GetMapping("/new")
    public String inviteNew(Model model) {
        model.addAttribute(OWNERNAME, STUDENT);
        model.addAttribute(OWNER, new StudentDto());
        model.addAttribute(ASSETNAME, GROUP);
        model.addAttribute(ASSETS, groupServ.retrieveAll());
        return "/students/newbie";
    }

    @Secured(Role.Recall.CREATOR)
    @PostMapping()
    public String load(@Valid @ModelAttribute(OWNER) StudentDto studentDto, BindingResult result, Model model) {
        List<Group>assets = groupServ.retrieveAll();
        
        model.addAttribute(OWNER, studentDto);
        model.addAttribute(OWNERNAME, STUDENT);
        model.addAttribute(ASSETS, assets);
        model.addAttribute(ASSETNAME, GROUP);
        
        if(result.hasErrors()) {
            
            if(studentDto.getId()!=null) {
                Group ownedAsset = groupServ.retrieveById(studentDto.getGroupId());
                
                assets.remove(ownedAsset);
                model.addAttribute("ownedassets", ownedAsset);
                return "/students/modification";
            }else {
                return "/students/newbie";
            }
         }
         return "redirect:students/" + studentServ.enter(mapperUtil.toEntity(studentDto)).getId();
    }

    @Secured(Role.Recall.CREATOR)
    @GetMapping("/{id}/modify")
    public String modify(@PathVariable("id") Long id, Model model) {
        Group ownedAsset = studentServ.retrieveById(id).getGroup();
        List<Group>assets = groupServ.retrieveAll();
        
        assets.remove(ownedAsset);
        model.addAttribute(OWNERNAME, STUDENT);
        model.addAttribute(ASSETNAME, GROUP);
        model.addAttribute(OWNER,  mapperUtil.toDto(studentServ.retrieveById(id)));
        model.addAttribute(ASSETS, assets);
        model.addAttribute("ownedassets", ownedAsset);
        return "/students/modification";
    }

    @Secured(Role.Recall.CREATOR)
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        studentServ.removeById(id);
        return "redirect:/students";
    }

}
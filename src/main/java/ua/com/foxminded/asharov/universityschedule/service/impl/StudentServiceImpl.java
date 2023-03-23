package ua.com.foxminded.asharov.universityschedule.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.entity.Student;
import ua.com.foxminded.asharov.universityschedule.exception.UniversityException;
import ua.com.foxminded.asharov.universityschedule.repository.GroupRepository;
import ua.com.foxminded.asharov.universityschedule.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements ua.com.foxminded.asharov.universityschedule.service.StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRep;
    private final GroupRepository groupRep;

    public StudentServiceImpl(StudentRepository studentRep, GroupRepository groupRep) {
        this.studentRep = studentRep;
        this.groupRep = groupRep;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> retrieveAll() {
        logger.debug("retrieveAllStudents from StudentServiceImpl started");
        List<Student> result = new ArrayList<>();
        
        studentRep.findAll().forEach(result::add);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> retrieveByGroupId(Long groupId) {
        logger.debug("retrieveByGroupId from StudentServiceImpl started by groupId = {}", groupId);
        return studentRep.findByGroup(groupRep.findById(groupId).orElseThrow(() -> new UniversityException(
                "don't find Student with StudentServiceImpl/retrieveByGroupId/findById when groupId = " + groupId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Student retrieveById(Long id) {
        logger.debug("retrieveById from StudentServiceImpl started with studentId = {}", id);
        return studentRep.findById(id).orElseThrow(() -> new UniversityException(
                "don't find Student with StudentServiceImpl/retrieveById/findById when id = " + id));
    }

    @Override
    public Student enter(Student student) {
        logger.debug("enter from StudentServiceImpl started with Student = {}", student);
        return studentRep.save(student);
    }

    @Override
    public void removeById(Long id) {
        logger.debug("removeById from StudentServiceImpl started with studentId = {}", id);

        if (studentRep.existsById(id)) {
            studentRep.deleteById(id);
        } else {
            throw new UniversityException(
                    "don't find Student with StudentServiceImpl/removeById/existsById when id = " + id);
        }
    }

}

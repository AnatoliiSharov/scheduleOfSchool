package ua.com.foxminded.asharov.universityschedule.servise.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.dao.StudentDao;
import ua.com.foxminded.asharov.universityschedule.model.Student;

@Service
public class StudentServiceImpl implements ua.com.foxminded.asharov.universityschedule.service.StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> retrieveAll() {
        logger.debug("retrieveAllStudents from StudentServiceImpl started");
        return studentDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> retrieveByGroupId(Long groupId) {
        logger.debug("retrieveByGroupId from StudentServiceImpl started by groupId = {}", groupId);
        return studentDao.findListByGroupId(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public Student retrieveById(Long id) {
        logger.debug("retrieveById from StudentServiceImpl started with studentId = {}", id);
        return studentDao.findById(id).orElse(new Student());
    }

    @Override
    public List<Student> retrieveByName(String firstName, String lastName) {
        logger.debug("retrieveByName from StudentServiceImpl started with firstName= {}, lastName = {}", firstName,
                lastName);
        return studentDao.findByFullName(firstName, lastName);
    }

    @Override
    public Student enter(Student student) {
        logger.debug("enter from StudentServiceImpl started with Student = {}", student);
        return studentDao.save(student);
    }

    @Override
    public void removeById(Long id) {
        logger.debug("removeById from StudentServiceImpl started with studentId = {}", id);
        studentDao.deleteById(id);
    }

}

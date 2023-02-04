package ua.com.foxminded.asharov.universityschedule.service;

import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.asharov.universityschedule.entity.Student;

import java.util.List;

public interface StudentService {
    @Transactional(readOnly = true)
    List<Student> retrieveAll();

    @Transactional(readOnly = true)
    List<Student> retrieveByGroupId(Long groupId);

    @Transactional(readOnly = true)
    Student retrieveById(Long id);

    @Transactional
    Student enter(Student student);

    @Transactional
    void removeById(Long id);

}

package ua.com.foxminded.asharov.universityschedule.dao;

import java.util.List;

import ua.com.foxminded.asharov.universityschedule.model.Student;

public interface StudentDao extends CrudEntityDao<Student, Long> {

    public List<Student> findByFullName(String firstName, String lastName);

    public List<Student> findListByGroupId(Long groupId);

}

package ua.com.foxminded.asharov.universityschedule.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Student;
import ua.com.foxminded.asharov.universityschedule.repository.custom.CustomStudentRepository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface StudentRepository extends CustomStudentRepository, CrudRepository<Student, Long> {

    List<Student> findByGroup(Group group);

}

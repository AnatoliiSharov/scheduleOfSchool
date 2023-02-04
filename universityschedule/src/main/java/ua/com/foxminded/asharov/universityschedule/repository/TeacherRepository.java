package ua.com.foxminded.asharov.universityschedule.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;
import ua.com.foxminded.asharov.universityschedule.repository.custom.CustomTeacherRepository;

@Repository
@EnableJpaRepositories
public interface TeacherRepository extends CustomTeacherRepository, CrudRepository<Teacher, Long> {

}

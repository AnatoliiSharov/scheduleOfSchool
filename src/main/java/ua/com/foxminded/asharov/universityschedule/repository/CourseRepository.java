package ua.com.foxminded.asharov.universityschedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.entity.Course;
import ua.com.foxminded.asharov.universityschedule.repository.custom.CustomCourseRepository;

@Repository
@EnableJpaRepositories
public interface CourseRepository extends CustomCourseRepository, JpaRepository<Course, Long> {

}

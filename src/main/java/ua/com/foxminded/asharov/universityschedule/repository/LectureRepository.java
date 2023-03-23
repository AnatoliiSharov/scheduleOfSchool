package ua.com.foxminded.asharov.universityschedule.repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.asharov.universityschedule.entity.Group;
import ua.com.foxminded.asharov.universityschedule.entity.Lecture;
import ua.com.foxminded.asharov.universityschedule.entity.Room;
import ua.com.foxminded.asharov.universityschedule.entity.Teacher;

import java.time.LocalDate;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface LectureRepository extends  CrudRepository<Lecture, Long> {
    
    List<Lecture> findAllByDateBetweenAndGroup(LocalDate startDate, LocalDate finishDate, Group group);

    List<Lecture> findAllByDateBetweenAndTeacher(LocalDate startDate, LocalDate finishDate, Teacher teacher);

    List<Lecture> findAllByDateBetweenAndRoom(LocalDate startDate, LocalDate finishDate, Room room);

}

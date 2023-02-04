package ua.com.foxminded.asharov.universityschedule.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static ua.com.foxminded.asharov.universityschedule.entity.Course.COURSE_ID;
import static ua.com.foxminded.asharov.universityschedule.entity.Teacher.TEACHER_ID;

@Entity
@Table(name = "teachers_courses")
@NoArgsConstructor
@Data
public class Accreditation {

    @EmbeddedId
    AccreditationKey id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = COURSE_ID, nullable = false)
    Course course;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = TEACHER_ID, nullable = false)
    Teacher teacher;

    public Accreditation(Course course, Teacher teacher) {
        this.id = new AccreditationKey(course.getId(), teacher.getId());
        this.course = course;
        this.teacher = teacher;
    }

}

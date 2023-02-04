package ua.com.foxminded.asharov.universityschedule.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static ua.com.foxminded.asharov.universityschedule.entity.Course.COURSE_ID;
import static ua.com.foxminded.asharov.universityschedule.entity.Group.GROUP_ID;

@Entity
@Table(name = "courses_groups")
@NoArgsConstructor
@Data
public class Education {

    @EmbeddedId
    EducationKey id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = GROUP_ID, nullable = false)
    Group group;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = COURSE_ID, nullable = false)
    Course course;

    public Education(Group group, Course course) {
        this.id = new EducationKey(group.getId(), course.getId());
        this.course = course;
        this.group = group;
    }

}

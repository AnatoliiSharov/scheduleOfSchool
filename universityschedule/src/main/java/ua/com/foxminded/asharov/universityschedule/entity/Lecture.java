package ua.com.foxminded.asharov.universityschedule.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "lectures", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "day_lecture_number", "date_lecture", "room_id" }),
        @UniqueConstraint(columnNames = { "day_lecture_number", "date_lecture", "teacher_id" }),
        @UniqueConstraint(columnNames = { "day_lecture_number", "date_lecture", "group_id" }) })
@NoArgsConstructor
@Data
@Builder
public class Lecture {
    public static final String LECTURE_ID = "lecture_id";
    public static final String NUMBER_OF_LECTURE_PER_DAY = "day_lecture_number";
    public static final String DATE_OF_LECTURE = "date_lecture";
    public static final String ROOM_ID = "room_id";
    public static final String TEACHER_ID = "teacher_id";
    public static final String COURSE_ID = "course_id";
    public static final String GROUP_ID = "group_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = LECTURE_ID)
    private Long id;

    @Column(name = NUMBER_OF_LECTURE_PER_DAY, nullable = false)
    private Integer serialNumberPerDay;

    @Column(name = DATE_OF_LECTURE, nullable = false)
    private LocalDate date;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = ROOM_ID, nullable = false)
    private Room room;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = TEACHER_ID, nullable = false)
    private Teacher teacher;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = COURSE_ID, nullable = false)
    private Course course;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = GROUP_ID, nullable = false)
    private Group group;

    public Lecture(Long id, Integer serialNumberPerDay, LocalDate date, Room room, Teacher teacher, Course course,
            Group group) {
        super();
        this.id = id;
        this.serialNumberPerDay = serialNumberPerDay;
        this.date = date;
        this.room = room;
        this.teacher = teacher;
        this.course = course;
        this.group = group;
    }

    public Lecture(Integer serialNumberPerDay, LocalDate date, Room room, Teacher teacher, Course course, Group group) {
        super();
        this.id = null;
        this.serialNumberPerDay = serialNumberPerDay;
        this.date = date;
        this.room = room;
        this.teacher = teacher;
        this.course = course;
        this.group = group;
    }

}

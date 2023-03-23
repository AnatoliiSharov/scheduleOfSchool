package ua.com.foxminded.asharov.universityschedule.model;

import java.time.LocalDate;
import java.util.Objects;

public class Lecture extends AbstractEntity<Long> implements Comparable<Lecture> {

    public static final String LECTURE_TABLE_NAME = "lectures";
    public static final String LECTURE_ID = "lecture_id";
    public static final String NUMBER_OF_LECTURE_PER_DAY = "day_lecture_number";
    public static final String DATE_OF_LECTURE = "date_lecture";
    public static final String ROOM_ID = "room_id";
    public static final String TEACHER_ID = "teacher_id";
    public static final String COURSE_ID = "course_id";
    public static final String GROUP_ID = "group_id";

    private Integer serialNumberPerDay;
    private LocalDate date;
    private Long teacherId;
    private Long roomId;
    private Long courseId;
    private Long groupId;

    public Lecture(Long id, Integer serialNumberPerDay, LocalDate date, Long roomId, Long teacherId, Long courseId,
                   Long groupId) {
        super(id);
        this.serialNumberPerDay = serialNumberPerDay;
        this.date = date;
        this.roomId = roomId;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.groupId = groupId;
    }

    public Lecture(Integer serialNumberPerDay, LocalDate date, Long roomId, Long teacherId, Long courseId,
                   Long groupId) {
        this(null, serialNumberPerDay, date, roomId, teacherId, courseId, groupId);
    }

    public Lecture(Long roomId, Long teacherId, Long courseId, Long groupId) {
        this(null, null, roomId, teacherId, courseId, groupId);
    }

    public Lecture() {
        this(null, null, null, null, null, null, null);
    }

    public Lecture(Integer serialNumberPerDay, LocalDate date) {
        this(serialNumberPerDay, date, null, null, null, null);
    }

    public Lecture(LocalDate date) {
        this(null, date, null, null, null, null);
    }

    public Lecture(long id, Integer serialNumberPerDay, Long roomId, Long teacherId, Long courseId, Long groupId) {
        this(id, serialNumberPerDay, null, roomId, teacherId, courseId, groupId);
    }

    public Integer getSerialNumberPerDay() {
        return serialNumberPerDay;
    }

    public void setSerialNumberPerDay(Integer dayLectureNumber) {
        this.serialNumberPerDay = dayLectureNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public int compareTo(Lecture o) {
        return (int) (this.getId() - o.getId());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(courseId, date, groupId, roomId, serialNumberPerDay, teacherId);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lecture other = (Lecture) obj;
        return Objects.equals(courseId, other.courseId) && Objects.equals(date, other.date)
            && Objects.equals(groupId, other.groupId) && Objects.equals(roomId, other.roomId)
            && Objects.equals(serialNumberPerDay, other.serialNumberPerDay)
            && Objects.equals(teacherId, other.teacherId);
    }

    @Override
    public String toString() {
        return "Lecture [serialNumberPerDay=" + serialNumberPerDay + ", date=" + date + ", teacherId=" + teacherId
            + ", roomId=" + roomId + ", courseId=" + courseId + ", groupId=" + groupId + "]";
    }

}

CREATE TABLE teachers_courses
(
    teacher_id BIGINT NOT NULL,
    course_id  BIGINT NOT NULL,
    UNIQUE (teacher_id, course_id)
);

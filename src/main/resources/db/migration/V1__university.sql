CREATE TABLE groups
(
    group_id   BIGSERIAL PRIMARY KEY,
    group_name CHARACTER VARYING(30) UNIQUE NOT NULL
);
CREATE TABLE students
(
    student_id        BIGSERIAL PRIMARY KEY,
    group_id          BIGINT REFERENCES groups (group_id) ON DELETE CASCADE,
    student_firstname CHARACTER VARYING(1478) NOT NULL,
    student_lastname  CHARACTER VARYING(700)  NOT NULL
);
CREATE TABLE courses
(
    course_id          BIGSERIAL PRIMARY KEY,
    course_name        CHARACTER VARYING(30) UNIQUE NOT NULL,
    course_description TEXT                         NOT NULL
);
CREATE TABLE courses_groups
(
    course_id BIGINT REFERENCES courses (course_id) ON DELETE CASCADE NOT NULL,
    group_id  BIGINT REFERENCES groups (group_id) ON DELETE CASCADE   NOT NULL,
    UNIQUE (course_id, group_id)
);
CREATE TABLE teachers
(
    teacher_id        BIGSERIAL PRIMARY KEY,
    teacher_firstname CHARACTER VARYING(1478) NOT NULL,
    teacher_lastname  CHARACTER VARYING(700)  NOT NULL
);
CREATE TABLE rooms
(
    room_id       BIGSERIAL PRIMARY KEY,
    room_address  CHARACTER VARYING(30) NOT NULL,
    room_capacity INTEGER               NOT NULL
);
CREATE TABLE lectures
(
    lecture_id         BIGSERIAL PRIMARY KEY,
    day_lecture_number INTEGER                                                   NOT NULL,
    date_lecture       DATE                                                      NOT NULL,
    room_id            BIGINT REFERENCES rooms (room_id) ON DELETE CASCADE       NOT NULL,
    teacher_id         BIGINT REFERENCES teachers (teacher_id) ON DELETE CASCADE NOT NULL,
    course_id          BIGINT REFERENCES courses (course_id) ON DELETE CASCADE   NOT NULL,
    group_id           BIGINT REFERENCES groups (group_id) ON DELETE CASCADE     NOT NULL,
    UNIQUE (day_lecture_number, date_lecture, room_id),
    UNIQUE (day_lecture_number, date_lecture, teacher_id),
    UNIQUE (day_lecture_number, date_lecture, group_id)
);

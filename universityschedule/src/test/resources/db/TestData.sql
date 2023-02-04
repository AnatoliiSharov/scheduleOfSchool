INSERT INTO groups(group_id, group_name)
VALUES (10001, 'AA-11');
INSERT INTO groups(group_id, group_name)
VALUES (10002, 'AA-22');
INSERT INTO groups(group_id, group_name)
VALUES (10003, 'AA-33');

INSERT INTO students(student_id, group_id, student_firstname, student_lastname)
VALUES (10001, 10001, 'FirstName1', 'LastName1');
INSERT INTO students(student_id, group_id, student_firstname, student_lastname)
VALUES (10002, 10002, 'FirstName2', 'LastName2');
INSERT INTO students(student_id, group_id, student_firstname, student_lastname)
VALUES (10003, 10002, 'FirstName3', 'LastName3');
INSERT INTO students(student_id, group_id, student_firstname, student_lastname)
VALUES (10004, 10003, 'FirstName4', 'LastName4');
INSERT INTO students(student_id, group_id, student_firstname, student_lastname)
VALUES (10005, 10003, 'FirstName5', 'LastName5');
INSERT INTO students(student_id, group_id, student_firstname, student_lastname)
VALUES (10006, 10003, 'FirstName6', 'LastName6');
INSERT INTO students(student_id, student_firstname, student_lastname)
VALUES (10007, 'FirstName7', 'LastName7');

INSERT INTO courses(course_id, course_name, course_description)
VALUES (10001, 'Course1', 'Description1');
INSERT INTO courses(course_id, course_name, course_description)
VALUES (10002, 'Course2', 'Description2');
INSERT INTO courses(course_id, course_name, course_description)
VALUES (10003, 'Course3', 'Description3');
INSERT INTO courses(course_id, course_name, course_description)
VALUES (10004, 'Course4', 'Description4');
INSERT INTO courses(course_id, course_name, course_description)
VALUES (10005, 'Course5', 'Description5');

INSERT INTO teachers(teacher_id, teacher_firstname, teacher_lastname)
VALUES (10001, 'teacherFirstName1', 'teacherLastName1');
INSERT INTO teachers(teacher_id, teacher_firstname, teacher_lastname)
VALUES (10002, 'teacherFirstName2', 'teacherLastName2');
INSERT INTO teachers(teacher_id, teacher_firstname, teacher_lastname)
VALUES (10003, 'teacherFirstName3', 'teacherLastName3');

INSERT INTO rooms(room_id, room_address, room_capacity)
VALUES (10001, 'Str, bldg1, fl1, off1', 60);
INSERT INTO rooms(room_id, room_address, room_capacity)
VALUES (10002, 'Str, bldg1, fl1, off2', 20);
INSERT INTO rooms(room_id, room_address, room_capacity)
VALUES (10003, 'Str, bldg1, fl2, off3', 30);
INSERT INTO rooms(room_id, room_address, room_capacity)
VALUES (10004, 'Str, bldg2, fl1, off1', 10);
INSERT INTO rooms(room_id, room_address, room_capacity)
VALUES (10005, 'Str, bldg2, fl2, off2', 20);

INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10001, 1, '0001-01-01', 10001, 10001, 10001, 10001);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10002, 2, '0001-01-01', 10002, 10002, 10002, 10001);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10003, 3, '0001-01-01', 10003, 10003, 10003, 10001);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10004, 1, '0001-01-01', 10004, 10003, 10003, 10002);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10005, 2, '0001-01-01', 10004, 10001, 10001, 10002);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10006, 3, '0001-01-01', 10004, 10002, 10002, 10002);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10007, 1, '0001-01-02', 10004, 10001, 10001, 10001);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10008, 2, '0001-01-02', 10004, 10002, 10002, 10001);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10009, 3, '0001-01-02', 10004, 10003, 10003, 10001);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10010, 1, '0001-01-02', 10001, 10003, 10003, 10002);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10011, 2, '0001-01-02', 10002, 10001, 10001, 10002);
INSERT INTO lectures(lecture_id, day_lecture_number, date_lecture, room_id, teacher_id, course_id, group_id)
VALUES (10012, 3, '0001-01-02', 10003, 10002, 10002, 10002);

INSERT INTO teachers_courses(teacher_id, course_id)
VALUES (10001, 10001);
INSERT INTO teachers_courses(teacher_id, course_id)
VALUES (10001, 10002);
INSERT INTO teachers_courses(teacher_id, course_id)
VALUES (10002, 10001);
INSERT INTO teachers_courses(teacher_id, course_id)
VALUES (10002, 10002);
INSERT INTO teachers_courses(teacher_id, course_id)
VALUES (10002, 10003);
INSERT INTO teachers_courses(teacher_id, course_id)
VALUES (10002, 10004);
INSERT INTO teachers_courses(teacher_id, course_id)
VALUES (10002, 10005);
INSERT INTO teachers_courses(teacher_id, course_id)
VALUES (10003, 10003);
INSERT INTO teachers_courses(teacher_id, course_id)
VALUES (10003, 10004);
INSERT INTO teachers_courses(teacher_id, course_id)
VALUES (10003, 10005);

INSERT INTO courses_groups(course_id, group_id)
VALUES (10001, 10001);
INSERT INTO courses_groups(course_id, group_id)
VALUES (10001, 10002);
INSERT INTO courses_groups(course_id, group_id)
VALUES (10001, 10003);
INSERT INTO courses_groups(course_id, group_id)
VALUES (10002, 10002);
INSERT INTO courses_groups(course_id, group_id)
VALUES (10003, 10003);
INSERT INTO courses_groups(course_id, group_id)
VALUES (10004, 10002);
INSERT INTO courses_groups(course_id, group_id)
VALUES (10004, 10003);
INSERT INTO courses_groups(course_id, group_id)
VALUES (10005, 10001);

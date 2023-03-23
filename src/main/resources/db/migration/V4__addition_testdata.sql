TRUNCATE TABLE students CASCADE;
TRUNCATE TABLE groups CASCADE;
TRUNCATE TABLE courses CASCADE;
TRUNCATE TABLE teachers CASCADE;
TRUNCATE TABLE rooms CASCADE;
TRUNCATE TABLE lectures CASCADE;
TRUNCATE TABLE courses_groups CASCADE;
TRUNCATE TABLE teachers_courses CASCADE;
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (1, 'the branch of magic and science that studied stars and the movement of planets', 'Astronomy');
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (2, 'the study of spells concerned with giving an object new and unexpected properties', 'Charms');
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (3, 'the class that taught students defensive techniques to defend against the Dark Arts, and to be protected from dark creatures', 'Defence Against the Dark Arts');
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (4, 'the study of magical plants and how to take care of, utilise and combat them', 'Herbology');
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (5, 'the study of magical history', 'History of Magic');
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (6, 'the art of creating mixtures with magical effects', 'Potions');
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (7, 'the class that taught the use of broomsticks made for the use of flying', 'Transfiguration');
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (8, 'the study of the Muggle (non-magical) culture "from a wizarding point of view", including Muggle Art and Muggle Music', 'Muggle Studies');
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (9, 'the art of predicting the future, including tea leaves, fire-omens, crystal balls, palmistry, cartomancy (including the reading of conventional playing cards and the tarot), astrology and dream interpretations', 'Divination');
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (10, 'the composite study between Transfiguration, Potions and Muggle Chemistry, focused roughly on the transmutation of substances into other forms', 'Alchemy');
INSERT INTO public.courses (course_id, course_description, course_name) VALUES (11, 'quiz', 'charades');

INSERT INTO public.groups (group_id, group_name) VALUES (1, 'Gryffindor');
INSERT INTO public.groups (group_id, group_name) VALUES (2, 'Hufflepuff');
INSERT INTO public.groups (group_id, group_name) VALUES (3, 'Ravenclaw');
INSERT INTO public.groups (group_id, group_name) VALUES (4, 'Slytherin');

INSERT INTO public.courses_groups (course_id, group_id) VALUES (1, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (2, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (3, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (4, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (5, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (6, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (7, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (8, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (9, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (10, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (11, 1);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (1, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (2, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (3, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (4, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (5, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (6, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (7, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (8, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (9, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (10, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (11, 2);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (1, 4);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (2, 4);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (3, 4);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (4, 4);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (5, 4);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (6, 4);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (7, 4);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (8, 4);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (9, 4);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (10, 4);
INSERT INTO public.courses_groups (course_id, group_id) VALUES (11, 4);

INSERT INTO public.rooms (room_id, room_address, room_capacity) VALUES (1, 'address1', 10);
INSERT INTO public.rooms (room_id, room_address, room_capacity) VALUES (2, 'address2', 20);
INSERT INTO public.rooms (room_id, room_address, room_capacity) VALUES (3, 'address3', 30);
INSERT INTO public.rooms (room_id, room_address, room_capacity) VALUES (4, 'address4', 40);

INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (1, 'Albus', 'Dumbledore');
INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (2, 'Filius', 'Flitwick');
INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (3, 'Gilderoy', 'Lockhart');
INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (4, 'Minerva', 'McGonagall');
INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (5, 'Pomona', 'Sprout');
INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (6, 'Quirinus', 'Quirrell');
INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (7, 'Remus', 'Lupin');
INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (8, 'Rubeus', 'Hagrid');
INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (9, 'Severus', 'Snape');
INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (10, 'Sybill', 'Trelawney');
INSERT INTO public.teachers (teacher_id, teacher_firstname, teacher_lastname) VALUES (11, 'Anatolii', 'Sharov');

INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (1, 'George', 'Washington', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (2, 'John', 'Adams', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (3, 'Thomas', 'Jefferson', 2);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (4, 'James', 'Madison', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (5, 'James', 'Monroe', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (6, 'John Quincy', 'Adams', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (7, 'Andrew', 'Jackson', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (8, 'Martin', 'Van Buren', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (9, 'William Henry', 'Harrison', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (10, 'John', 'Tyler', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (11, 'James K.', 'Polk', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (12, 'Zachary', 'Taylor', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (13, 'Millard', 'Fillmore', 2);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (14, 'Franklin', 'Pierce', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (15, 'James', 'Buchanan', 2);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (16, 'Abraham', 'Lincoln', 2);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (17, 'Andrew', 'Johnson', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (18, 'Ulysses S.', 'Grant', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (19, 'Rutherford B.', 'Hayes', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (20, 'James A.', 'Garfield', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (21, 'Chester A.', 'Arthur', 2);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (22, 'Grover', 'Cleveland', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (23, 'Benjamin', 'Harrison', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (24, 'Grover', 'Cleveland', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (25, 'William', 'McKinley', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (26, 'Theodore', 'Roosevelt', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (27, 'William Howard', 'Taft', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (28, 'Woodrow', 'Wilson', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (29, 'Warren G.', 'Harding', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (30, 'Calvin', 'Coolidge', 2);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (31, 'Herbert', 'Hoover', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (32, 'Franklin D.', 'Roosevelt', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (33, 'Harry S.', 'Truman', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (34, 'Dwight D.', 'Eisenhower', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (35, 'John F.', 'Kennedy', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (36, 'Lyndon B.', 'Johnson', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (37, 'Richard', 'Nixon', 2);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (38, 'Gerald', 'Ford', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (39, 'Jimmy', 'Carter', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (40, 'Ronald', 'Reagan', 2);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (41, 'George H.W.', 'Bush', 2);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (42, 'Bill', 'Clinton', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (43, 'George W.', 'Bush', 4);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (44, 'Barack', 'Obama', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (45, 'Donald', 'Trump', 1);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (46, 'Joe', 'Biden', 3);
INSERT INTO public.students (student_id, student_firstname, student_lastname, group_id) VALUES (47, 'sharov', 'anatolii', 4);

INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (1, 11);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (2, 11);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (3, 11);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (4, 11);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (2, 1);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (3, 1);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (4, 1);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (5, 1);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (6, 1);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (7, 1);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (8, 1);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (9, 1);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (10, 1);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (11, 1);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (7, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (8, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (9, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (10, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (11, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (1, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (2, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (3, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (4, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (5, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (6, 2);
INSERT INTO public.teachers_courses (course_id, teacher_id) VALUES (1, 1);
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

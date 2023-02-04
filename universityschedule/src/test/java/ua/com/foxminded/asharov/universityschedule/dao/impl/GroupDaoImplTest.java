package ua.com.foxminded.asharov.universityschedule.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.jdbc.Sql;
import ua.com.foxminded.asharov.universityschedule.dao.GroupDao;
import ua.com.foxminded.asharov.universityschedule.model.Group;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupDaoImplTest {

    @Autowired
    JdbcTemplate template;

    @Autowired
    NamedParameterJdbcTemplate namedParamTemplate;
    SimpleJdbcInsert simpleInsert;
    GroupDao groupDao;

    @BeforeEach
    void setUp() {
        groupDao = new GroupDaoImpl(template, namedParamTemplate);
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindAll_shouldRetrieveAllRowGroups_whenMakeFindAllAndDataExist() {
        List<Group> expected = Arrays.asList(new Group(10001L, "AA-11"), new Group(10002L, "AA-22"),
            new Group(10003L, "AA-33"));

        assertEquals(expected, groupDao.findAll());
    }

    @ParameterizedTest
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    @MethodSource("provideFindeById")
    void testFindById(Long groupId, Group expected, boolean intermediatResult) {
        Optional<Group> actual = groupDao.findById(groupId);

        assertEquals(actual.isPresent(), intermediatResult);
        assertEquals(expected, actual.orElse(expected));
    }

    private static Stream<Arguments> provideFindeById() {
        return Stream.of(Arguments.of(10003L, new Group(10003L, "AA-33"), true),
            Arguments.of(33333L, new Group(9999L, "forTest"), false));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testDeleteById_shouldDeleteRow() {
        groupDao.deleteById(10002L);
        assertTrue(groupDao.findById(10002L).isEmpty());
    }

    @ParameterizedTest
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    @CsvSource(value = {"AA-22 , true, 10002",
        "BB-22, false, 99999"})
    void testFindByName(String groupName, boolean intermedatResult, Long expected) {
        Optional<Group> actual = groupDao.findByName(groupName);

        assertEquals(intermedatResult, actual.isPresent());
        assertEquals(expected, actual.orElse(new Group(99999L, "forTest")).getId());
    }

    @ParameterizedTest
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    @MethodSource("provideSave")
    void testSave_shouldUpdate_whenRowExist(Group start, Group expected) {
        assertEquals(expected, groupDao.save(start));
    }

    private static Stream<Arguments> provideSave() {
        return Stream.of(Arguments.of(new Group(10002L, "BB-99"), new Group(10002L, "BB-99")),
            Arguments.of(new Group("CC-22"), new Group(1L, "CC-22")));
    }


    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindFreeByTime() {
        List<Group> expected = Arrays.asList(new Group(10003L, "AA-33"));

        assertEquals(expected, groupDao.findFreeByTime(LocalDate.of(01, 01, 0001), 2));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFindFreeByTimeByTeacherId() {
        List<Group> expected = Arrays.asList(new Group(10003L, "AA-33"));
        assertEquals(expected, groupDao.findFreeByTimeByTeacherId(10003L, LocalDate.of(01, 01, 0001), 2));
    }

    @Test
    @Sql(scripts = {"/db/clean_db.sql", "/db/TestData.sql"})
    void testFreeFreeByTeacherByCourseByTime() {
        List<Group> expected = Arrays.asList(new Group(10003L, "AA-33"));
        assertEquals(expected, groupDao.findFreeFreeByTeacherByCourseByTime(10002L, 10001L, LocalDate.of(01, 01, 0001), 2));
    }

}

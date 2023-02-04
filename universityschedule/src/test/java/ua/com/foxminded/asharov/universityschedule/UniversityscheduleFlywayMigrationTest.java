package ua.com.foxminded.asharov.universityschedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UniversityscheduleFlywayMigrationTest {

    @Autowired
    JdbcTemplate template;

    @Test
    void migrationDone() throws SQLException {
        List<String> expected = Arrays.asList("students", "groups", "courses", "courses_groups", "rooms", "teachers", "flyway_schema_history", "lectures", "teachers_courses");
        String sqlQuery = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE';";

        List<String> actual = template.queryForList(sqlQuery, String.class);

        Collections.sort(expected);
        Collections.sort(actual);

        Assertions.assertEquals(actual, expected);
    }
}

package ua.com.foxminded.asharov.universityschedule.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public interface DaoUtil<T> extends RowMapper<T>{

    T mapRow(ResultSet rs, int rowNum) throws SQLException;

    Map<String, Object> matchParam(T entity);

}

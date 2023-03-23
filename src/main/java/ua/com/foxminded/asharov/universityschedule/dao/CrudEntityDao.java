package ua.com.foxminded.asharov.universityschedule.dao;

import ua.com.foxminded.asharov.universityschedule.model.Entity;

import java.util.List;
import java.util.Optional;

public interface CrudEntityDao<T extends Entity<K>, K> {

    Optional<T> findById(K id);

    List<T> findAll();

    void deleteById(K id);

    T save(T entity);

}

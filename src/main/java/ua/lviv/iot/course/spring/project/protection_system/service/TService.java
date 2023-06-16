package ua.lviv.iot.course.spring.project.protection_system.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface TService<T> {
    T getById(Integer id);

    Collection<T> getAll();

    T create(T entity) throws IOException;

    T update(Integer id, T entity) throws IOException;

    void delete(Integer id) throws IOException;

    Map<Integer, T> getMap();
}

package ua.lviv.iot.course.spring.project.protection_system.controler;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collection;

public interface TController<T> {
    ResponseEntity<T> getById(Integer id);

    ResponseEntity<Collection<T>> getAll();

    ResponseEntity<T> create(T entity) throws IOException;

    ResponseEntity<T> update(Integer id, T entity) throws IOException;

    ResponseEntity<T> delete(Integer id) throws IOException;
}


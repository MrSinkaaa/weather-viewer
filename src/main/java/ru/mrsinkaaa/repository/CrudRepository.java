package ru.mrsinkaaa.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<K, T>{

    Optional<T> findById(K id);

    List<T> findAll();

    void update(T entity);

    T save(T entity);

    boolean delete(T entity);
}

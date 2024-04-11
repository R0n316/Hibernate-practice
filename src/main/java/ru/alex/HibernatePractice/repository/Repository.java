package ru.alex.HibernatePractice.repository;

import ru.alex.HibernatePractice.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<E extends BaseEntity<K>,K extends Serializable> {
    Optional<E> get(K id);

    List<E> getAll();

    void save(E entity);

    void update(E entity);

    void delete(E entity);
}
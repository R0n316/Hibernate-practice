package ru.alex.HibernatePractice.repository;

import ru.alex.HibernatePractice.entity.BaseEntity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Repository<E extends BaseEntity<K>,K extends Serializable> {

    default Optional<E> get(K id){
        return get(id, Collections.emptyMap());
    }

    Optional<E> get(K id, Map<String,Object> properties);

    default List<E> getAll(){
        return getAll(Collections.emptyMap());
    }

    List<E> getAll(Map<String,Object> properties);

    void save(E entity);

    void update(E entity);

    void delete(E entity);
}
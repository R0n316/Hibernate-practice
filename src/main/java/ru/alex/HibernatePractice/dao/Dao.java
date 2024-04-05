package ru.alex.HibernatePractice.dao;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public interface Dao<T,K> {
    Optional<T> get(Session session,K id);

    List<T> getAll(Session session);

    void save(Session session,T t);

    void update(Session session,T t);

    void delete(Session session,T t);
}
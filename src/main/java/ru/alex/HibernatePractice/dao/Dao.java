package ru.alex.HibernatePractice.dao;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public interface Dao<T,K> {
    Optional<T> get(Session session,K id);

    List<T> getAll(Session session);

    void save(Session session,T id);

    void update(Session session,T id);

    void delete(Session session,T id);
}
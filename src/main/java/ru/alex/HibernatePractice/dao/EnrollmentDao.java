package ru.alex.HibernatePractice.dao;

import org.hibernate.Session;
import ru.alex.HibernatePractice.entity.Enrollment;

import java.util.List;
import java.util.Optional;

public class EnrollmentDao implements Dao<Enrollment,Integer> {


    @Override
    public Optional<Enrollment> get(Session session, Integer id) {
        return Optional.ofNullable(session.get(Enrollment.class, id));
    }

    @Override
    public List<Enrollment> getAll(Session session) {
        return session.createQuery("SELECT e FROM Enrollment e",Enrollment.class).list();
    }

    @Override
    public void save(Session session, Enrollment enrollment) {
        session.persist(enrollment);
    }

    @Override
    public void update(Session session, Enrollment enrollment) {
        session.persist(enrollment);
    }

    @Override
    public void delete(Session session, Enrollment enrollment) {
        session.remove(enrollment);
    }
}

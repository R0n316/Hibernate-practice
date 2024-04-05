package ru.alex.HibernatePractice.dao;

import org.hibernate.Session;
import ru.alex.HibernatePractice.entity.Course;

import java.util.List;
import java.util.Optional;

public class CourseDao implements Dao<Course,Integer>{

    @Override
    public Optional<Course> get(Session session, Integer id) {
        return Optional.ofNullable(session.get(Course.class,id));
    }

    @Override
    public List<Course> getAll(Session session) {
        return session.createQuery("SELECT c FROM Course c",Course.class).list();
    }

    @Override
    public void save(Session session, Course course) {
        session.persist(course);
    }

    @Override
    public void update(Session session, Course course) {
        session.persist(course);
    }

    @Override
    public void delete(Session session, Course course) {
        session.remove(course);
    }

    public List<Course> findCoursesByStudent(Session session, Integer studentId){
        return session.createQuery(
                "SELECT DISTINCT c FROM Course c JOIN c.enrollments e WHERE e.student.id=:studentId",Course.class)
                .setParameter("studentId",studentId)
                .list();
    }
}

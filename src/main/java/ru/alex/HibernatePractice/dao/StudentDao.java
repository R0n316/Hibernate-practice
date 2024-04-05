package ru.alex.HibernatePractice.dao;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.entity.Student;

import java.util.List;
import java.util.Optional;

public class StudentDao implements Dao<Student,Integer>{

    @Transactional
    @Override
    public Optional<Student> get(Session session,Integer id) {
         return Optional.ofNullable(session.get(Student.class,id));
    }

    @Override
    public List<Student> getAll(Session session) {
        return session.createQuery("SELECT s FROM Student s",Student.class).list();
    }

    @Override
    public void save(Session session,Student student) {
        session.persist(student);
    }

    @Override
    public void update(Session session,Student student) {
        session.persist(student);
    }

    @Override
    public void delete(Session session, Student student) {
        session.remove(student);
    }

    public List<Student> finStudentsByCourse(Session session,Integer courseId){
        return session.createQuery(
                        "SELECT DISTINCT s FROM Student s JOIN FETCH s.enrollments e WHERE e.course.id = :courseId", Student.class)
                .setParameter("courseId", courseId)
                .list();
    }
}

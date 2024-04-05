package ru.alex.HibernatePractice.dao;

import org.hibernate.Session;
import ru.alex.HibernatePractice.entity.Enrollment;
import ru.alex.HibernatePractice.entity.Student;

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

    public void giveStudentGradeForCourse(Session session,Integer studentId, Integer courseId, Float grade){
        Enrollment enrollment = session.createQuery(
                "SELECT e FROM Enrollment e WHERE e.student.id=:studentId AND e.course.id=:courseId",Enrollment.class)
                .setParameter("studentId",studentId)
                .setParameter("courseId",courseId)
                .getSingleResult();

        enrollment.setCourseGrade(grade);
        session.persist(enrollment);
    }
}

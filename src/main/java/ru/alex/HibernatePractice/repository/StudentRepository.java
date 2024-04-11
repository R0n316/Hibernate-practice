package ru.alex.HibernatePractice.repository;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import ru.alex.HibernatePractice.entity.Student;

import java.util.List;

public class StudentRepository extends BaseRepository<Student,Integer> {


    public StudentRepository(EntityManager entityManager) {
        super(entityManager, Student.class);
    }

    public List<Student> findStudentsByCourse(Session session, Integer courseId){
        return session.createQuery(
                        "SELECT DISTINCT s FROM Student s JOIN FETCH s.enrollments e WHERE e.course.id = :courseId", Student.class)
                .setParameter("courseId", courseId)
                .list();
    }
}

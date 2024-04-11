package ru.alex.HibernatePractice.repository;

import jakarta.persistence.EntityManager;
import ru.alex.HibernatePractice.entity.Enrollment;

import java.util.List;

public class EnrollmentRepository extends BaseRepository<Enrollment,Integer> {


    public EnrollmentRepository(EntityManager entityManager) {
        super(entityManager, Enrollment.class);
    }

    public void giveStudentGradeForCourse(Integer studentId, Integer courseId, Float grade){
        Enrollment enrollment = entityManager.createQuery(
                        "SELECT e FROM Enrollment e WHERE e.student.id=:studentId AND e.course.id=:courseId",Enrollment.class)
                .setParameter("studentId",studentId)
                .setParameter("courseId",courseId)
                .getSingleResult();

        enrollment.setCourseGrade(grade);
        entityManager.persist(enrollment);
    }

    public List<Enrollment> findEnrollmentsByCourse(Integer courseId){
        return entityManager
                .createQuery("SELECT e FROM Enrollment e WHERE e.course.id = :courseId",Enrollment.class)
                .setParameter("courseId",courseId)
                .getResultList();
    }

    public List<Enrollment> findEnrollmentsByStudent(Integer studentId){
        return entityManager
                .createQuery("SELECT e FROM Enrollment e WHERE e.student.id = :studentId",Enrollment.class)
                .setParameter("studentId",studentId)
                .getResultList();
    }
}

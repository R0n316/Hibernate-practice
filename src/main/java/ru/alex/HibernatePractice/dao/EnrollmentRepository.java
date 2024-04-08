package ru.alex.HibernatePractice.dao;

import jakarta.persistence.EntityManager;
import ru.alex.HibernatePractice.entity.Enrollment;

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
}

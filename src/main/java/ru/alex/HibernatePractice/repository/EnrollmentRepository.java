package ru.alex.HibernatePractice.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;

import ru.alex.HibernatePractice.dto.filter.EnrollmentFilter;
import ru.alex.HibernatePractice.entity.Enrollment;

import java.util.List;

import static ru.alex.HibernatePractice.entity.QEnrollment.*;

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


    public List<Enrollment> findEnrollmentsByFilter(EnrollmentFilter filter){
        Predicate predicate = QPredicate.builder()
                .add(filter.getCourse(), enrollment.course::eq)
                .add(filter.getStudent(),enrollment.student::eq)
                .buildAndPredicate();
        return new JPAQuery<>(entityManager)
                .select(enrollment)
                .from(enrollment)
                .where(predicate)
                .fetch();
    }
}

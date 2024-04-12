package ru.alex.HibernatePractice.repository;

import jakarta.persistence.EntityManager;
import ru.alex.HibernatePractice.entity.Course;

import java.util.List;

public class CourseRepository extends BaseRepository<Course,Integer> {


    public CourseRepository(EntityManager entityManager) {
        super(entityManager, Course.class);
    }

    public List<Course> findCoursesByStudent(Integer studentId){
        return entityManager.createQuery(
                        "SELECT DISTINCT c FROM Course c JOIN c.enrollments e WHERE e.student.id=:studentId",Course.class)
                .setParameter("studentId",studentId)
                .getResultList();
    }

    @Override
    public void delete(Course entity) {
        entityManager.createQuery("DELETE FROM Enrollment e WHERE e.course = :course")
                .setParameter("course",entity)
                .executeUpdate();
        super.delete(entity);
    }
}

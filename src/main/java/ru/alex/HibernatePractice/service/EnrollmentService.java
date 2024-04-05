package ru.alex.HibernatePractice.service;

import org.hibernate.Session;
import ru.alex.HibernatePractice.dao.EnrollmentDao;
import ru.alex.HibernatePractice.entity.Enrollment;
import ru.alex.HibernatePractice.entity.Student;

import java.util.List;
import java.util.Optional;

public class EnrollmentService {

    private final EnrollmentDao enrollmentDao = new EnrollmentDao();

    public Optional<Enrollment> get(Session session,Integer id){
        return enrollmentDao.get(session,id);
    }

    public List<Enrollment> getAll(Session session){
        return enrollmentDao.getAll(session);
    }

    public void save(Session session,Enrollment enrollment){
        enrollmentDao.save(session, enrollment);
    }

    public void update(Session session,Enrollment enrollment){
        enrollmentDao.update(session, enrollment);
    }

    public void delete(Session session, Enrollment enrollment){
        enrollmentDao.delete(session, enrollment);
    }
}

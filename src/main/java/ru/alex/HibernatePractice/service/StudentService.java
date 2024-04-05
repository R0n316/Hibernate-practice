package ru.alex.HibernatePractice.service;

import org.hibernate.Session;
import ru.alex.HibernatePractice.dao.StudentDao;
import ru.alex.HibernatePractice.entity.Student;

import java.util.List;
import java.util.Optional;

public class StudentService {

    private final StudentDao studentDao = new StudentDao();

    public Optional<Student> get(Session session, Integer id) {
        return studentDao.get(session,id);
    }

    public List<Student> getAll(Session session) {
        return studentDao.getAll(session);
    }

    public void save(Session session,Student student){
        studentDao.save(session,student);
    }

    public void update(Session session, Student student){
        studentDao.update(session,student);
    }

    public void delete(Session session,Student student){
        studentDao.delete(session,student);
    }

    public List<Student> findStudentsByCourse(Session session, Integer courseId){
        return studentDao.finStudentsByCourse(session,courseId);
    }
}

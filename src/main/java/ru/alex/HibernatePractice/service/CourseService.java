package ru.alex.HibernatePractice.service;

import org.hibernate.Session;
import ru.alex.HibernatePractice.dao.CourseDao;
import ru.alex.HibernatePractice.entity.Course;

import java.util.List;
import java.util.Optional;

public class CourseService {

    private final CourseDao courseDao = new CourseDao();

    public Optional<Course> get(Session session,Integer id){
        return courseDao.get(session,id);
    }

    public List<Course> getAll(Session session){
        return courseDao.getAll(session);
    }

    public void save(Session session,Course course){
        courseDao.save(session, course);
    }

    public void update(Session session,Course course){
        courseDao.update(session, course);
    }

    public void delete(Session session,Course course){
        session.remove(course);
    }
}

package ru.alex.HibernatePractice.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.entity.Student;
import ru.alex.HibernatePractice.util.TestDataImporter;
import ru.alex.HibernatePractice.util.HibernateTestUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CourseDaoTest {

    private SessionFactory sessionFactory;

    private final CourseDao courseDao = new CourseDao();

    private static final Course MATH = Course.builder()
            .id(1)
            .name("Math")
            .duration(60)
            .build();

    private static final Course PHYSICS = Course.builder()
            .id(2)
            .name("physics")
            .duration(70)
            .build();

    private static final Course HISTORY = Course.builder()
            .id(3)
            .name("history")
            .duration(40)
            .build();

    Student PETR = Student.builder()
            .id(2)
            .name("Petr")
            .surname("Petrov")
            .email("pertov@gmail.com")
            .birthDate(LocalDate.of(1995,3,4))
            .grade(4.85F)
            .build();


    @BeforeEach
    public void setUp(){
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.initData(sessionFactory);
    }

    @Test
    void get() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Optional<Course> course = courseDao.get(session,MATH.getId());
            assertThat(course).isPresent();
            assertThat(course.get()).isEqualTo(MATH);
            session.getTransaction().commit();
        }
    }

    @Test
    void getAll() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Course> courses = courseDao.getAll(session);
            assertThat(courses).contains(MATH,HISTORY,PHYSICS);
            session.getTransaction().commit();
        }
    }

    @Test
    void save() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Course newCourse = Course.builder()
                    .name("some name")
                    .duration(100)
                    .build();
            courseDao.save(session,newCourse);
            newCourse.setId(4);
            Optional<Course> course = courseDao.get(session,newCourse.getId());
            assertThat(course).isPresent();
            assertThat(course.get()).isEqualTo(newCourse);
            session.getTransaction().commit();
        }
    }

    @Test
    void update() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Optional<Course> maybeCourse = courseDao.get(session,MATH.getId());
            assertThat(maybeCourse).isPresent();
            Course course = maybeCourse.get();
            course.setDuration(course.getDuration() + 10);
            courseDao.update(session,course);
            session.flush();
            Course updatedCourse = courseDao.get(session,course.getId()).get();
            assertThat(updatedCourse.getDuration()).isNotEqualTo(MATH.getDuration());

            session.getTransaction().commit();
        }
    }

    @Test
    void delete() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Optional<Course> course = courseDao.get(session,MATH.getId());
            assertThat(course).isPresent();
            courseDao.delete(session,course.get());

            Optional<Course> maybeCourse = courseDao.get(session,MATH.getId());
            assertThat(maybeCourse).isEmpty();

            session.getTransaction().commit();
        }
    }

    @Test
    void findCoursesByStudent(){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            List<String> expectedCourseNames = List.of("history");


            List<Course> courses = courseDao.findCoursesByStudent(session,PETR.getId());

            List<String> actualCourseNames = courses.stream().map(Course::getName).toList();

            assertThat(actualCourseNames).isEqualTo(expectedCourseNames);

            session.getTransaction().commit();
        }
    }
}
package ru.alex.HibernatePractice.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.entity.Student;
import ru.alex.HibernatePractice.util.TestDataImporter;
import ru.alex.HibernatePractice.util.HibernateTestUtil;

import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CourseRepositoryTest {

    private SessionFactory sessionFactory;

    private CourseRepository courseRepository;

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
        try(Session session = buildSession()){
            courseRepository = new CourseRepository(session);
            session.beginTransaction();
            Optional<Course> course = courseRepository.get(MATH.getId());
            assertThat(course).isPresent();
            assertThat(course.get()).isEqualTo(MATH);
            session.getTransaction().commit();
        }
    }

    @Test
    void getAll() {
        try(Session session = buildSession()){
            courseRepository = new CourseRepository(session);
            session.beginTransaction();
            List<Course> courses = courseRepository.getAll();
            assertThat(courses).contains(MATH,HISTORY,PHYSICS);
            session.getTransaction().commit();
        }
    }

    @Test
    void save() {
        try(Session session = buildSession()){
            courseRepository = new CourseRepository(session);
            session.beginTransaction();
            Course newCourse = Course.builder()
                    .name("some name")
                    .duration(100)
                    .build();
            courseRepository.save(newCourse);
            newCourse.setId(4);
            Optional<Course> course = courseRepository.get(newCourse.getId());
            assertThat(course).isPresent();
            assertThat(course.get()).isEqualTo(newCourse);
            session.getTransaction().commit();
        }
    }

    @Test
    void update() {
        try(Session session = buildSession()){
            courseRepository = new CourseRepository(session);
            session.beginTransaction();
            Optional<Course> maybeCourse = courseRepository.get(MATH.getId());
            assertThat(maybeCourse).isPresent();
            Course course = maybeCourse.get();
            course.setDuration(course.getDuration() + 10);
            courseRepository.update(course);
            session.flush();
            Course updatedCourse = courseRepository.get(course.getId()).get();
            assertThat(updatedCourse.getDuration()).isNotEqualTo(MATH.getDuration());

            session.getTransaction().commit();
        }
    }

    @Test
    void delete() {
        try(Session session = buildSession()){
            courseRepository = new CourseRepository(session);
            session.beginTransaction();

            Optional<Course> course = courseRepository.get(MATH.getId());
            assertThat(course).isPresent();
            courseRepository.delete(course.get());

            Optional<Course> maybeCourse = courseRepository.get(MATH.getId());
            assertThat(maybeCourse).isEmpty();

            session.getTransaction().commit();
        }
    }

    @Test
    void findCoursesByStudent(){
        try(Session session = sessionFactory.openSession()){
            courseRepository = new CourseRepository(session);
            session.beginTransaction();

            List<String> expectedCourseNames = List.of("history");


            List<Course> courses = courseRepository.findCoursesByStudent(PETR.getId());

            List<String> actualCourseNames = courses.stream().map(Course::getName).toList();

            assertThat(actualCourseNames).isEqualTo(expectedCourseNames);

            session.getTransaction().commit();
        }
    }

    private Session buildSession(){
        return (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),new Class[]{Session.class},
                ((proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(),args)));
    }
}
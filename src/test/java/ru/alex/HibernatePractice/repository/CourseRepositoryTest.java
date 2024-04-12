package ru.alex.HibernatePractice.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
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

class CourseRepositoryTest {

    private SessionFactory sessionFactory;

    private CourseRepository courseRepository;

    private Session session;


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
        session = sessionFactory.openSession();
        session.beginTransaction();
        courseRepository = new CourseRepository(session);
    }

    @Test
    void get() {
        Optional<Course> course = courseRepository.get(MATH.getId());
        assertThat(course).isPresent();
        assertThat(course.get()).isEqualTo(MATH);
    }

    @Test
    void getAll() {;
        List<Course> courses = courseRepository.getAll();
        assertThat(courses).contains(MATH,HISTORY,PHYSICS);
    }

    @Test
    void save() {
        Course newCourse = Course.builder()
                .name("some name")
                .duration(100)
                .build();
        courseRepository.save(newCourse);
        newCourse.setId(4);
        Optional<Course> course = courseRepository.get(newCourse.getId());
        assertThat(course).isPresent();
        assertThat(course.get()).isEqualTo(newCourse);
    }

    @Test
    void update() {
        Optional<Course> maybeCourse = courseRepository.get(MATH.getId());
        assertThat(maybeCourse).isPresent();
        Course course = maybeCourse.get();
        course.setDuration(course.getDuration() + 10);
        courseRepository.update(course);
        session.flush();
        Course updatedCourse = courseRepository.get(course.getId()).get();
        assertThat(updatedCourse.getDuration()).isNotEqualTo(MATH.getDuration());
    }

    @Test
    void delete() {
        Optional<Course> course = courseRepository.get(MATH.getId());
        assertThat(course).isPresent();
        courseRepository.delete(course.get());

        Optional<Course> maybeCourse = courseRepository.get(MATH.getId());
        assertThat(maybeCourse).isEmpty();
    }

    @Test
    void findCoursesByStudent(){
        List<String> expectedCourseNames = List.of("history");

        List<Course> courses = courseRepository.findCoursesByStudent(PETR.getId());

        List<String> actualCourseNames = courses.stream().map(Course::getName).toList();

        assertThat(actualCourseNames).isEqualTo(expectedCourseNames);
    }

    @AfterEach
    void closeResources(){
        session.getTransaction().commit();
        sessionFactory.close();
        session.close();
    }
}
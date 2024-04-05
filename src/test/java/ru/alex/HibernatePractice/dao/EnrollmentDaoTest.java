package ru.alex.HibernatePractice.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.entity.Enrollment;
import ru.alex.HibernatePractice.entity.Student;
import ru.alex.HibernatePractice.util.TestDataImporter;
import ru.alex.HibernatePractice.util.HibernateTestUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class EnrollmentDaoTest {

    private SessionFactory sessionFactory;

    private final EnrollmentDao enrollmentDao = new EnrollmentDao();

    private static final Student IVAN = Student.builder()
            .id(1)
            .name("Ivan")
            .surname("Ivanov")
            .email("ivan@gmail.com")
            .birthDate(LocalDate.of(2000,1,1))
            .grade(4.7F)
            .build();

    private static final Course MATH = Course.builder()
            .id(1)
            .name("Math")
            .duration(60)
            .build();

    private static final Enrollment ENROLLMENT = Enrollment.builder()
            .id(1)
            .student(IVAN)
            .course(MATH)
            .build();

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.initData(sessionFactory);
    }

    @Test
    void get() {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Enrollment> enrollment = enrollmentDao.get(session, ENROLLMENT.getId());
            assertThat(enrollment).isPresent();
            assertThat(enrollment.get()).isEqualTo(ENROLLMENT);
            session.getTransaction().commit();
        }
    }


    @Test
    void getAll() {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Enrollment> enrollments = enrollmentDao.getAll(session);
            assertThat(enrollments).hasSize(6);
            session.getTransaction().commit();
        }
    }

    @Test
    void save() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Enrollment enrollment = Enrollment.builder()
                    .student(IVAN)
                    .course(MATH)
                    .build();
            enrollmentDao.save(session,enrollment);

            session.flush();

            enrollment.setId(7);
            Optional<Enrollment> maybeEnrollment = enrollmentDao.get(session,enrollment.getId());
            assertThat(maybeEnrollment).isPresent();
            assertThat(maybeEnrollment.get()).isEqualTo(enrollment);
            session.getTransaction().commit();
        }
    }

    @Test
    void testDefaultDate(){
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Enrollment enrollment = Enrollment.builder()
                    .student(IVAN)
                    .course(MATH)
                    .build();
            enrollmentDao.save(session,enrollment);

            session.flush();

            Optional<Enrollment> enrollment1 = enrollmentDao.get(session, 7);
            assertThat(enrollment1).isPresent();
            System.out.println(enrollment1.get());
            assertThat(enrollment1.get().getEnrollmentDate()).isNotNull();
            System.out.println(enrollment1.get().getEnrollmentDate());
            session.getTransaction().commit();
        }
    }

    @Test
    void update() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Student alex = Student.builder()
                    .name("alex")
                    .surname("rav")
                    .email("alex@gmail.com")
                    .grade(4.83f)
                    .build();

            Optional<Enrollment> enrollment = enrollmentDao.get(session, ENROLLMENT.getId());

            assertThat(enrollment).isPresent();
            enrollment.get().setStudent(alex);
            session.persist(alex);
            enrollmentDao.update(session,enrollment.get());
            session.flush();

            Optional<Enrollment> updatedEnrollment = enrollmentDao.get(session,1);

            assertThat(updatedEnrollment.get().getStudent()).isEqualTo(alex);

            session.getTransaction().commit();
        }
    }

    @Test
    void delete() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Optional<Enrollment> enrollment = enrollmentDao.get(session, ENROLLMENT.getId());
            assertThat(enrollment).isPresent();
            enrollmentDao.delete(session,enrollment.get());
            session.flush();
            enrollment = enrollmentDao.get(session, ENROLLMENT.getId());
            assertThat(enrollment).isEmpty();

            session. getTransaction().commit();
        }
    }

    @Test
    void giveStudentGradeForCourse(){
        try(Session session = sessionFactory.openSession()){

        }
    }
}
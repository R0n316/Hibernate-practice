package ru.alex.HibernatePractice.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
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
class EnrollmentRepositoryTest {

    private SessionFactory sessionFactory;

    private EnrollmentRepository enrollmentRepository;

    private Session session;

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
        session = sessionFactory.openSession();
        enrollmentRepository = new EnrollmentRepository(session);
        session.beginTransaction();
    }

    @Test
    void get() {
            Optional<Enrollment> enrollment = enrollmentRepository.get(ENROLLMENT.getId());
            assertThat(enrollment).isPresent();
            assertThat(enrollment.get()).isEqualTo(ENROLLMENT);
    }


    @Test
    void getAll() {
            List<Enrollment> enrollments = enrollmentRepository.getAll();
            assertThat(enrollments).hasSize(6);
    }

    @Test
    void save() {
            Enrollment enrollment = Enrollment.builder()
                    .student(IVAN)
                    .course(MATH)
                    .build();
            enrollmentRepository.save(enrollment);

            session.flush();

            enrollment.setId(7);
            Optional<Enrollment> maybeEnrollment = enrollmentRepository.get(enrollment.getId());
            assertThat(maybeEnrollment).isPresent();
            assertThat(maybeEnrollment.get()).isEqualTo(enrollment);
    }

    @Test
    void testDefaultDate(){
            Enrollment enrollment = Enrollment.builder()
                    .student(IVAN)
                    .course(MATH)
                    .build();
            enrollmentRepository.save(enrollment);

            session.flush();

            Optional<Enrollment> enrollment1 = enrollmentRepository.get(7);
            assertThat(enrollment1).isPresent();
            System.out.println(enrollment1.get());
            assertThat(enrollment1.get().getEnrollmentDate()).isNotNull();
            System.out.println(enrollment1.get().getEnrollmentDate());
    }

    @Test
    void update() {
            Student alex = Student.builder()
                    .name("alex")
                    .surname("rav")
                    .email("alex@gmail.com")
                    .grade(4.83f)
                    .build();

            Optional<Enrollment> enrollment = enrollmentRepository.get(ENROLLMENT.getId());

            assertThat(enrollment).isPresent();
            enrollment.get().setStudent(alex);
            session.persist(alex);
            enrollmentRepository.update(enrollment.get());
            session.flush();

            Optional<Enrollment> updatedEnrollment = enrollmentRepository.get(1);

            assertThat(updatedEnrollment.get().getStudent()).isEqualTo(alex);
    }

    @Test
    void delete() {
            Optional<Enrollment> enrollment = enrollmentRepository.get(ENROLLMENT.getId());
            assertThat(enrollment).isPresent();
            enrollmentRepository.delete(enrollment.get());
            session.flush();
            enrollment = enrollmentRepository.get(ENROLLMENT.getId());
            assertThat(enrollment).isEmpty();
    }

    @Test
    void giveStudentGradeForCourse(){

    }

    @AfterEach
    void closeResources(){
        session.getTransaction().commit();
        sessionFactory.close();
        session.close();
    }
}
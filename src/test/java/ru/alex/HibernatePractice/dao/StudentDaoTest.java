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

class StudentDaoTest {

    private SessionFactory sessionFactory;

    private final StudentDao studentDao = new StudentDao();

    private static final Student IVAN = Student.builder()
            .id(1)
            .name("Ivan")
            .surname("Ivanov")
            .email("ivan@gmail.com")
            .birthDate(LocalDate.of(2000,1,1))
            .grade(4.7F)
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
            Optional<Student> maybeIvan = studentDao.get(session,IVAN.getId());
            assertThat(maybeIvan).isPresent();
            assertThat(maybeIvan.get()).isEqualTo(IVAN);
            session.getTransaction().commit();
        }
    }

    @Test
    void getAll() {
        List<String> expectedEmails = List.of(
                "ivan@gmail.com",
                "pertov@gmail.com",
                "jones@gmail.com"
        );

        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Student> students = studentDao.getAll(session);
            List<String> actualEmails = students.stream().map(Student::getEmail).toList();
            assertThat(actualEmails).isEqualTo(expectedEmails);
        }
    }

    @Test
    void save() {
        Student exampleStudent = Student.builder()
                .name("example")
                .email("example@mail.com")
                .surname("some surname")
                .grade(5F)
                .build();
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            studentDao.save(session,exampleStudent);
            session.flush();
            Optional<Student> maybeStudent = studentDao.get(session,4);
            assertThat(maybeStudent).isPresent();
            assertThat(maybeStudent.get()).isEqualTo(exampleStudent);
        }
    }

    @Test
    void update() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Optional<Student> maybeStudent = studentDao.get(session,IVAN.getId());
            assertThat(maybeStudent).isPresent();
            Student student = maybeStudent.get();
            student.setEmail("newEmail@new.email");
            studentDao.update(session,student);
            session.flush();

            Student updatedStudent = studentDao.get(session,IVAN.getId()).get();

            assertThat(updatedStudent).isNotEqualTo(IVAN);

            session.getTransaction().commit();
        }
    }

    @Test
    void delete() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Optional<Student> student = studentDao.get(session,IVAN.getId());
            assertThat(student).isPresent();

            studentDao.delete(session,student.get());

            Optional<Student> maybeStudent = studentDao.get(session,IVAN.getId());

            assertThat(maybeStudent).isEmpty();

            session.getTransaction().commit();
        }
    }

    @Test
    void findStudentsByCourse(){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Course math = Course.builder()
                    .id(1)
                    .name("Math")
                    .duration(60)
                    .build();

            List<String> expectedEmails = List.of("ivan@gmail.com","jones@gmail.com");

            List<Student> students = studentDao.finStudentsByCourse(session,math.getId());

            List<String> actualEmails = students.stream().map(Student::getEmail).toList();

            assertThat(actualEmails).isEqualTo(expectedEmails);

            session.getTransaction().commit();

        }
    }
}
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

class StudentRepositoryTest {

    private SessionFactory sessionFactory;

    private StudentRepository studentRepository;

    private Session session;

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
        session = sessionFactory.openSession();
        session.beginTransaction();
        studentRepository = new StudentRepository(session);
    }

    @Test
    void get() {
        Optional<Student> maybeIvan = studentRepository.get(IVAN.getId());
        assertThat(maybeIvan).isPresent();
        assertThat(maybeIvan.get()).isEqualTo(IVAN);
    }

    @Test
    void getAll() {
        List<String> expectedEmails = List.of(
                "ivan@gmail.com",
                "pertov@gmail.com",
                "jones@gmail.com"
        );

        List<Student> students = studentRepository.getAll();
        List<String> actualEmails = students.stream().map(Student::getEmail).toList();
        assertThat(actualEmails).isEqualTo(expectedEmails);
    }

    @Test
    void save() {
        Student exampleStudent = Student.builder()
                .name("example")
                .email("example@mail.com")
                .surname("some surname")
                .grade(5F)
                .build();
        studentRepository.save(exampleStudent);
        session.flush();
        Optional<Student> maybeStudent = studentRepository.get(4);
        assertThat(maybeStudent).isPresent();
        assertThat(maybeStudent.get()).isEqualTo(exampleStudent);
    }

    @Test
    void update() {
        Optional<Student> maybeStudent = studentRepository.get(IVAN.getId());
        assertThat(maybeStudent).isPresent();
        Student student = maybeStudent.get();
        student.setEmail("newEmail@new.email");
        studentRepository.update(student);
        session.flush();

        Student updatedStudent = studentRepository.get(IVAN.getId()).get();

        assertThat(updatedStudent).isNotEqualTo(IVAN);
    }

    @Test
    void delete() {
        Optional<Student> student = studentRepository.get(IVAN.getId());
        assertThat(student).isPresent();

        studentRepository.delete(student.get());

        Optional<Student> maybeStudent = studentRepository.get(IVAN.getId());

        assertThat(maybeStudent).isEmpty();
    }

    @Test
    void findStudentsByCourse(){
        Course math = Course.builder()
                .id(1)
                .name("Math")
                .duration(60)
                .build();

        List<String> expectedEmails = List.of("ivan@gmail.com","jones@gmail.com");

        List<Student> students = studentRepository.findStudentsByCourse(session,math.getId());

        List<String> actualEmails = students.stream().map(Student::getEmail).toList();

        assertThat(actualEmails).isEqualTo(expectedEmails);
    }

    @AfterEach
    void closeResources(){
        session.getTransaction().commit();
        sessionFactory.close();
        session.close();
    }
}
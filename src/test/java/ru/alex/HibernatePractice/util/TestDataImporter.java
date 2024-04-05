package ru.alex.HibernatePractice.util;

import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.entity.Enrollment;
import ru.alex.HibernatePractice.entity.Student;

import java.time.LocalDate;

@UtilityClass
public class TestDataImporter {
    public void initData(SessionFactory sessionFactory){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Student student1 = Student.builder()
                    .name("Ivan")
                    .surname("Ivanov")
                    .email("ivan@gmail.com")
                    .birthDate(LocalDate.of(2000,1,1))
                    .grade(4.7F)
                    .build();
            Student student2 = Student.builder()
                    .name("Petr")
                    .surname("Petrov")
                    .email("pertov@gmail.com")
                    .birthDate(LocalDate.of(1995,3,4))
                    .grade(4.85F)
                    .build();
            Student student3 = Student.builder()
                    .name("John")
                    .surname("Jones")
                    .email("jones@gmail.com")
                    .birthDate(LocalDate.of(1998,5,7))
                    .grade(4.63f)
                    .build();

            Course math = Course.builder()
                    .name("Math")
                    .duration(60)
                    .build();

            Course history = Course.builder()
                    .name("history")
                    .duration(40)
                    .build();
            Course physics = Course.builder()
                    .name("physics")
                    .duration(70)
                    .build();

            Enrollment enrollment = Enrollment.builder()
                    .student(student1)
                    .course(math)
                    .build();
            Enrollment enrollment1 = Enrollment.builder()
                    .student(student1)
                    .course(history)
                    .build();
            Enrollment enrollment2 = Enrollment.builder()
                    .student(student1)
                    .course(physics)
                    .build();

            Enrollment enrollment3 = Enrollment.builder()
                    .student(student2)
                    .course(history)
                    .build();

            Enrollment enrollment4 = Enrollment.builder()
                    .student(student3)
                    .course(math)
                    .build();

            Enrollment enrollment5 = Enrollment.builder()
                    .student(student3)
                    .course(physics)
                    .build();

            session.persist(student1);
            session.persist(student2);
            session.persist(student3);

            session.persist(math);
            session.persist(physics);
            session.persist(history);


            session.persist(enrollment);
            session.persist(enrollment1);
            session.persist(enrollment2);
            session.persist(enrollment3);
            session.persist(enrollment4);
            session.persist(enrollment5);
            session.getTransaction().commit();
        }
    }
}

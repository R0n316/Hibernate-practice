package ru.alex.HibernatePractice.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.entity.Enrollment;
import ru.alex.HibernatePractice.entity.Student;

@UtilityClass
public class HibernateUtil {
    public static SessionFactory buildSessionFactory(){
        Configuration configuration = buildConfiguration();
        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration(){
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Course.class);
        configuration.addAnnotatedClass(Enrollment.class);
        configuration.addAnnotatedClass(Student.class);
        return configuration;
    }
}

package ru.alex.HibernatePractice.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.alex.HibernatePractice.dto.student.StudentCreateDto;
import ru.alex.HibernatePractice.dto.student.StudentReadDto;
import ru.alex.HibernatePractice.mapper.studentMapper.StudentCreateMapper;
import ru.alex.HibernatePractice.mapper.studentMapper.StudentMapper;
import ru.alex.HibernatePractice.mapper.studentMapper.StudentReadMapper;
import ru.alex.HibernatePractice.mapper.studentMapper.StudentUpdateMapper;
import ru.alex.HibernatePractice.repository.StudentRepository;
import ru.alex.HibernatePractice.util.HibernateTestUtil;
import ru.alex.HibernatePractice.util.TestDataImporter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private SessionFactory sessionFactory;
    private StudentService studentService;

    private final StudentReadDto IVAN = StudentReadDto.builder()
            .id(1)
            .name("Ivan")
            .surname("Ivanov")
            .email("ivan@gmail.com")
            .birthDate(LocalDate.of(2000,1,1))
            .grade(4.7F)
            .courses(List.of(1,2,3))
            .build();

    @BeforeEach
    void setUp() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.initData(sessionFactory);
    }

    @Test
    void findById() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            studentService = getStudentService(session);

            Optional<StudentReadDto> studentReadDto = studentService.findById(IVAN.getId());

            assertThat(studentReadDto).isPresent();
            assertThat(studentReadDto.get()).isEqualTo(IVAN);

            session.getTransaction().commit();
        }
    }

    @Test
    void findAll() {
        List<String> expectedResult = List.of(
                "ivan@gmail.com",
                "pertov@gmail.com",
                "jones@gmail.com"
        );
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            studentService = getStudentService(session);
            List<StudentReadDto> studentReadDtos = studentService.findAll();

            List<String> actualResult = studentReadDtos.stream().map(StudentReadDto::getEmail).toList();
            assertThat(actualResult).hasSize(3);
            assertThat(actualResult).isEqualTo(expectedResult);

            session.getTransaction().commit();
        }
    }

    @Test
    void create() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            studentService = getStudentService(session);
            StudentCreateDto studentCreateDto = StudentCreateDto.builder()
                    .name("Nikita")
                    .surname("Smirnov")
                    .email("smirnov@gmail.com")
                    .birthDate(LocalDate.of(2003,5,17))
                    .grade(4.22F)
                    .build();

            studentService.create(studentCreateDto);
            session.flush();

            Optional<StudentReadDto> studentReadDto = studentService.findById(4);
            assertThat(studentReadDto).isPresent();
            assertThat(studentReadDto.get().getEmail()).isEqualTo(studentCreateDto.getEmail());
            session.getTransaction().commit();
        }
    }

    @Test
    void update() {
        try(Session session = sessionFactory.openSession()){
            StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);
            studentService = getStudentService(session);
            session.beginTransaction();

            IVAN.setEmail("updatedEmail@gmail.com");
            studentService.update(IVAN.getId(),studentMapper.mapFrom(IVAN));

            session.flush();

            Optional<StudentReadDto> studentReadDto = studentService.findById(IVAN.getId());

            assertThat(studentReadDto).isPresent();
            assertThat(studentReadDto.get().getEmail()).isEqualTo(IVAN.getEmail());

            session.getTransaction().commit();
        }
    }

    @Test
    void delete() {
        try(Session session = sessionFactory.openSession()){
            studentService = getStudentService(session);
            session.beginTransaction();
            Optional<StudentReadDto> studentReadDto = studentService.findById(IVAN.getId());
            assertThat(studentReadDto).isPresent();

            studentService.delete(studentReadDto.get().getId());
            session.flush();

            Optional<StudentReadDto> studentReadDto1 = studentService.findById(IVAN.getId());
            assertThat(studentReadDto1).isEmpty();

            session.getTransaction().commit();
        }
    }

    private StudentService getStudentService(Session session){
        StudentRepository studentRepository = new StudentRepository(session);
        StudentReadMapper studentReadMapper = Mappers.getMapper(StudentReadMapper.class);
        StudentCreateMapper studentCreateMapper = Mappers.getMapper(StudentCreateMapper.class);
        StudentUpdateMapper studentUpdateMapper = Mappers.getMapper(StudentUpdateMapper.class);
        return new StudentService(studentRepository,studentReadMapper,studentCreateMapper,studentUpdateMapper);
    }
}
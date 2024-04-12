package ru.alex.HibernatePractice.service;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import ru.alex.HibernatePractice.dto.student.StudentCreateDto;
import ru.alex.HibernatePractice.dto.student.StudentReadDto;
import ru.alex.HibernatePractice.dto.student.StudentUpdateDto;
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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class StudentServiceTest {

    private SessionFactory sessionFactory;
    private StudentService studentService;
    private Session session;

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
        session = sessionFactory.openSession();
        session.beginTransaction();
        studentService = getStudentService(session);
    }

    @Test
    void findById() {
        Optional<StudentReadDto> studentReadDto = studentService.findById(IVAN.getId());
        assertThat(studentReadDto).isPresent();
        assertThat(studentReadDto.get()).isEqualTo(IVAN);
    }

    @Test
    void findAll() {
        List<String> expectedResult = List.of(
                "ivan@gmail.com",
                "pertov@gmail.com",
                "jones@gmail.com"
        );
        List<StudentReadDto> studentReadDtos = studentService.findAll();

        List<String> actualResult = studentReadDtos.stream().map(StudentReadDto::getEmail).toList();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void create() {
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
    }

    @Test
    void update() {
        StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

        IVAN.setEmail("updatedEmail@gmail.com");
        StudentUpdateDto studentUpdateDto = studentMapper.mapFrom(IVAN);
        studentService.update(IVAN.getId(),studentUpdateDto);

        session.flush();

        Optional<StudentReadDto> studentReadDto = studentService.findById(IVAN.getId());

        assertThat(studentReadDto).isPresent();
        assertThat(studentReadDto.get().getEmail()).isEqualTo(IVAN.getEmail());
    }

    @Test
    void delete() {
        Optional<StudentReadDto> studentReadDto = studentService.findById(IVAN.getId());
        assertThat(studentReadDto).isPresent();

        studentService.delete(studentReadDto.get().getId());
        session.flush();

        Optional<StudentReadDto> studentReadDto1 = studentService.findById(IVAN.getId());
        assertThat(studentReadDto1).isEmpty();
    }


    @ParameterizedTest
    @MethodSource("getArgumentsForValidationTest")
    void checkValidation(String name, String surname, String email, Float grade, String exceptionMessage){
        StudentCreateDto studentCreateDto = StudentCreateDto.builder()
                .name(name)
                .surname(surname)
                .email(email)
                .grade(grade)
                .build();

        var exception = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> studentService.create(studentCreateDto)
        );

        assertThat(exception.getMessage()).isEqualTo(exceptionMessage);
    }


    static Stream<Arguments> getArgumentsForValidationTest(){
        return Stream.of(
                Arguments.of(
                        null,getSurname(),getEmail(),getGrade(),
                        "name: " + getDefaultNullConstraintMessage()
                ),
                Arguments.of(
                        "ababababababababababababababababc",getSurname(),getEmail(),getGrade(),
                        "name: length should be lowest or equals then 32"
                ),
                Arguments.of(
                        getName(),null,getEmail(),getGrade(),
                        "surname: " + getDefaultNullConstraintMessage()
                ),
                Arguments.of(
                        getName(),"ababababababababababababababababc",getEmail(),getGrade(),
                        "surname: length should be lowest or equals then 32"
                ),
                Arguments.of(
                        getName(),getSurname(),"not_valid_email",getGrade(),
                        "email: email is not valid"
                ),
                Arguments.of(
                        getName(),getSurname(),getEmail(),null,
                        "grade: " + getDefaultNullConstraintMessage()
                ),
                Arguments.of(
                         getName(),getSurname(),getEmail(),-1F,
                        "grade: grade should be greater or equals then 1"
                )
        );
    }

    @AfterEach
    void closeResources(){
        sessionFactory.close();
        session.getTransaction().commit();
        session.close();
    }

    static String getName(){
        return "name";
    }

    static String getSurname(){
        return "surname";
    }

    static String getEmail(){
        return "email@gmail.com";
    }

    static Float getGrade(){
        return 5F;
    }

    static String getDefaultNullConstraintMessage(){
        return "не должно равняться null";
    }

    private StudentService getStudentService(Session session){
        StudentRepository studentRepository = new StudentRepository(session);
        StudentReadMapper studentReadMapper = Mappers.getMapper(StudentReadMapper.class);
        StudentCreateMapper studentCreateMapper = Mappers.getMapper(StudentCreateMapper.class);
        StudentUpdateMapper studentUpdateMapper = Mappers.getMapper(StudentUpdateMapper.class);
        return new StudentService(studentRepository,studentReadMapper,studentCreateMapper,studentUpdateMapper);
    }
}
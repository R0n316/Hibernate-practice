package ru.alex.HibernatePractice.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.alex.HibernatePractice.dto.course.CourseCreateDto;
import ru.alex.HibernatePractice.dto.course.CourseReadDto;
import ru.alex.HibernatePractice.dto.course.CourseUpdateDto;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseCreateMapper;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseMapper;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseReadMapper;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseUpdateMapper;
import ru.alex.HibernatePractice.repository.CourseRepository;
import ru.alex.HibernatePractice.util.HibernateTestUtil;
import ru.alex.HibernatePractice.util.TestDataImporter;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class CourseServiceTest {

    private SessionFactory sessionFactory;
    private CourseService courseService;

    private final CourseReadDto MATH_DTO  = CourseReadDto.builder()
            .id(1)
            .name("Math")
            .duration(60)
            .students(List.of(1,3))
            .build();

    @BeforeEach
    public void setUp(){
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.initData(sessionFactory);
    }

    @Test
    void findById() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            courseService = getCourseService(session);
            Optional<CourseReadDto> courseReadDto = courseService.findById(MATH_DTO.getId());

            assertThat(courseReadDto).isPresent();
            assertThat(courseReadDto.get()).isEqualTo(MATH_DTO);

            session.getTransaction().commit();

        }
    }

    @Test
    void findAll() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            courseService = getCourseService(session);
            CourseReadDto physicsDto = CourseReadDto.builder()
                    .id(2)
                    .name("physics")
                    .duration(70)
                    .students(List.of(1,3))
                    .build();
            CourseReadDto historyDto = CourseReadDto.builder()
                    .id(3)
                    .name("history")
                    .duration(40)
                    .students(List.of(1,2))
                    .build();

            List<CourseReadDto> expectedDtos = List.of(MATH_DTO,physicsDto,historyDto);
            List<CourseReadDto> actualDtos = courseService.findAll();
            assertThat(actualDtos).hasSize(expectedDtos.size());
            assertThat(actualDtos).isEqualTo(expectedDtos);

            session.getTransaction().commit();
        }
    }

    @Test
    void create() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            courseService = getCourseService(session);
            CourseCreateDto example = CourseCreateDto.builder()
                    .name("example")
                    .description("course description")
                    .duration(37)
                    .build();

            courseService.create(example);
            session.flush();
            Optional<CourseReadDto> courseReadDto = courseService.findById(4);
            assertThat(courseReadDto).isPresent();
            assertThat(courseReadDto.get().getName()).isEqualTo(example.getName());

            session.getTransaction().commit();
        }
    }

    @Test
    void update() {
        CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            courseService = getCourseService(session);

            Optional<CourseReadDto> courseReadDtoOptional = courseService.findById(1);

            assertThat(courseReadDtoOptional).isPresent();

            CourseReadDto courseReadDto = courseReadDtoOptional.get();
            CourseUpdateDto course = courseMapper.mapFrom(courseReadDto);
            course.setName("new name");
            courseService.update(courseReadDto.getId(),course);
//            session.merge(course);
            session.flush();

            Optional<CourseReadDto> courseReadDtoOptional1 = courseService.findById(1);
            assertThat(courseReadDtoOptional1).isPresent();
            assertThat(courseReadDtoOptional1.get().getName()).isNotEqualTo(courseReadDto.getName());

            session.getTransaction().commit();
        }
    }

    @Test
    void delete() {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            courseService = getCourseService(session);
            Optional<CourseReadDto> courseReadDto = courseService.findById(MATH_DTO.getId());
            assertThat(courseReadDto).isPresent();
            courseService.delete(courseReadDto.get().getId());
            session.flush();

            Optional<CourseReadDto> courseReadDto1 = courseService.findById(MATH_DTO.getId());

            assertThat(courseReadDto1).isEmpty();

            session.getTransaction().commit();
        }
    }

    public CourseService getCourseService(Session session){
        CourseRepository courseRepository = new CourseRepository(session);
        CourseReadMapper courseReadMapper = Mappers.getMapper(CourseReadMapper.class);
        CourseCreateMapper courseCreateMapper = Mappers.getMapper(CourseCreateMapper.class);
        CourseUpdateMapper courseUpdateMapper = Mappers.getMapper(CourseUpdateMapper.class);
        return new CourseService(courseRepository,courseReadMapper,courseCreateMapper,courseUpdateMapper);
    }
}
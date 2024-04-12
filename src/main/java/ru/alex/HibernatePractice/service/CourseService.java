package ru.alex.HibernatePractice.service;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import ru.alex.HibernatePractice.dto.course.CourseUpdateDto;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.entity.Enrollment;
import ru.alex.HibernatePractice.mapper.Mapper;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseCreateMapper;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseUpdateMapper;
import ru.alex.HibernatePractice.repository.CourseRepository;
import ru.alex.HibernatePractice.dto.course.CourseCreateDto;
import ru.alex.HibernatePractice.dto.course.CourseReadDto;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseReadMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseReadMapper courseReadMapper;
    private final CourseCreateMapper courseCreateMapper;
    private final CourseUpdateMapper courseUpdateMapper;

    public <T> Optional<T> findById(Integer id, Mapper<Course,T> mapper){
        Optional<Course> course = courseRepository.get(id,getEntityGraphReadProperties());
        return course.map(mapper::mapFrom);
    }


    public Optional<CourseReadDto> findById(Integer id){
        return findById(id,courseReadMapper);
    }

    public List<CourseReadDto> findAll(){
        return courseRepository.getAll(getEntityGraphReadProperties()).stream().map(courseReadMapper::mapFrom).toList();
    }

    public void create(CourseCreateDto courseCreateDto){
        Course course = courseCreateMapper.mapFrom(courseCreateDto);
        validateCourse(course);
        courseRepository.save(course);
    }
    public Boolean update(Integer id, CourseUpdateDto updatedCourse){
        Optional<Course> courseOptional = courseRepository.get(id);
        courseOptional.ifPresent(it -> {
            Course course = courseUpdateMapper.mapFrom(updatedCourse);
            course.setId(id);
            validateCourse(course);
            courseRepository.update(course);
        });
        return courseOptional.isPresent();
    }

    public Boolean delete(Integer id){
        Optional<Course> courseOptional = courseRepository.get(id);
        courseOptional.ifPresent(courseRepository::delete);
        return courseOptional.isPresent();
    }

    void validateCourse(Course course){
        try(ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            var validationResult = validator.validate(course);
            if (!validationResult.isEmpty()) {
                throw new ConstraintViolationException(validationResult);
            }
        }
    }

    public Map<String,Object> getEntityGraphReadProperties(){
        EntityGraph<Course> entityGraph = courseRepository.getEntityManager().createEntityGraph(Course.class);
        entityGraph.addAttributeNodes("enrollments");
        Subgraph<Enrollment> enrollments = entityGraph.addSubgraph("enrollments", Enrollment.class);
        enrollments.addAttributeNodes("student");
        return Map.of(
                GraphSemantic.LOAD.getJakartaHintName(),
                entityGraph
        );
    }
}

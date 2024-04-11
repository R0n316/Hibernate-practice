package ru.alex.HibernatePractice.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import ru.alex.HibernatePractice.dto.course.CourseUpdateDto;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.mapper.Mapper;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseCreateMapper;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseUpdateMapper;
import ru.alex.HibernatePractice.repository.CourseRepository;
import ru.alex.HibernatePractice.dto.course.CourseCreateDto;
import ru.alex.HibernatePractice.dto.course.CourseReadDto;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseReadMapper;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseReadMapper courseReadMapper;
    private final CourseCreateMapper courseCreateMapper;
    private final CourseUpdateMapper courseUpdateMapper;

    public <T> Optional<T> findById(Integer id, Mapper<Course,T> mapper){
        Optional<Course> course = courseRepository.get(id);
        return course.map(mapper::mapFrom);
//        return courseRepository.get(id).map(mapper::mapFrom);
    }


    public Optional<CourseReadDto> findById(Integer id){
        return findById(id,courseReadMapper);
    }

    public List<CourseReadDto> findAll(){
        return courseRepository.getAll().stream().map(courseReadMapper::mapFrom).toList();
    }

    public void create(CourseCreateDto courseCreateDto){
        try(ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()){
            Validator validator = validatorFactory.getValidator();
            Course course = courseCreateMapper.mapFrom(courseCreateDto);
            var validationResult = validator.validate(course);
            if(!validationResult.isEmpty()){
                throw new ConstraintViolationException(validationResult);
            }
            courseRepository.save(course);
        }
    }
    public Boolean update(Integer id, CourseUpdateDto updatedCourse){
        Optional<Course> courseOptional = courseRepository.get(id);
        courseOptional.ifPresent(it -> {
            Course course = courseUpdateMapper.mapFrom(updatedCourse);
            course.setId(id);
            courseRepository.update(course);
        });
        return courseOptional.isPresent();
        // TODO проверить существует ли этот объект, и если существует, то обновить его
        // TODO проверить, делается ли повторный запрос на получение объекта при удалении
    }

    public Boolean delete(Integer id){
        Optional<Course> courseOptional = courseRepository.get(id);
        courseOptional.ifPresent(courseRepository::delete);
        return courseOptional.isPresent();
    }
}

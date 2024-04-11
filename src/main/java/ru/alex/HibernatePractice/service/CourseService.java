package ru.alex.HibernatePractice.service;

import lombok.RequiredArgsConstructor;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.mapper.Mapper;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseCreateMapper;
import ru.alex.HibernatePractice.repository.CourseRepository;
import ru.alex.HibernatePractice.dto.CourseCreateDto;
import ru.alex.HibernatePractice.dto.CourseReadDto;
import ru.alex.HibernatePractice.mapper.courseMapper.CourseReadMapper;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseReadMapper courseReadMapper;
    private final CourseCreateMapper courseCreateMapper;

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
        courseRepository.save(courseCreateMapper.mapFrom(courseCreateDto));
    }
    public boolean update(Integer id, Course updatedCourse){
        Optional<Course> courseOptional = courseRepository.get(id);
        courseOptional.ifPresent(it -> {
            updatedCourse.setId(id);
            courseRepository.update(updatedCourse);
        });
        return courseOptional.isPresent();
        // TODO проверить существует ли этот объект, и если существует, то обновить его
        // TODO проверить, делается ли повторный запрос на получение объекта при удалении
    }

    public boolean delete(Integer id){
        Optional<Course> courseOptional = courseRepository.get(id);
        courseOptional.ifPresent(course -> courseRepository.delete(course));
        return courseOptional.isPresent();
    }
}

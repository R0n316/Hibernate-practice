package ru.alex.HibernatePractice.mapper.courseMapper;


import ru.alex.HibernatePractice.dto.CourseCreateDto;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.mapper.Mapper;

@org.mapstruct.Mapper
public interface CourseCreateMapper extends Mapper<CourseCreateDto, Course> {

    @Override
    Course mapFrom(CourseCreateDto object);

}
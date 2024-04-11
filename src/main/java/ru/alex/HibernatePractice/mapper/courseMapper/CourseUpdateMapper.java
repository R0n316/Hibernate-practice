package ru.alex.HibernatePractice.mapper.courseMapper;

import ru.alex.HibernatePractice.dto.course.CourseUpdateDto;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.mapper.Mapper;

@org.mapstruct.Mapper
public interface CourseUpdateMapper extends Mapper<CourseUpdateDto, Course> {

}

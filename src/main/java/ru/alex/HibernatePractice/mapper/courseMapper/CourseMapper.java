package ru.alex.HibernatePractice.mapper.courseMapper;

import ru.alex.HibernatePractice.dto.course.CourseReadDto;
import ru.alex.HibernatePractice.dto.course.CourseUpdateDto;
import ru.alex.HibernatePractice.mapper.Mapper;

@org.mapstruct.Mapper
public interface CourseMapper extends Mapper<CourseReadDto, CourseUpdateDto>{

}

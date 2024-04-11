package ru.alex.HibernatePractice.mapper.courseMapper;

import ru.alex.HibernatePractice.dto.CourseReadDto;
import ru.alex.HibernatePractice.dto.Dto;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.mapper.Mapper;

@org.mapstruct.Mapper
public interface CourseMapper extends Mapper<CourseReadDto, Course>{

    @Override
    Course mapFrom(CourseReadDto object);
}

package ru.alex.HibernatePractice.mapper.courseMapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.alex.HibernatePractice.dto.course.CourseReadDto;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.entity.Enrollment;
import ru.alex.HibernatePractice.mapper.Mapper;


@org.mapstruct.Mapper(uses = Enrollment.class)
public interface CourseReadMapper extends Mapper<Course, CourseReadDto> {


    @Override
    @IterableMapping(qualifiedByName = "mapToId")
    @Mapping(target = "students", source = "enrollments", qualifiedByName = "mapToId")
    CourseReadDto mapFrom(Course course);


    @Named("mapToId")
    default Integer mapToId(Enrollment enrollment){
        return enrollment.getStudent().getId();
    }
}
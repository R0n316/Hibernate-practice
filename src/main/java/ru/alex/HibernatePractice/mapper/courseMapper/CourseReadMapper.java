package ru.alex.HibernatePractice.mapper.courseMapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.alex.HibernatePractice.dto.CourseReadDto;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.entity.Enrollment;
import ru.alex.HibernatePractice.mapper.Mapper;

import java.util.List;


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

    @Named("enrollmentsToStudentIds")
    default List<Integer> enrollmentsToStudentIds(List<Enrollment> enrollments) {
        return enrollments.stream().map(this::mapToId).toList();
    }
}
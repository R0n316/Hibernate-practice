package ru.alex.HibernatePractice.mapper.studentMapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.alex.HibernatePractice.dto.student.StudentReadDto;
import ru.alex.HibernatePractice.entity.Enrollment;
import ru.alex.HibernatePractice.entity.Student;
import ru.alex.HibernatePractice.mapper.Mapper;

@org.mapstruct.Mapper
public interface StudentReadMapper extends Mapper<Student, StudentReadDto> {
    @Override
    @IterableMapping(qualifiedByName = "mapToId")
    @Mapping(target = "courses",source = "enrollments",qualifiedByName = "mapToId")
    StudentReadDto mapFrom(Student object);

    @Named("mapToId")
    default Integer mapToId(Enrollment enrollment){
        return enrollment.getCourse().getId();
    }
}

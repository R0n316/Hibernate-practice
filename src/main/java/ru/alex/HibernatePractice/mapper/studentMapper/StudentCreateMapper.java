package ru.alex.HibernatePractice.mapper.studentMapper;

import ru.alex.HibernatePractice.dto.student.StudentCreateDto;
import ru.alex.HibernatePractice.entity.Student;
import ru.alex.HibernatePractice.mapper.Mapper;

@org.mapstruct.Mapper
public interface StudentCreateMapper extends Mapper<StudentCreateDto, Student> {
}

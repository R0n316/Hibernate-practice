package ru.alex.HibernatePractice.mapper.studentMapper;

import ru.alex.HibernatePractice.dto.student.StudentUpdateDto;
import ru.alex.HibernatePractice.entity.Student;
import ru.alex.HibernatePractice.mapper.Mapper;

@org.mapstruct.Mapper
public interface StudentUpdateMapper extends Mapper<StudentUpdateDto, Student> {

}

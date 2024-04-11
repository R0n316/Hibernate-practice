package ru.alex.HibernatePractice.mapper.studentMapper;

import ru.alex.HibernatePractice.dto.student.StudentReadDto;
import ru.alex.HibernatePractice.dto.student.StudentUpdateDto;
import ru.alex.HibernatePractice.mapper.Mapper;

@org.mapstruct.Mapper
public interface StudentMapper extends Mapper<StudentReadDto, StudentUpdateDto> {
}

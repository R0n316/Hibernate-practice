package ru.alex.HibernatePractice.service;

import lombok.RequiredArgsConstructor;
import ru.alex.HibernatePractice.dto.student.StudentCreateDto;
import ru.alex.HibernatePractice.dto.student.StudentReadDto;
import ru.alex.HibernatePractice.dto.student.StudentUpdateDto;
import ru.alex.HibernatePractice.entity.Student;
import ru.alex.HibernatePractice.mapper.studentMapper.StudentCreateMapper;
import ru.alex.HibernatePractice.mapper.studentMapper.StudentReadMapper;
import ru.alex.HibernatePractice.mapper.studentMapper.StudentUpdateMapper;
import ru.alex.HibernatePractice.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentReadMapper studentReadMapper;
    private final StudentCreateMapper studentCreateMapper;
    private final StudentUpdateMapper studentUpdateMapper;

    public Optional<StudentReadDto> findById(Integer id){
        return studentRepository.get(id).map(studentReadMapper::mapFrom);
    }

    public List<StudentReadDto> findAll(){
        return studentRepository.getAll().stream().map(studentReadMapper::mapFrom).toList();
    }

    public void create(StudentCreateDto studentCreateDto){
        studentRepository.save(studentCreateMapper.mapFrom(studentCreateDto));
    }

    public Boolean update(Integer id, StudentUpdateDto studentUpdateDto){
        Optional<Student> studentOptional = studentRepository.get(id);
        studentOptional.ifPresent(it -> {
            Student student = studentUpdateMapper.mapFrom(studentUpdateDto);
            student.setId(id);
            studentRepository.update(student);
        });
        return studentOptional.isPresent();
    }

    public Boolean delete(Integer id){
        Optional<Student> student = studentRepository.get(id);
        student.ifPresent(studentRepository::delete);
        return student.isPresent();
    }
}

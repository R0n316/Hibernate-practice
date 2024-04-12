package ru.alex.HibernatePractice.service;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import ru.alex.HibernatePractice.dto.student.StudentCreateDto;
import ru.alex.HibernatePractice.dto.student.StudentReadDto;
import ru.alex.HibernatePractice.dto.student.StudentUpdateDto;
import ru.alex.HibernatePractice.entity.Student;
import ru.alex.HibernatePractice.mapper.studentMapper.StudentCreateMapper;
import ru.alex.HibernatePractice.mapper.studentMapper.StudentReadMapper;
import ru.alex.HibernatePractice.mapper.studentMapper.StudentUpdateMapper;
import ru.alex.HibernatePractice.repository.StudentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentReadMapper studentReadMapper;
    private final StudentCreateMapper studentCreateMapper;
    private final StudentUpdateMapper studentUpdateMapper;

    public Optional<StudentReadDto> findById(Integer id){
        return studentRepository.get(id, getEntityGraphReadProperties()).map(studentReadMapper::mapFrom);
    }

    public List<StudentReadDto> findAll(){
        return studentRepository.getAll(getEntityGraphReadProperties()).stream().map(studentReadMapper::mapFrom).toList();
    }

    public void create(StudentCreateDto studentCreateDto){
        Student student = studentCreateMapper.mapFrom(studentCreateDto);
        validateStudent(student);
        studentRepository.save(student);
    }

    public Boolean update(Integer id, StudentUpdateDto studentUpdateDto){
        Optional<Student> studentOptional = studentRepository.get(id);
        studentOptional.ifPresent(it -> {
            Student student = studentUpdateMapper.mapFrom(studentUpdateDto);
            student.setId(id);
            validateStudent(student);
            studentRepository.update(student);
        });
        return studentOptional.isPresent();
    }

    public Boolean delete(Integer id){
        Optional<Student> student = studentRepository.get(id);
        student.ifPresent(studentRepository::delete);
        return student.isPresent();
    }

    void validateStudent(Student student){
        try(ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()){
            Validator validator = validatorFactory.getValidator();
            var validationResult = validator.validate(student);
            if(!validationResult.isEmpty()){
                throw new ConstraintViolationException(validationResult);
            }
        }
    }

    private Map<String, Object> getEntityGraphReadProperties(){
        EntityGraph<Student> entityGraph = studentRepository.getEntityManager().createEntityGraph(Student.class);
        entityGraph.addAttributeNodes("enrollments");
        Subgraph<Object> enrollments = entityGraph.addSubgraph("enrollments");
        enrollments.addAttributeNodes("course");
        return Map.of(
                GraphSemantic.FETCH.getJakartaHintName(),
                entityGraph
        );
    }
}

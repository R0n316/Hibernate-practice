package ru.alex.HibernatePractice.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.alex.HibernatePractice.entity.Course;
import ru.alex.HibernatePractice.entity.Student;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EnrollmentFilter {
    private Course course;
    private Student student;
}

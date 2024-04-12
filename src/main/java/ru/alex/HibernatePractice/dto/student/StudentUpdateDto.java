package ru.alex.HibernatePractice.dto.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class StudentUpdateDto {
    private String name;
    private String surname;
    private String email;
    private LocalDate birthDate;
    private Float grade;
}

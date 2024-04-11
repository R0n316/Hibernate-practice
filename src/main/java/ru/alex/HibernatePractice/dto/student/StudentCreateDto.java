package ru.alex.HibernatePractice.dto.student;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class StudentCreateDto {
    private String name;
    private String surname;
    private String email;
    private LocalDate birthDate;
    private Float grade;
}

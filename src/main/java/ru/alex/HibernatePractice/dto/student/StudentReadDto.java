package ru.alex.HibernatePractice.dto.student;

import lombok.*;
import ru.alex.HibernatePractice.dto.Dto;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class StudentReadDto implements Dto {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private LocalDate birthDate;
    private Float grade;
    private List<Integer> courses;
}

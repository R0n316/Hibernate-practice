package ru.alex.HibernatePractice.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.alex.HibernatePractice.dto.Dto;

@Getter
@Setter
@AllArgsConstructor
public class CourseUpdateDto implements Dto {
    private String name;
    private String description;
    private Integer duration;
}

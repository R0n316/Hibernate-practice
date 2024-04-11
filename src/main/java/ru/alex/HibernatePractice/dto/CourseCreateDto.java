package ru.alex.HibernatePractice.dto;

import lombok.*;

@Builder
//public record CourseCreateDto(
//        String name,
//        String description,
//        Integer duration
//) {
//}
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class CourseCreateDto implements Dto {
    private String name;
    private String description;
    private Integer duration;
}
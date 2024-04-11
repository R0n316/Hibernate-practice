package ru.alex.HibernatePractice.dto.course;

import lombok.*;
import ru.alex.HibernatePractice.dto.Dto;

import java.util.List;

@Builder
//public record CourseReadDto (
//        Integer id,
//        String name,
//        String description,
//        Integer duration,
//        List<Integer> students
//){
//
//}
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class CourseReadDto implements Dto {
    private Integer id;
    private String name;
    private String description;
    private Integer duration;
    private List<Integer> students;
}
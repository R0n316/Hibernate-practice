package ru.alex.HibernatePractice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "enrollments")
@EqualsAndHashCode(exclude = "enrollments")
@Builder
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private int duration;

    @OneToMany(mappedBy = "course",cascade = CascadeType.REMOVE)
    private List<Enrollment> enrollments;
}

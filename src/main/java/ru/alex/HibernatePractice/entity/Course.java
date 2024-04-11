package ru.alex.HibernatePractice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "enrollments")
@EqualsAndHashCode(exclude = "enrollments")
@Builder
@Entity
public class Course implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "name should not be null")
    @Length(max = 64, message = "name should be lowest or equals then 64 symbols")
    private String name;

    private String description;

    @NotNull(message = "duration should not be null")
    @Min(value = 10, message = "course duration should be greater or equals the 10 hours")
    private Integer duration;

    @OneToMany(mappedBy = "course",cascade = CascadeType.REMOVE)
    private List<Enrollment> enrollments;
}

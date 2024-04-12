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

    @NotNull
    @Length(max = 64, message = "name should be lowest or equals then 64 symbols")
    private String name;

    private String description;

    @NotNull
    @Min(value = 10, message = "course duration should be greater or equals the 10 hours")
    private Integer duration;

    @OneToMany(mappedBy = "course",cascade = CascadeType.REMOVE)
    private List<Enrollment> enrollments;
}

package ru.alex.HibernatePractice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "enrollments")
@EqualsAndHashCode(of = "email")
@Builder
@Entity
public class Student implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    private String name;

    private String surname;

    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private Float grade;

    @OneToMany(mappedBy = "student",cascade = CascadeType.REMOVE)
    private List<Enrollment> enrollments;
}

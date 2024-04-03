package ru.alex.HibernatePractice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "email")
@EqualsAndHashCode(exclude = "enrollments")
@Builder
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String surname;

    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private float grade;

    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;
}

package ru.alex.HibernatePractice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @ManyToOne
    @JoinColumn(name = "student_id",referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id",referencedColumnName = "id")
    private Course course;
}

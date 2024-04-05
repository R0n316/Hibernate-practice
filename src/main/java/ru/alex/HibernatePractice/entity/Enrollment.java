package ru.alex.HibernatePractice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(exclude = "enrollmentDate")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "enrollment_date")
    @CreationTimestamp
    private LocalDate enrollmentDate;

    @Column(name = "course_grade")
    private Float courseGrade;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "student_id",referencedColumnName = "id")
    private Student student;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id",referencedColumnName = "id")
    private Course course;
}

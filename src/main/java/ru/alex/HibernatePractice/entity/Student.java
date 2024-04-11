package ru.alex.HibernatePractice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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

    @NotNull(message = "name should not be null")
    @Length(max = 32, message = "name length should be lowest or equals then 32")
    private String name;

    @NotNull(message = "surname should not be null")
    @Length(max = 32, message = "surname length should be lowest or equals then 32")
    private String surname;

    @Email(message = "email is not valid")
    @Column(unique = true)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Min(value = 0, message = "grade should be greater or equals then 0")
    private Float grade;

    @OneToMany(mappedBy = "student",cascade = CascadeType.REMOVE)
    private List<Enrollment> enrollments;

}

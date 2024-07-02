package dev.profitsoft.internship.hw_block_02.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Student entity
 */
@Getter
@Setter
@Entity
@Table(name = "student", indexes = {
        @Index(name = "idx_student_username", columnList = "username")
})
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Student {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Username is required")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull(message = "Email is required")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", length = 30)
    private String firstName;

    @Column(name = "last_name", length = 30)
    private String lastName;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
package dev.profitsoft.internship.hw_block_02.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Course entity
 */
@Getter
@Setter
@Entity
@Table(name = "course")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Course name is required")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @ToString.Exclude
    @OneToMany(mappedBy = "course")
    private Set<Student> students = new LinkedHashSet<>();
}
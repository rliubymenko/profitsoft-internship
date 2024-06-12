package dev.profitsoft.internship.hw_block_02.dto.details;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Getter
@Builder
@ToString
@Jacksonized
public class StudentDetailsDto {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthDay;

    private CourseDetailsDto course;
}

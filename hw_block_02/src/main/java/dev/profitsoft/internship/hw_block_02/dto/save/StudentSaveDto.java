package dev.profitsoft.internship.hw_block_02.dto.save;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Getter
@Builder
@Jacksonized
public class StudentSaveDto {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthDay;

    private Long courseId;
}

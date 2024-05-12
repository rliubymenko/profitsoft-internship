package dev.profitsoft.internship.hw_block_02.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class StudentPageable {

    private String username;

    private String firstName;

    private String lastName;

    private Long courseId;

    private Integer page;

    private Integer size;
}

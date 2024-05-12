package dev.profitsoft.internship.hw_block_02.dto.details;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class CourseDetailsDto {

    private Long id;

    private String name;

    private String description;

    private Boolean isCompleted;
}

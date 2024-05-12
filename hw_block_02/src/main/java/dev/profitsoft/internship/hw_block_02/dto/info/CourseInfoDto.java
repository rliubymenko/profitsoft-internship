package dev.profitsoft.internship.hw_block_02.dto.info;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class CourseInfoDto {

    private String name;

    private String description;

    private Boolean isCompleted;
}

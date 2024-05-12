package dev.profitsoft.internship.hw_block_02.dto.save;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class CourseSaveDto {

    @NotBlank(message = "Course name is required")
    private String name;

    private String description;

    private Boolean isCompleted;
}

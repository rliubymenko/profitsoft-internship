package dev.profitsoft.internship.hw_block_02.dto;

import dev.profitsoft.internship.hw_block_02.dto.info.StudentInfoDto;

import java.util.List;

public record StudentPage(List<StudentInfoDto> students, int totalPages) {
}

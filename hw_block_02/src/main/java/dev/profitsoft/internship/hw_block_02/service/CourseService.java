package dev.profitsoft.internship.hw_block_02.service;

import dev.profitsoft.internship.hw_block_02.dto.details.CourseDetailsDto;
import dev.profitsoft.internship.hw_block_02.dto.info.CourseInfoDto;
import dev.profitsoft.internship.hw_block_02.dto.save.CourseSaveDto;

import java.util.List;

public interface CourseService {

    CourseDetailsDto create(CourseSaveDto courseSaveDto);

    CourseDetailsDto update(Long id, CourseSaveDto courseSaveDto);

    void delete(Long id);

    List<CourseInfoDto> findAll();
}

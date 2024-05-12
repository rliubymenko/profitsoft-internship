package dev.profitsoft.internship.hw_block_02.service.impl;

import dev.profitsoft.internship.hw_block_02.dto.details.CourseDetailsDto;
import dev.profitsoft.internship.hw_block_02.dto.info.CourseInfoDto;
import dev.profitsoft.internship.hw_block_02.dto.save.CourseSaveDto;
import dev.profitsoft.internship.hw_block_02.entity.Course;
import dev.profitsoft.internship.hw_block_02.exception.DataValidationException;
import dev.profitsoft.internship.hw_block_02.repository.CourseRepository;
import dev.profitsoft.internship.hw_block_02.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Course service
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseDetailsDto create(CourseSaveDto courseSaveDto) {
        if (courseRepository.existsByName(courseSaveDto.getName())) {
            throw new DataValidationException("Name must be unique");
        }

        Course course = mapToCourse(courseSaveDto, null);
        Course savedCourse = courseRepository.save(course);
        return mapToDetailsDto(savedCourse);
    }

    @Override
    public CourseDetailsDto update(Long id, CourseSaveDto courseSaveDto) {
        if (courseRepository.existsByNameAndIdIsNot(courseSaveDto.getName(), id)) {
            throw new DataValidationException("Name must be unique");
        }
        Course course = mapToCourse(courseSaveDto, id);
        Course savedCourse = courseRepository.save(course);
        return mapToDetailsDto(savedCourse);
    }

    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public List<CourseInfoDto> findAll() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream()
                .map(course -> CourseInfoDto.builder()
                        .name(course.getName())
                        .description(course.getDescription())
                        .isCompleted(course.getIsCompleted())
                        .build())
                .toList();
    }

    private static Course mapToCourse(CourseSaveDto courseSaveDto, Long id) {
        return Course.builder()
                .id(id)
                .name(courseSaveDto.getName())
                .description(courseSaveDto.getDescription())
                .isCompleted(courseSaveDto.getIsCompleted())
                .build();
    }

    private static CourseDetailsDto mapToDetailsDto(Course savedCourse) {
        return CourseDetailsDto.builder()
                .id(savedCourse.getId())
                .name(savedCourse.getName())
                .description(savedCourse.getDescription())
                .isCompleted(savedCourse.getIsCompleted())
                .build();
    }
}

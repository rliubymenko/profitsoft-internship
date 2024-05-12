package dev.profitsoft.internship.hw_block_02.controller;

import dev.profitsoft.internship.hw_block_02.dto.details.CourseDetailsDto;
import dev.profitsoft.internship.hw_block_02.dto.info.CourseInfoDto;
import dev.profitsoft.internship.hw_block_02.dto.save.CourseSaveDto;
import dev.profitsoft.internship.hw_block_02.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Course controller
 */
@RestController
@RequestMapping(value = "/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Create a new course
     *
     * @param courseSaveDto a save dto
     * @return detailed information about saved course
     */
    @PostMapping
    public ResponseEntity<CourseDetailsDto> createCourse(@Valid @RequestBody CourseSaveDto courseSaveDto) {
        CourseDetailsDto savedDto = courseService.create(courseSaveDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    /**
     * Update course by id
     *
     * @param id            the course id
     * @param courseSaveDto a save dto
     * @return detailed information about saved course
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseDetailsDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseSaveDto courseSaveDto) {
        CourseDetailsDto savedDto = courseService.update(id, courseSaveDto);
        return new ResponseEntity<>(savedDto, HttpStatus.OK);
    }

    /**
     * Retrieve all courses
     *
     * @return all courses
     */
    @GetMapping
    public ResponseEntity<List<CourseInfoDto>> findAllCourses() {
        List<CourseInfoDto> courseInfoDtos = courseService.findAll();
        return ResponseEntity.ok(courseInfoDtos);
    }

    /**
     * Delete course by id
     *
     * @param id the course id
     * @return status ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

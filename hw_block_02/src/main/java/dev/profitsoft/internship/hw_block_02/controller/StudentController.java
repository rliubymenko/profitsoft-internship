package dev.profitsoft.internship.hw_block_02.controller;

import dev.profitsoft.internship.hw_block_02.dto.StudentPage;
import dev.profitsoft.internship.hw_block_02.dto.StudentPageable;
import dev.profitsoft.internship.hw_block_02.dto.details.StudentDetailsDto;
import dev.profitsoft.internship.hw_block_02.dto.save.StudentSaveDto;
import dev.profitsoft.internship.hw_block_02.service.StudentService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Student controller
 */
@RestController
@RequestMapping(value = "/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Create a new student
     *
     * @param studentSaveDto a save student dto
     * @return detailed information about a saved student
     */
    @PostMapping
    public ResponseEntity<StudentDetailsDto> createStudent(@RequestBody StudentSaveDto studentSaveDto) {
        StudentDetailsDto savedDto = studentService.create(studentSaveDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    /**
     * Update a student
     *
     * @param id             the student id
     * @param studentSaveDto a save student dto
     * @return detailed information about a saved student
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentDetailsDto> updateStudent(@PathVariable Long id, @RequestBody StudentSaveDto studentSaveDto) {
        StudentDetailsDto savedDto = studentService.update(id, studentSaveDto);
        return new ResponseEntity<>(savedDto, HttpStatus.OK);
    }

    /**
     * Find student by id
     *
     * @param id the id of the student
     * @return detailed information about a saved student
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailsDto> findStudent(@PathVariable Long id) {
        StudentDetailsDto studentDetailsDto = studentService.findById(id);
        return ResponseEntity.ok(studentDetailsDto);
    }

    /**
     * Delete a student by its id
     *
     * @param id the student id
     * @return status ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Return student page
     *
     * @param studentPageable a student pageable
     * @return information about total pages and list of students
     */
    @PostMapping("/_list")
    public ResponseEntity<StudentPage> findListOfStudents(@RequestBody StudentPageable studentPageable) {
        StudentPage studentPage = studentService.findAll(studentPageable);
        return ResponseEntity.ok(studentPage);
    }

    /**
     * Generate report in csv format with students
     *
     * @param studentPageable a student pageable
     * @return information about total pages and list of students in .csv format
     */
    @PostMapping(value = "/_report", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getCvsFileWithStudents(@RequestBody StudentPageable studentPageable) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student.csv");

        byte[] report = studentService.getStudentReport(studentPageable);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers)
                .body(report);
    }

    /**
     * Upload students from json file to db
     *
     * @param multipartFile a json file
     * @return count of successfully uploaded students and failures
     */
    @PostMapping(value = "/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile multipartFile) throws FileUploadException {
        Map<String, String> uploadResult = studentService.upload(multipartFile);
        return ResponseEntity.ok(uploadResult);
    }
}

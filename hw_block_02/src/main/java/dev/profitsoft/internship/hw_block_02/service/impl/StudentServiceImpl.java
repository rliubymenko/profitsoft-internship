package dev.profitsoft.internship.hw_block_02.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.profitsoft.internship.hw_block_02.dto.StudentPage;
import dev.profitsoft.internship.hw_block_02.dto.StudentPageable;
import dev.profitsoft.internship.hw_block_02.dto.details.CourseDetailsDto;
import dev.profitsoft.internship.hw_block_02.dto.details.StudentDetailsDto;
import dev.profitsoft.internship.hw_block_02.dto.info.StudentInfoDto;
import dev.profitsoft.internship.hw_block_02.dto.save.StudentSaveDto;
import dev.profitsoft.internship.hw_block_02.entity.Course;
import dev.profitsoft.internship.hw_block_02.entity.Student;
import dev.profitsoft.internship.hw_block_02.exception.DataValidationException;
import dev.profitsoft.internship.hw_block_02.repository.CourseRepository;
import dev.profitsoft.internship.hw_block_02.repository.StudentRepository;
import dev.profitsoft.internship.hw_block_02.service.StudentService;
import dev.profitsoft.internship.hw_block_02.util.CsvUtil;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Student service
 */
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        objectMapper = new ObjectMapper();
    }

    @Override
    public StudentDetailsDto create(StudentSaveDto studentSaveDto) {
        Course course = null;
        if (studentSaveDto.getCourseId() != null) {
            course = courseRepository.findById(studentSaveDto.getCourseId()).get();
        }

        if (studentSaveDto.getUsername().isBlank() || studentRepository.existsByUsername(studentSaveDto.getUsername())) {
            throw new DataValidationException("Username must be unique and not null");
        }
        if (studentSaveDto.getEmail().isBlank() || studentRepository.existsByEmail(studentSaveDto.getEmail())) {
            throw new DataValidationException("Email must be unique and not null");
        }

        Student student = mapToStudent(studentSaveDto, course, null);
        Student savedStudent = studentRepository.save(student);

        return mapToDetails(savedStudent);
    }

    @Override
    public StudentDetailsDto update(Long id, StudentSaveDto studentSaveDto) {
        Course course = null;
        if (studentSaveDto.getCourseId() != null) {
            course = courseRepository.findById(studentSaveDto.getCourseId()).get();
        }

        if (studentSaveDto.getUsername().isBlank() || studentRepository.existsByUsernameAndIdIsNot(studentSaveDto.getUsername(), id)) {
            throw new DataValidationException("Username must be unique and not null");
        }
        if (!studentRepository.existsById(id)) {
            throw new DataValidationException("Must have a valid id");
        }

        Student student = mapToStudent(studentSaveDto, course, id);
        Student savedStudent = studentRepository.save(student);

        return mapToDetails(savedStudent);
    }

    @Override
    public StudentDetailsDto findById(Long id) {
        Optional<Student> maybeStudent = studentRepository.findById(id);
        if (maybeStudent.isPresent()) {
            Student student = maybeStudent.get();

            return mapToDetails(student);
        }
        return null;
    }

    @Override
    public StudentPage findAll(StudentPageable studentPageable) {
        Student studentProbe = getStudentProbe(studentPageable);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("course.name", "course.description", "course.isCompleted", "course.students");

        Example<Student> example = Example.of(studentProbe, exampleMatcher);
        PageRequest pageable = PageRequest.of(studentPageable.getPage(), studentPageable.getSize());

        Page<Student> studentPage = studentRepository.findAll(example, pageable);

        List<StudentInfoDto> studentInfoDtos = studentPage.getContent()
                .stream()
                .map(student -> StudentInfoDto.builder()
                        .id(student.getId())
                        .username(student.getUsername())
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .build())
                .toList();

        return new StudentPage(studentInfoDtos, studentPage.getTotalPages());
    }

    @Override
    public byte[] getStudentReport(StudentPageable studentPageable) {
        StudentPage page = findAll(studentPageable);
        return CsvUtil.generateReport(page.students());
    }

    private Student getStudentProbe(StudentPageable studentPageable) {
        Course course = null;
        if (studentPageable.getCourseId() != null) {
            course = courseRepository.findById(studentPageable.getCourseId()).get();
        }

        return Student.builder()
                .username(studentPageable.getUsername())
                .firstName(studentPageable.getFirstName())
                .lastName(studentPageable.getLastName())
                .course(course)
                .build();
    }

    @Override
    public Map<String, String> upload(MultipartFile file) throws FileUploadException {
        try {
            byte[] fileBytes = file.getBytes();
            AtomicInteger failureCounter = new AtomicInteger();
            List<Student> students = objectMapper.readValue(fileBytes, new TypeReference<List<StudentInfoDto>>() {
                    })
                    .stream()
                    .map(studentInfoDto -> {
                        boolean isExist = studentRepository.existsByUsername(studentInfoDto.getUsername());

                        if (!isExist) {
                            return mapToStudent(studentInfoDto, null);
                        }
                        failureCounter.getAndIncrement();
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toList();

            List<Student> savedStudents = studentRepository.saveAll(students);

            Map<String, String> statistics = new HashMap<>();
            statistics.put("success", String.valueOf(savedStudents.size()));
            statistics.put("failure", String.valueOf(failureCounter.get()));

            return statistics;
        } catch (IOException e) {
            throw new FileUploadException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    private StudentDetailsDto mapToDetails(Student student) {
        return StudentDetailsDto.builder()
                .id(student.getId())
                .username(student.getUsername())
                .email(student.getEmail())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .birthDay(student.getBirthDay())
                .course(getCourseDetailsDto(student.getCourse()))
                .build();
    }

    private CourseDetailsDto getCourseDetailsDto(Course course) {
        if (course != null) {
            return CourseDetailsDto.builder()
                    .id(course.getId())
                    .name(course.getName())
                    .description(course.getDescription())
                    .isCompleted(course.getIsCompleted())
                    .build();
        }
        return null;
    }

    private static Student mapToStudent(StudentInfoDto studentInfoDto, Course course) {
        return Student.builder()
                .username(studentInfoDto.getUsername())
                .firstName(studentInfoDto.getFirstName())
                .lastName(studentInfoDto.getLastName())
                .course(course)
                .build();
    }

    private static Student mapToStudent(StudentSaveDto studentSaveDto, Course course, Long id) {
        return Student.builder()
                .id(id)
                .username(studentSaveDto.getUsername())
                .email(studentSaveDto.getEmail())
                .firstName(studentSaveDto.getFirstName())
                .lastName(studentSaveDto.getLastName())
                .birthDay(studentSaveDto.getBirthDay())
                .course(course)
                .build();
    }
}

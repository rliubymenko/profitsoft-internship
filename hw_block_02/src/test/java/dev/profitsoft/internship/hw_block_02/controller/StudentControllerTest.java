package dev.profitsoft.internship.hw_block_02.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.profitsoft.internship.hw_block_02.HwBlock02Application;
import dev.profitsoft.internship.hw_block_02.dto.StudentPage;
import dev.profitsoft.internship.hw_block_02.dto.StudentPageable;
import dev.profitsoft.internship.hw_block_02.dto.details.StudentDetailsDto;
import dev.profitsoft.internship.hw_block_02.dto.save.StudentSaveDto;
import dev.profitsoft.internship.hw_block_02.entity.Student;
import dev.profitsoft.internship.hw_block_02.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = HwBlock02Application.class)
@AutoConfigureMockMvc
@Transactional
class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentSaveDto studentSaveDto;

    @BeforeEach
    void init() {
        studentSaveDto = StudentSaveDto.builder()
                .username("User name")
                .firstName("FirstName")
                .lastName("LastName")
                .birthDay(LocalDate.of(2001, 1, 1))
                .courseId(5L)
                .build();
    }

    @AfterEach
    public void afterEach() {
        studentRepository.deleteByUsername("User name");
    }

    @Test
    void shouldCreateStudent() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentSaveDto))
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("application/json"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        StudentDetailsDto studentDetailsDto = parseResponse(mvcResult, StudentDetailsDto.class);

        assertThat(studentDetailsDto.getUsername()).isEqualTo("User name");
        assertThat(studentDetailsDto.getFirstName()).isEqualTo("FirstName");
        assertThat(studentDetailsDto.getLastName()).isEqualTo("LastName");
        assertThat(studentDetailsDto.getCourse().getId()).isEqualTo(5);

        Student student = studentRepository.findById(studentDetailsDto.getId()).orElse(null);
        assertThat(student).isNotNull();
        assertThat(student.getUsername()).isEqualTo("User name");
        assertThat(student.getFirstName()).isEqualTo("FirstName");
        assertThat(student.getLastName()).isEqualTo("LastName");
        assertThat(student.getCourse().getId()).isEqualTo(5);
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/v1/students/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentSaveDto))
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        StudentDetailsDto studentDetailsDto = parseResponse(mvcResult, StudentDetailsDto.class);

        assertThat(studentDetailsDto.getUsername()).isEqualTo("User name");
        assertThat(studentDetailsDto.getFirstName()).isEqualTo("FirstName");
        assertThat(studentDetailsDto.getLastName()).isEqualTo("LastName");
        assertThat(studentDetailsDto.getCourse().getId()).isEqualTo(5);

        Student student = studentRepository.findById(studentDetailsDto.getId()).orElse(null);
        assertThat(student).isNotNull();
        assertThat(student.getUsername()).isEqualTo("User name");
        assertThat(student.getFirstName()).isEqualTo("FirstName");
        assertThat(student.getLastName()).isEqualTo("LastName");
        assertThat(student.getCourse().getId()).isEqualTo(5);
    }

    @Test
    void findStudent() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/students/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void findListOfStudents() throws Exception {
        StudentPageable studentPageable = StudentPageable.builder()
                .page(1)
                .size(5)
                .build();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/v1/students/_list")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(studentPageable)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("application/json"))
                .andReturn();

        StudentPage studentPage = parseResponse(mvcResult, StudentPage.class);

        assertThat(studentPage.students()).isNotNull();
    }

    private <T> T parseResponse(MvcResult mvcResult, Class<T> clazz) {
        try {
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), clazz);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Error parsing json", e);
        }
    }
}
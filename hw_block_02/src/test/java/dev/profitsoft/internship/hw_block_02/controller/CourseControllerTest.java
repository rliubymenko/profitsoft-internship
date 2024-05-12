package dev.profitsoft.internship.hw_block_02.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.profitsoft.internship.hw_block_02.HwBlock02Application;
import dev.profitsoft.internship.hw_block_02.dto.details.CourseDetailsDto;
import dev.profitsoft.internship.hw_block_02.dto.info.CourseInfoDto;
import dev.profitsoft.internship.hw_block_02.dto.save.CourseSaveDto;
import dev.profitsoft.internship.hw_block_02.entity.Course;
import dev.profitsoft.internship.hw_block_02.repository.CourseRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = HwBlock02Application.class)
@AutoConfigureMockMvc
@Transactional
class CourseControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseSaveDto courseSaveDto;

    @BeforeEach
    void init() {
        courseSaveDto = CourseSaveDto.builder()
                .name("Course")
                .description("Description")
                .isCompleted(true)
                .build();
    }


    @AfterEach
    public void afterEach() {
        courseRepository.deleteByName("Course");
    }

    @Test
    void shouldCreateCourse() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseSaveDto))
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(courseSaveDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        CourseDetailsDto courseDetailsDto = parseResponse(mvcResult, CourseDetailsDto.class);

        assertThat(courseDetailsDto.getName()).isEqualTo("Course");
        assertThat(courseDetailsDto.getDescription()).isEqualTo("Description");
        assertThat(courseDetailsDto.getIsCompleted()).isTrue();

        Course course = courseRepository.findById(courseDetailsDto.getId()).orElse(null);
        assertThat(course).isNotNull();
        assertThat(course.getName()).isEqualTo("Course");
        assertThat(course.getDescription()).isEqualTo("Description");
        assertThat(course.getIsCompleted()).isTrue();
    }

    @Test
    void updateCourse() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/v1/courses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseSaveDto))
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(courseSaveDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CourseDetailsDto courseDetailsDto = parseResponse(mvcResult, CourseDetailsDto.class);

        assertThat(courseDetailsDto.getName()).isEqualTo("Course");
        assertThat(courseDetailsDto.getDescription()).isEqualTo("Description");
        assertThat(courseDetailsDto.getIsCompleted()).isTrue();

        Course course = courseRepository.findById(courseDetailsDto.getId()).orElse(null);
        assertThat(course).isNotNull();
        assertThat(course.getName()).isEqualTo("Course");
        assertThat(course.getDescription()).isEqualTo("Description");
        assertThat(course.getIsCompleted()).isTrue();

    }

    @Test
    void findAllCourses() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/v1/courses"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("application/json"))
                .andReturn();

        List<CourseInfoDto> courseInfoDtos = parseResponse(mvcResult, List.class);

        assertThat(courseInfoDtos).hasSizeGreaterThan(1);
    }

    private <T> T parseResponse(MvcResult mvcResult, Class<T> clazz) {
        try {
            return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), clazz);
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Error parsing json", e);
        }
    }
}
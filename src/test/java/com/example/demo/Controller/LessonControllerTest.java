package com.example.demo.Controller;

import com.example.demo.Model.Lesson;
import com.example.demo.Repository.LessonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.transaction.Transactional;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonRepository lessonRepository;

    private Lesson testLesson;

    @BeforeEach
    void setUp() {
        testLesson = new Lesson();
        testLesson.setTitle("How to get rich");
        testLesson.setDeliveredOn(new Date());
        lessonRepository.save(testLesson);
    }

    @Test
    @Transactional
    @Rollback
    public void testGetAllLessons() throws Exception {
        MockHttpServletRequestBuilder request = get("/lessons");
        this.mockMvc.perform(request).andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateALesson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Lesson newLesson = new Lesson();
        newLesson.setTitle("How to get rich 2");
        newLesson.setDeliveredOn(new Date());

        String json = mapper.writeValueAsString(newLesson);

        MockHttpServletRequestBuilder request = post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mockMvc.perform(request)
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title", is("How to get rich 2")));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateLesson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Lesson newLesson = new Lesson();
        newLesson.setTitle("How to get poor");
        newLesson.setDeliveredOn(new Date());

        String json = mapper.writeValueAsString(newLesson);

        MockHttpServletRequestBuilder request = patch("/lessons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mockMvc.perform(request)
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title", is("How to get poor")));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetLessonByTitle() throws Exception {
        MockHttpServletRequestBuilder request = get("/lessons/find/How to get rich");
        this.mockMvc.perform(request).andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetLessonByDateRange() throws Exception {
        MockHttpServletRequestBuilder request = get("/lessons/between?date1=2021-08-01&date2=2021-10-01");
        this.mockMvc.perform(request).andExpect(jsonPath("$[0].id").exists());
    }
}
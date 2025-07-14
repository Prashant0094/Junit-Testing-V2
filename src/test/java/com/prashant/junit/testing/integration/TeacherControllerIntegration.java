package com.prashant.junit.testing.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prashant.junit.testing.dto.TeacherRequestDto;
import com.prashant.junit.testing.dto.TeacherResponseDto;

import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Rollback
public class TeacherControllerIntegration {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldCreateTeacherSuccessfully() throws Exception {
		TeacherRequestDto teacher = new TeacherRequestDto("Teacher1", "teacher@email.com", "Password");

		mockMvc.perform(post("/teacher/create").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(teacher))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.teacherEmail").value("teacher@email.com"));
	}

	@Test
	void updateTeacherShouldUpdateTeacher() throws Exception {
		TeacherRequestDto original = new TeacherRequestDto("Teacher1", "teacher@email.com", "Password");

		String createResponse = mockMvc
				.perform(post("/teacher/create").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(original)))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

		TeacherResponseDto created = objectMapper.readValue(createResponse, TeacherResponseDto.class);
		int teacherId = created.getTeacherId();

		TeacherRequestDto updated = new TeacherRequestDto("Updated", "teacher@email.com", "Password");
		mockMvc.perform(put("/teacher/update/{teacherId}", teacherId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updated))).andExpect(status().isOk())
				.andExpect(jsonPath("$.teacherName").value("Updated"))
				.andExpect(jsonPath("$.teacherEmail").value("teacher@email.com"));
	}
}

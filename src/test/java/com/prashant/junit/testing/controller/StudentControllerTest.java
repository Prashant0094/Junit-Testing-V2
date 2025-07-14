package com.prashant.junit.testing.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.prashant.junit.testing.controller.StudentController;
import com.prashant.junit.testing.dto.StudentDetailsDto;
import com.prashant.junit.testing.exception.ResourceNotFoundException;
import com.prashant.junit.testing.jwt.JwtService;
import com.prashant.junit.testing.model.StudentDetails;
import com.prashant.junit.testing.security.JwtAuthFilter;
import com.prashant.junit.testing.service.StudentService;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StudentService studentService;

	@MockBean
	private JwtAuthFilter jwtAuthFilter;

	@MockBean
	private JwtService jwtService;

	@Test
	void getStudentDetails_shouldGetStudent() throws Exception {
		int studentId = 1;
		StudentDetails entity = new StudentDetails();
		entity.setStudentName("Ghost");
		entity.setEnglishMarks(80);
		entity.setHindiMarks(90);
		entity.setMarathiMarks(100);
		StudentDetailsDto student = StudentDetailsDto.fromEntity(entity);
		when(studentService.getStudentDetails(studentId)).thenReturn(student);
		mockMvc.perform(get("/student/{studentId}", studentId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.['Student Name']").value("Ghost"))
				.andExpect(jsonPath("$.['English Marks']").value(80)).andExpect(jsonPath("$.['Hindi Marks']").value(90))
				.andExpect(jsonPath("$.['Marathi Marks']").value(100))
				.andExpect(jsonPath("$.['Percentage']").value(90));
	}

	@Test
	void getStudentDetails_shouldSendResourceNotFound_whenStudentIdDoesNotExist() throws Exception {
		int studentId = 5;
		String errorMessage = "The student with ID " + studentId + " does not exist!";
		when(studentService.getStudentDetails(studentId)).thenThrow(new ResourceNotFoundException(errorMessage));

		mockMvc.perform(get("/student/{studentId}", studentId)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value(errorMessage)).andExpect(jsonPath("$.status").value("NOT_FOUND"))
				.andExpect(jsonPath("$.timestamp").exists());
	}
}


package com.prashant.junit.testing.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prashant.junit.testing.dto.StudentDetailsDto;
import com.prashant.junit.testing.dto.TeacherRequestDto;
import com.prashant.junit.testing.dto.TeacherResponseDto;
import com.prashant.junit.testing.jwt.JwtService;
import com.prashant.junit.testing.model.StudentDetails;
import com.prashant.junit.testing.model.TeacherDetails;
import com.prashant.junit.testing.security.JwtAuthFilter;
import com.prashant.junit.testing.service.TeacherService;

@WebMvcTest(TeacherController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TeacherControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TeacherService teacherService;

	@MockBean
	private JwtAuthFilter jwtAuthFilter;

	@MockBean
	private JwtService jwtService;
	
	@MockBean
	private Pageable pageable;

	@Test
	void createTeacher_shouldSaveAndRespond_whenTeacherEmailIsUnique() throws Exception {
		int teacherId = 1;
		TeacherRequestDto teacher = new TeacherRequestDto("Teacher1", "teacher@email.com", "Password");
		TeacherDetails entity = new TeacherDetails();
		entity.setTeacherId(teacherId);
		entity.setTeacherName(teacher.getTeacherName());
		entity.setTeacherEmail(teacher.getTeacherEmail());
		entity.setPassword(teacher.getPassword());
		TeacherResponseDto dto = TeacherResponseDto.fromEntity(entity);

		when(teacherService.createTeacher(any())).thenReturn(dto);
		mockMvc.perform(post("/teacher/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(teacher)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.teacherName").value("Teacher1"))
				.andExpect(jsonPath("$.teacherEmail").value("teacher@email.com"));
	}

	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@Test
	void updateTeacher_shouldUpdateAndRespond_whenTeacherIdExist() throws Exception {
		int teacherId = 1;
		TeacherRequestDto dto = new TeacherRequestDto("Teacher1", "teacher@email.com", "Password");
		TeacherDetails teacher = dto.toEntity();
		teacher.setTeacherId(teacherId);
		TeacherResponseDto responseDto = TeacherResponseDto.fromEntity(teacher);

		when(teacherService.updateTeacher(eq(teacherId), any(TeacherRequestDto.class))).thenReturn(responseDto);

		mockMvc.perform(put("/teacher/update/{teacherId}", teacherId).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(dto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.teacherName").value("Teacher1"))
				.andExpect(jsonPath("$.teacherEmail").value("teacher@email.com"));
	}
	@Test
	void deleteTeacher_shouldDeleteTeacher_whenTeacherExist() throws Exception {
		int teacherId = 1;
		
		TeacherRequestDto requestDto = new TeacherRequestDto("A", "A@email.com", "pass");
		TeacherDetails teacher = requestDto.toEntity();
		teacher.setTeacherId(teacherId);
		when(teacherService.deleteTeacher(teacherId)).thenReturn("Teacher Deleted Successfully");

		mockMvc.perform(delete("/teacher/delete/{teacherId}", teacherId)).andExpect(status().isOk())
				.andExpect(content().string("Teacher Deleted Successfully"));
	}
	@Test
	void getTeachers_shouldReturnTeachersList_whenTeachersListNotNull() throws Exception {
		int teacherId = 1;
		
		TeacherRequestDto requestDto = new TeacherRequestDto("A","A@email.com","pass");
		TeacherDetails teacher = requestDto.toEntity();
		teacher.setTeacherId(teacherId);
		TeacherResponseDto responseDto = TeacherResponseDto.fromEntity(teacher);
	    Pageable pageable = PageRequest.of(0, 10);
	    Page<TeacherResponseDto> page = new PageImpl<>(List.of(responseDto), pageable, 1);
		when (teacherService.getTeachers(pageable)).thenReturn(page);
		
		mockMvc.perform(get("/teacher/all?page=0&size=10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].teacherName").value("A"))
				.andExpect(jsonPath("$.content[0].teacherEmail").value("A@email.com"));
	}
	@Test
	void createStudent_shouldSaveAndRespond_whenStudentCreated() throws Exception {
		int studentId = 1;
		StudentDetailsDto studentDto = new StudentDetailsDto("A",70,72,78);
		StudentDetails student = studentDto.toEntity();
		student.setStudentId(studentId);
		when(teacherService.createStudent(any(StudentDetailsDto.class))).thenReturn(studentDto);
		
		mockMvc.perform(post("/teacher/createStudent").contentType(MediaType.APPLICATION_JSON).content(asJsonString(studentDto)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$['Student Name']").value("A"))
				.andExpect(jsonPath("$['English Marks']").value(70))
				.andExpect(jsonPath("$['Hindi Marks']").value(72))
				.andExpect(jsonPath("$['Marathi Marks']").value(78));
	}
	@Test
	void updateStudent_shouldSaveAndReturnResponse_whenStudentIdPresent() throws Exception {
	int studentId = 1;
	StudentDetailsDto studentDto = new StudentDetailsDto("Ajay",90, 95, 85);
	StudentDetails student = studentDto.toEntity();
	student.setStudentId(studentId);
	when(teacherService.updateStudent(eq(studentId),any(StudentDetailsDto.class))).thenReturn(studentDto);
	
	mockMvc.perform(put("/teacher/updateStudent/{studentId}", studentId)
			.contentType(MediaType.APPLICATION_JSON).content(asJsonString(studentDto)))
			.andExpect(jsonPath("$['Student Name']").value("Ajay"))
			.andExpect(jsonPath("$['English Marks']").value(90));
	}
	@Test
	void deleteStudentshouldDeleteStudentWhenStudentExistById() throws Exception {
		int studentId = 1;
		StudentDetailsDto studentDto = new StudentDetailsDto("Ajay",67,67,67);
		StudentDetails student = studentDto.toEntity();
		student.setStudentId(studentId);
		when(teacherService.deleteStudent(eq(studentId))).thenReturn("Student Deleted Successfully");
		
		mockMvc.perform(delete("/teacher/deleteStudent/{studentId}",studentId))
				.andExpect(status().isOk())
				.andExpect(content().string("Student Deleted Successfully"));
	}
	@Test
	void getStudentsShouldGetAllStudentDetailsWhenNotNull() throws Exception {
		int studentId = 1;
		StudentDetailsDto studentDto = new StudentDetailsDto("Amy", 80, 90, 80);
		StudentDetails student = studentDto.toEntity();
		student.setStudentId(studentId);
		StudentDetailsDto response = StudentDetailsDto.fromEntity(student);
		Pageable pageable = PageRequest.of(0, 10);
		Page<StudentDetailsDto> page = new PageImpl<>(List.of(response), pageable, 1);
		when(teacherService.getStudents(pageable)).thenReturn(page);
		
		mockMvc.perform(get("/teacher/allStudents"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$content.[0]['Student Name']").value("Amy"))
				.andExpect(jsonPath("$content.[0]['English Marks']").value(80));				
	}
}
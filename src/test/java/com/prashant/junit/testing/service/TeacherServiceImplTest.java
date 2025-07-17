package com.prashant.junit.testing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.prashant.junit.testing.model.TeacherDetails;
import com.prashant.junit.testing.dto.StudentDetailsDto;
import com.prashant.junit.testing.dto.TeacherRequestDto;
import com.prashant.junit.testing.model.StudentDetails;
import com.prashant.junit.testing.repository.StudentRepo;
import com.prashant.junit.testing.repository.TeacherRepo;
import com.prashant.junit.testing.dto.TeacherResponseDto;
import com.prashant.junit.testing.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {

	@Mock
	private TeacherRepo teacherRepo;

	@InjectMocks
	private TeacherServiceImpl teacherServiceImpl;

	@Mock
	private StudentRepo studentRepo;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@Mock
	private Pageable pageable;

	@Test
	void createTeacher_shouldSaveAndReturnResponse_whenEmailNotExists() {
		TeacherRequestDto dto = new TeacherRequestDto("Amay", "Amay@email.com", "Password1");
		when(teacherRepo.existsByTeacherEmail("Amay@email.com")).thenReturn(false);
		when(passwordEncoder.encode("Password1")).thenReturn("encoded-Password");

		TeacherDetails savedEntity = dto.toEntity();
		savedEntity.setPassword("encoded-Password");
		savedEntity.setTeacherId(1);

		when(teacherRepo.save(any(TeacherDetails.class))).thenReturn(savedEntity);

		TeacherResponseDto result = teacherServiceImpl.createTeacher(dto);

		assertEquals("Amay", result.getTeacherName());
		assertEquals("Amay@email.com", result.getTeacherEmail());
	}

	@Test
	void createTeacher_shouldThrowException_whenEmailAlreadyExist() {
		TeacherRequestDto dto = new TeacherRequestDto("Amay", "Amay@email.com", "Password1");
		when(teacherRepo.existsByTeacherEmail("Amay@email.com")).thenReturn(true);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			teacherServiceImpl.createTeacher(dto);
		});

		assertEquals("Teacher with similar Email already Exists", exception.getMessage());
		verify(teacherRepo, never()).save(any());
		System.out.println("Teacher Existing Email test Successful");
	}

	@Test
	void updateTeacher_shouldThrowException_whenTeacherIdNotFound() {
		int teacherId = 43;
		TeacherRequestDto dto = new TeacherRequestDto("Amay", "Amay@email.com", "Password1");
		when(teacherRepo.existsById(teacherId)).thenReturn(false);

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			teacherServiceImpl.updateTeacher(teacherId, dto);
		});
		assertEquals("Teacher by this ID doesn't Exist", ex.getMessage());

		verify(teacherRepo, never()).save(any());
	}

	@ParameterizedTest
	@CsvSource({ "Amay,Amay@email.com,Password1", "Ghost,Ghost@email.com,Pass", "mi23,mi@email.com,Pass3" })
	void updateTeacher_shouldSaveAndReturnResponse_whenTeacherIdFound(String teacherName, String teacherEmail,
			String password) {
		int teacherId = 2;
		TeacherRequestDto dto = new TeacherRequestDto(teacherName, teacherEmail, password);
		when(teacherRepo.existsById(teacherId)).thenReturn(true);
		when(passwordEncoder.encode(anyString())).thenAnswer(inv -> "encoded-" + inv.getArgument(0));

		TeacherDetails saved = dto.toEntity();
		when(teacherRepo.save(any(TeacherDetails.class))).thenReturn(saved);

		TeacherResponseDto result = teacherServiceImpl.updateTeacher(teacherId, dto);
		ArgumentCaptor<TeacherDetails> captor = ArgumentCaptor.forClass(TeacherDetails.class);
		verify(teacherRepo).save(captor.capture());
		
		assertEquals("encoded-"+password, captor.getValue().getPassword());
		assertEquals(teacherName, result.getTeacherName());
		assertEquals(teacherEmail, result.getTeacherEmail());

		verify(teacherRepo).existsById(teacherId);
		verify(passwordEncoder).encode(password);
		verify(teacherRepo).save(any(TeacherDetails.class));
	}
	@Test
	void deleteTeacher_shouldDeleteTeacher_whenTeacherIdPresent() {
		int teacherId = 2;
		TeacherRequestDto dto = new TeacherRequestDto("Amay", "Amay@email.com", "Password1");

		TeacherDetails saved = dto.toEntity();
		saved.setTeacherId(teacherId);

		when(teacherRepo.existsById(teacherId)).thenReturn(true);

		String result = teacherServiceImpl.deleteTeacher(teacherId);
		verify(teacherRepo, times(1)).deleteById(teacherId);
		assertEquals("Teacher Deleted Successfully", result);
	}
	@Test
	void deleteTeacher_throwsException_whenTeacherIdNotFound() {
		int teacherId = 5;
		when(teacherRepo.existsById(teacherId)).thenReturn(false);

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			teacherServiceImpl.deleteTeacher(teacherId);
		});

		assertEquals("Teacher by this ID doesn't Exist", ex.getMessage());
		verify(teacherRepo, times(0)).deleteById(teacherId);
	}
	@Test
	void getTeachers_returnsAllTeachersDetails() {
		List<TeacherDetails> list = Arrays.asList(new TeacherDetails("Amay", "Amay@email.com", "Pass"),
				new TeacherDetails("Amy", "Amy@email.com", "Pass1"));
		 Pageable pageable = PageRequest.of(0, 10);
		    Page<TeacherDetails> page = new PageImpl<>(list, pageable, list.size());
		when(teacherRepo.findAll(pageable)).thenReturn(page);

		Page<TeacherResponseDto> result = teacherServiceImpl.getTeachers(pageable);

		assertNotNull(result);
		assertEquals("Amay", result.getContent().get(0).getTeacherName());
		assertEquals("Amy@email.com", result.getContent().get(1).getTeacherEmail());
		assertEquals(2, result.getTotalElements());
		verify(teacherRepo, times(1)).findAll(pageable);
	}
	@Test
	void createStudents_shouldSaveAndReturnResponse() {
		StudentDetailsDto dto = new StudentDetailsDto("Amay", 23, 23, 23);
		StudentDetails saved = dto.toEntity();
		saved.setStudentName(dto.getStudentName());
		saved.setEnglishMarks(dto.getEnglishMarks());
		saved.setHindiMarks(dto.getHindiMarks());
		saved.setMarathiMarks(dto.getMarathiMarks());

		when(studentRepo.save(any(StudentDetails.class))).thenReturn(saved);

		StudentDetailsDto result = teacherServiceImpl.createStudent(dto);
		assertNotNull(result);
		assertEquals("Amay", saved.getStudentName());
		verify(studentRepo, times(1)).save(any(StudentDetails.class));
	}

	@ParameterizedTest
	@CsvSource({ "Amy,34,45,56", "Ghost,76,98,99", "New,45,87,76" })
	void updateStudents_shouldSaveAndReturnResponse_whenStudentIdPresent(String name, int eng, int hin, int mar) {

		int studentId = 1;

		StudentDetailsDto dto = new StudentDetailsDto(name, eng, hin, mar);

		when(studentRepo.existsById(studentId)).thenReturn(true);

		StudentDetails saved = dto.toEntity();
		when(studentRepo.save(any(StudentDetails.class))).thenReturn(saved);

		StudentDetailsDto result = teacherServiceImpl.updateStudent(studentId, dto);

		assertEquals(name, result.getStudentName());
		assertEquals(eng, result.getEnglishMarks());
		assertEquals(mar, result.getMarathiMarks());
		assertEquals(hin, result.getHindiMarks());

		verify(studentRepo, times(1)).save(any(StudentDetails.class));
	}
}
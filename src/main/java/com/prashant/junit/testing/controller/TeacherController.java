package com.prashant.junit.testing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prashant.junit.testing.dto.StudentDetailsDto;
import com.prashant.junit.testing.dto.TeacherRequestDto;
import com.prashant.junit.testing.dto.TeacherResponseDto;
import com.prashant.junit.testing.service.TeacherService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;

	@PostMapping("/create")
	public ResponseEntity<TeacherResponseDto> createTeacher(@Valid @RequestBody TeacherRequestDto dto) {
		TeacherResponseDto created = teacherService.createTeacher(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/update/{teacherId}")
	public ResponseEntity<TeacherResponseDto> updateTeacher(@PathVariable Integer teacherId,@Valid @RequestBody TeacherRequestDto dto) {
		TeacherResponseDto updated = teacherService.updateTeacher(teacherId, dto);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/delete/{teacherId}")
	public ResponseEntity<String> deleteTeacher(@PathVariable Integer teacherId) {
		String deleted = teacherService.deleteTeacher(teacherId);
		return ResponseEntity.ok(deleted);
	}

	@GetMapping("/paginated")
	public ResponseEntity<Page<TeacherResponseDto>> getPaginatedTeachers(Pageable pageable) {
		Page<TeacherResponseDto> response = teacherService.getTeachers(pageable);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/createStudent")
	public ResponseEntity<StudentDetailsDto> createStudent(@Valid @RequestBody StudentDetailsDto dto) {
		StudentDetailsDto details = teacherService.createStudent(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(details);
	}

	@PutMapping("/updateStudent/{studentId}")
	public ResponseEntity<StudentDetailsDto> updateStudent(@PathVariable Integer studentId,@Valid @RequestBody StudentDetailsDto dto) {
		StudentDetailsDto details = teacherService.updateStudent(studentId, dto);
		return ResponseEntity.ok(details);
	}

	@DeleteMapping("/deleteStudent/{studentId}")
	public ResponseEntity<String> deleteStudent(@PathVariable Integer studentId) {
		String delete = teacherService.deleteStudent(studentId);
		return ResponseEntity.ok(delete);
	}

	@GetMapping("/allStudents")
	public ResponseEntity<Page<StudentDetailsDto>> getStudent(Pageable pageable) {
		Page<StudentDetailsDto> response = teacherService.getStudents(pageable);
		return ResponseEntity.ok(response);
	}
}

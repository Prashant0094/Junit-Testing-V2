package com.prashant.junit.testing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prashant.junit.testing.dto.StudentDetailsDto;
import com.prashant.junit.testing.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@GetMapping("/{studentId}")
	public ResponseEntity<StudentDetailsDto> getStudentDetails(@PathVariable Integer studentId) {
		StudentDetailsDto students = studentService.getStudentDetails(studentId);
		return ResponseEntity.ok(students);
	}
}

package com.prashant.junit.testing.service;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.prashant.junit.testing.dto.StudentDetailsDto;
import com.prashant.junit.testing.dto.TeacherRequestDto;
import com.prashant.junit.testing.dto.TeacherResponseDto;
import com.prashant.junit.testing.exception.ResourceNotFoundException;
import com.prashant.junit.testing.model.StudentDetails;
import com.prashant.junit.testing.model.TeacherDetails;
import com.prashant.junit.testing.repository.StudentRepo;
import com.prashant.junit.testing.repository.TeacherRepo;

@Service
public class TeacherServiceImpl implements TeacherService {

	@Autowired
	private TeacherRepo teacherRepo;
	@Autowired
	private StudentRepo studentRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);

	@Override
	public TeacherResponseDto createTeacher(TeacherRequestDto dto) {
		if (teacherRepo.existsByTeacherEmail(dto.getTeacherEmail())) {
			log.error("Teacher with similar email {} already exist", dto.getTeacherEmail());
			throw new IllegalArgumentException("Teacher with similar Email already Exists");
		}
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		TeacherDetails saved = teacherRepo.save(dto.toEntity());
		log.info("Teacher created: {}", saved.getTeacherEmail());
		return TeacherResponseDto.fromEntity(saved);
	}

	@Override
	public TeacherResponseDto updateTeacher(Integer teacherId, TeacherRequestDto dto) {
		log.debug("Attempting to update the Teacher with Id: {}", teacherId);
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			log.warn("Password is being updated for Teacher: {}", teacherId);
			dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		if (!teacherRepo.existsById(teacherId)) {
			log.error("Update Failed. Teacher with ID {} not found", teacherId);
			throw new ResourceNotFoundException("Teacher by this ID doesn't Exist");
		}
		TeacherDetails updated = dto.toEntity();
		updated.setTeacherId(teacherId);
		TeacherDetails saved = teacherRepo.save(updated);
		log.info("The teacher details is updated: {}", saved.getTeacherEmail());
		return TeacherResponseDto.fromEntity(saved);
	}

	@Override
	public String deleteTeacher(Integer teacherId) {
		if (!teacherRepo.existsById(teacherId)) {
			log.error("Deletion failed. Teacher with ID {} doesnt exist", teacherId);
			throw new ResourceNotFoundException("Teacher by this ID doesn't Exist");
		}
		teacherRepo.deleteById(teacherId);
		log.info("The teacher with Id {} has been deleted.", teacherId);
		return "Teacher Deleted Successfully";
	}

	@Override
	public Page<TeacherResponseDto> getTeachers(Pageable pageable) {
		Page<TeacherDetails> page = teacherRepo.findAll(pageable);
		if (page.isEmpty()) {
			log.error("No Teacher found");
			throw new ResourceNotFoundException("No teacher found in database !");
		}
		log.info("Listing all available teachers");
		return page.map(TeacherResponseDto::fromEntity);
	}

	@Override
	public StudentDetailsDto createStudent(StudentDetailsDto dto) {
		StudentDetails saved = studentRepo.save(dto.toEntity());
		log.info("Student saved Successfully {}", saved.getStudentName());
		return StudentDetailsDto.fromEntity(saved);
	}

	@Override
	public StudentDetailsDto updateStudent(Integer studentId, StudentDetailsDto dto) {
		log.warn("Updating Student Details");
		if (!studentRepo.existsById(studentId)) {
			log.error("Student with Id {} not found", studentId);
			throw new ResourceNotFoundException("The Student with Id " + studentId + " doesn't Exist");
		}
		StudentDetails updated = dto.toEntity();
		updated.setStudentId(studentId);
		StudentDetails saved = studentRepo.save(updated);
		log.info("Student details updated for ID: {}", studentId);
		return StudentDetailsDto.fromEntity(saved);
	}

	@Override
	public String deleteStudent(Integer studentId) {
		if (!studentRepo.existsById(studentId)) {
			log.error("Student with ID {} not found", studentId);
			throw new ResourceNotFoundException("The Student with Id " + studentId + " doesn't Exist");
		}
		studentRepo.deleteById(studentId);
		log.info("Student with ID {} deleted successfully", studentId);
		return "Student Deleted Successfully";
	}

	@Override
	public Page<StudentDetailsDto> getStudents(Pageable pageable) {
		Page<StudentDetails> page = studentRepo.findAll(pageable);
		if (page.isEmpty()) {
			log.error("No student in database");
			throw new ResourceNotFoundException("No Student Found !");
		}
		log.info("Retrieving detail of students");
		return page.map(StudentDetailsDto::fromEntity);
	}

}

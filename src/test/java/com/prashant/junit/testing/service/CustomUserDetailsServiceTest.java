package com.prashant.junit.testing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.prashant.junit.testing.dto.TeacherRequestDto;
import com.prashant.junit.testing.dto.TeacherResponseDto;
import com.prashant.junit.testing.model.TeacherDetails;
import com.prashant.junit.testing.repository.TeacherRepo;
import com.prashant.junit.testing.security.CustomUserDetailsService;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
	
	@InjectMocks
	private CustomUserDetailsService service1;
	
	@Mock
	private TeacherRepo teacherRepo;
	
	@Mock
	private UserDetails userDetails;
	
	@Test
	void loadUserByUsernameShouldLoadUserWhenEmailExist() {
		int teacherId = 1;
		TeacherRequestDto teacherDto = new TeacherRequestDto("Amy","Amy@email.com","Pass");
		TeacherDetails teacher = teacherDto.toEntity();
		teacher.setTeacherId(teacherId);
		
		when(teacherRepo.findByTeacherEmail("Amy@email.com")).thenReturn(Optional.of(teacher));
		UserDetails response = service1.loadUserByUsername("Amy@email.com");
		assertEquals("Amy@email.com", response.getUsername());
		
	}
}

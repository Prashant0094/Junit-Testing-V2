package com.prashant.junit.testing.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prashant.junit.testing.model.TeacherDetails;
import com.prashant.junit.testing.repository.TeacherRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private TeacherRepo teacherRepo;
	
	private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		TeacherDetails teacher = teacherRepo.findByTeacherEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User with this Email was not Found"));
		log.info("Loading user by email {}: " ,email);
		return new CustomUserDetails(teacher);
	}
}

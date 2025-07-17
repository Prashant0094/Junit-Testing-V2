package com.prashant.junit.testing.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.prashant.junit.testing.model.TeacherDetails;

import java.util.Collections;


public class CustomUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final TeacherDetails teacher;

	public CustomUserDetails(TeacherDetails teacher) {
		this.teacher = teacher;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return teacher.getPassword();
	}

	@Override
	public String getUsername() {
		return teacher.getTeacherEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public TeacherDetails getTeacher() {
		return teacher;
	}
}

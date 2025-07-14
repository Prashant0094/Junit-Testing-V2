package com.prashant.junit.testing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TeacherDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int teacherId;
	private String teacherName;
	@Column(unique = true, nullable = false)
	private String teacherEmail;
	private String password;
	
	public TeacherDetails() {}

	public TeacherDetails(String teacherName, String teacherEmail, String password) {
		super();
		this.teacherName = teacherName;
		this.teacherEmail = teacherEmail;
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTeacherEmail() {
		return teacherEmail;
	}

	public void setTeacherEmail(String teacherEmail) {
		this.teacherEmail = teacherEmail;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
	
	
}

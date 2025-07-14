package com.prashant.junit.testing.dto;

import com.prashant.junit.testing.model.TeacherDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class TeacherRequestDto {
	
	@NotBlank(message = "Name cannot be Blank")
	private String teacherName;
	
	@Email (message = "Enter Valid Email")
	@NotBlank (message = "Email cannot be Blank")
	private String teacherEmail;
	private String password;
	
	public TeacherRequestDto() {}

	public TeacherRequestDto(String teacherName, String teacherEmail, String password) {
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

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherEmail() {
		return teacherEmail;
	}

	public void setTeacherEmail(String teacherEmail) {
		this.teacherEmail = teacherEmail;
	}
	
	public TeacherDetails toEntity() {
		TeacherDetails entity = new TeacherDetails();
		entity.setTeacherName(this.teacherName);
		entity.setTeacherEmail(this.teacherEmail);
		entity.setPassword(this.password);
		return entity;
	}
	
	public static TeacherRequestDto fromEntity(TeacherDetails entity) {
		return new TeacherRequestDto(
		entity.getTeacherName(),
		entity.getTeacherEmail(),
		entity.getPassword());
	}
}


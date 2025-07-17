package com.prashant.junit.testing.dto;

import com.prashant.junit.testing.model.TeacherDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class TeacherResponseDto {
		
		private int teacherId; 
		@NotBlank(message = "Name cannot be Blank")
		private String teacherName;
		
		@Email (message = "Enter Valid Email")
		@NotBlank (message = "Email cannot be Blank")
		private String teacherEmail;
		
		public TeacherResponseDto() {}

		public TeacherResponseDto(int teacherId, String teacherName, String teacherEmail) {
			super();
			this.teacherId = teacherId;
			this.teacherName = teacherName;
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
			return entity;
		}
		
		public static TeacherResponseDto fromEntity(TeacherDetails entity) {
			return new TeacherResponseDto(
			entity.getTeacherId(),
			entity.getTeacherName(),
			entity.getTeacherEmail());
		}
	}


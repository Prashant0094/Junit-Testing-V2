package com.prashant.junit.testing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prashant.junit.testing.model.StudentDetails;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StudentDetailsDto {

	@NotBlank(message = "Name cannot be Blank")
	@JsonProperty("Student Name")
	private String studentName;
	@Min(0)
	@Max(100)
	@JsonProperty("English Marks")
	private int englishMarks;
	@Min(0)
	@Max(100)
	@JsonProperty("Hindi Marks")
	private int hindiMarks;
	@Min(0)
	@Max(100)
	@JsonProperty("Marathi Marks")
	private int marathiMarks;
	@JsonProperty("Percentage")
	private double percentage;

	public StudentDetailsDto() {
	}
	
	public StudentDetailsDto(String studentName, int englishMarks, int hindiMarks, int marathiMarks) {
		super();
		this.studentName = studentName;
		this.englishMarks = englishMarks;
		this.hindiMarks = hindiMarks;
		this.marathiMarks = marathiMarks;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getEnglishMarks() {
		return englishMarks;
	}

	public void setEnglishMarks(int englishMarks) {
		this.englishMarks = englishMarks;
	}

	public int getHindiMarks() {
		return hindiMarks;
	}

	public void setHindiMarks(int hindiMarks) {
		this.hindiMarks = hindiMarks;
	}

	public int getMarathiMarks() {
		return marathiMarks;
	}

	public void setMarathiMarks(int marathiMarks) {
		this.marathiMarks = marathiMarks;
	}
	
	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public StudentDetails toEntity() {
		StudentDetails entity = new StudentDetails();
		entity.setStudentName(this.studentName);
		entity.setEnglishMarks(this.englishMarks);
		entity.setHindiMarks(this.hindiMarks);
		entity.setMarathiMarks(this.marathiMarks);
		return entity;

	}

	public static StudentDetailsDto fromEntity(StudentDetails entity) {
	  StudentDetailsDto dto = new	StudentDetailsDto(entity.getStudentName(), entity.getEnglishMarks(), entity.getHindiMarks(),
				entity.getMarathiMarks());
	double totalMarks = entity.getEnglishMarks() + entity.getHindiMarks() + entity.getMarathiMarks() ;
	double rawPercentage = (totalMarks/3);
	double roundedPercentage = Math.round(rawPercentage*100.0)/100.0;
	dto.setPercentage(roundedPercentage);
	return dto;
	}

	@Override
	public String toString() {
		return "StudentDetailsDto [studentName=" + studentName + ", englishMarks=" + englishMarks + ", hindiMarks="
				+ hindiMarks + ", marathiMarks=" + marathiMarks + "]";
	}
	
}

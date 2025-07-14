package com.prashant.junit.testing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class StudentDetails {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int studentId;
	private String studentName;
	private int englishMarks;
	private int hindiMarks;
	private int marathiMarks;
	
	public StudentDetails(){}

	public StudentDetails(String studentName, int englishMarks, int hindiMarks, int marathiMarks) {
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

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	
}

package com.prashant.junit.testing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prashant.junit.testing.dto.StudentDetailsDto;
import com.prashant.junit.testing.dto.TeacherResponseDto;
import com.prashant.junit.testing.service.ReportService;
import com.prashant.junit.testing.service.TeacherService;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final TeacherService teacherService;

    @Autowired
    public ReportController(ReportService reportService, TeacherService teacherService) {
        this.reportService = reportService;
        this.teacherService = teacherService;
    }
/*
    @GetMapping(value = "/students", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getStudentReport() throws Exception {
        // Get all students from TeacherService with a big enough page size
        Page<StudentDetailsDto> studentPage = teacherService.getStudents(PageRequest.of(0, 100));
        List<StudentDetailsDto> students = studentPage.getContent();

        byte[] pdf = reportService.generateStudentReportPdf(students);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("students.pdf").build());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }
    @GetMapping(value = "/teachers", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getTeacherReport() throws Exception {
        Page<TeacherResponseDto> teacherPage = teacherService.getTeachers(PageRequest.of(0, 100));
        List<TeacherResponseDto> teachers = teacherPage.getContent();

        byte[] pdf = reportService.generateTeacherReportPdf(teachers);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("teachers.pdf").build());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }*/

@GetMapping("/combined")
public ResponseEntity<byte[]> getCombinedReport() throws Exception {
	
    List<StudentDetailsDto> students = reportService.fetchAllPages(
        pageable -> teacherService.getStudents(pageable),
        100
    );

    List<TeacherResponseDto> teachers = reportService.fetchAllPages(
        pageable -> teacherService.getTeachers(pageable),
        100
    );
    System.out.println("Students: " + students.size());
    System.out.println("Teachers: " + teachers.size());


    byte[] studentPdf = reportService.generateStudentReportPdf(students);
    byte[] teacherPdf = reportService.generateTeacherReportPdf(teachers);
    byte[] mergedPdf = reportService.mergePdfs(studentPdf, teacherPdf);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.attachment().filename("combined_report.pdf").build());

    return ResponseEntity.ok().headers(headers).body(mergedPdf);
}

}


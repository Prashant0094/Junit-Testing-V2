package com.prashant.junit.testing.controller;

import com.prashant.junit.testing.dto.StudentDetailsDto;
import com.prashant.junit.testing.service.ReportService;
import com.prashant.junit.testing.service.TeacherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/students", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getStudentReport() throws Exception {
        // Get all students from TeacherService with a big enough page size
        Page<StudentDetailsDto> studentPage = teacherService.getStudents(PageRequest.of(0, 100));
        List<StudentDetailsDto> students = studentPage.getContent();

        byte[] pdf = reportService.exportStudentsToPdf(students);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("students.pdf").build());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }
}


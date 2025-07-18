package com.prashant.junit.testing.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.prashant.junit.testing.dto.StudentDetailsDto;
import com.prashant.junit.testing.dto.TeacherResponseDto;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {

	public byte[] generateStudentReportPdf(List<StudentDetailsDto> students) throws JRException {
		// Load and compile JRXML
		InputStream reportStream = getClass().getResourceAsStream("/reports/StudentDemo.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
		// Wrap data
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(students);
		// Fill report
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), // no parameters
				dataSource);
		if (students == null || students.isEmpty()) {
			throw new IllegalStateException("No student data available for report");
		}

		// Export to PDF in byte array
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	public byte[] generateTeacherReportPdf(List<TeacherResponseDto> teachers) throws JRException {
		InputStream reportStream = getClass().getResourceAsStream("/reports/TeacherDemo.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
		if (teachers == null || teachers.isEmpty()) {
			throw new IllegalStateException("No student data available for report");
		}

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(teachers);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	public byte[] mergePdfs(byte[]... pdfs) throws IOException {
	    List<PDDocument> sourceDocs = new ArrayList<>();
	    try (PDDocument merged = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	        for (byte[] pdf : pdfs) {
	            // Load the source doc but DON'T close it yet
	            PDDocument doc = Loader.loadPDF(pdf);
	            sourceDocs.add(doc);
	            for (int i = 0; i < doc.getNumberOfPages(); i++) {
	                merged.addPage(doc.getPage(i));
	            }
	        }
	        // Now save the merged doc while source docs are still open
	        merged.save(outputStream);
	        return outputStream.toByteArray();

	    } finally {
	        // IMPORTANT: Manually close all the source documents
	        for (PDDocument doc : sourceDocs) {
	            doc.close();
	        }
	    }
	}
	public <T> List<T> fetchAllPages(Function<Pageable, Page<T>> fetcher, int pageSize) {
		List<T> result = new ArrayList<>();
		int page = 0;
		Page<T> current;

		do {
			current = fetcher.apply(PageRequest.of(page, pageSize));
			result.addAll(current.getContent());
			page++;
		} while (!current.isLast());

		return result;
	}

}

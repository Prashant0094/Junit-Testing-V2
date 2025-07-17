package com.prashant.junit.testing.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import com.prashant.junit.testing.dto.StudentDetailsDto;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportService {

    public byte[] exportStudentsToPdf(List<StudentDetailsDto> students) throws JRException {
        // Load and compile JRXML
        InputStream reportStream = getClass().getResourceAsStream("/reports/StudentDemo.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Wrap data
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(students);

        // Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                new HashMap<>(), // no parameters
                dataSource
        );

        // Export to PDF in byte array
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}


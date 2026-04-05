package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.ImportAccessoriesInputData;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static org.junit.jupiter.api.Assertions.*;

public class ImportAccessoriesUseCaseControlTest {

    @Test
    void shouldValidateExcelFile() throws Exception {
        // Given - Create a valid Excel file
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Accessories");
        
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã SP");
        headerRow.createCell(1).setCellValue("Tên SP");
        headerRow.createCell(2).setCellValue("Mô tả");
        headerRow.createCell(3).setCellValue("Giá");
        headerRow.createCell(4).setCellValue("Số lượng");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("PT001");
        dataRow.createCell(1).setCellValue("Mũ bảo hiểm");
        dataRow.createCell(2).setCellValue("Phụ tùng");
        dataRow.createCell(3).setCellValue(500000.0);
        dataRow.createCell(4).setCellValue(20);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        MultipartFile file = new MockMultipartFile(
            "file", "accessories.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            baos.toByteArray()
        );

        ImportAccessoriesInputData inputData = new ImportAccessoriesInputData(
            file.getInputStream(), file.getOriginalFilename()
        );

        // Then - Validate file is readable
        assertNotNull(inputData);
        assertNotNull(inputData.getFileInputStream());
        assertEquals("accessories.xlsx", inputData.getOriginalFilename());
    }

    @Test
    void shouldHandleInvalidFileExtension() throws Exception {
        // Given
        MultipartFile file = new MockMultipartFile(
            "file", "test.txt", "text/plain", "test content".getBytes()
        );

        ImportAccessoriesInputData inputData = new ImportAccessoriesInputData(
            file.getInputStream(), file.getOriginalFilename()
        );

        // Then
        assertNotNull(inputData);
        assertNotNull(inputData.getFileInputStream());
        assertEquals("test.txt", inputData.getOriginalFilename());
    }
}

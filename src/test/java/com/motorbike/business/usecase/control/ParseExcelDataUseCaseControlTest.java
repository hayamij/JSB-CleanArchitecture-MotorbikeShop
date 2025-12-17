package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.parseexceldata.ParseExcelDataInputData;
import com.motorbike.business.dto.parseexceldata.ParseExcelDataOutputData;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static org.junit.jupiter.api.Assertions.*;

public class ParseExcelDataUseCaseControlTest {

    @Test
    void shouldParseExcelDataSuccessfully() throws Exception {
        // Given - Create a valid Excel file
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");
        
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã SP");
        headerRow.createCell(1).setCellValue("Tên SP");
        headerRow.createCell(2).setCellValue("Mô tả");
        headerRow.createCell(3).setCellValue("Giá");
        headerRow.createCell(4).setCellValue("Số lượng");
        
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("XE001");
        dataRow.createCell(1).setCellValue("Yamaha Exciter");
        dataRow.createCell(2).setCellValue("Xe thể thao");
        dataRow.createCell(3).setCellValue(45000000.0);
        dataRow.createCell(4).setCellValue(10);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        MultipartFile file = new MockMultipartFile(
            "file", "products.xlsx", 
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
            baos.toByteArray()
        );

        ParseExcelDataInputData inputData = new ParseExcelDataInputData(file);
        ParseExcelDataUseCaseControl useCase = new ParseExcelDataUseCaseControl(null);

        // When
        ParseExcelDataOutputData outputData = useCase.parseInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotNull(outputData.getRows());
        assertFalse(outputData.getRows().isEmpty());
        assertEquals(2, outputData.getRows().size()); // header + 1 data row
    }

    @Test
    void shouldHandleNullFile() {
        // Given
        MultipartFile nullFile = null;
        ParseExcelDataInputData inputData = new ParseExcelDataInputData(nullFile);
        ParseExcelDataUseCaseControl useCase = new ParseExcelDataUseCaseControl(null);

        // When
        ParseExcelDataOutputData outputData = useCase.parseInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
        assertNotNull(outputData.getErrorCode());
    }
}

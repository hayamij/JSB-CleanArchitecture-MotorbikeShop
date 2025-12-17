package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.parseexceldata.ParseExcelDataInputData;
import com.motorbike.business.dto.parseexceldata.ParseExcelDataOutputData;
import com.motorbike.business.usecase.output.ParseExcelDataOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParseExcelDataUseCaseControlTest {

    @Mock
    private ParseExcelDataOutputBoundary outputBoundary;

    private ParseExcelDataUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ParseExcelDataUseCaseControl(outputBoundary);
    }

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

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ParseExcelDataOutputData.class));
    }
}

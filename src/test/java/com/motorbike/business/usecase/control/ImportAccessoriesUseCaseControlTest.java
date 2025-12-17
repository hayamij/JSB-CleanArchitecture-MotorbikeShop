package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.ImportAccessoriesInputData;
import com.motorbike.business.usecase.input.ImportAccessoriesInputBoundary;
import com.motorbike.business.usecase.output.ImportAccessoriesOutputBoundary;
import com.motorbike.business.usecase.input.ValidateExcelFileInputBoundary;
import com.motorbike.business.usecase.input.ParseExcelDataInputBoundary;
import com.motorbike.business.usecase.input.ValidateImportRowInputBoundary;
import com.motorbike.business.usecase.input.GenerateImportReportInputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImportAccessoriesUseCaseControlTest {

    @Mock
    private ValidateExcelFileInputBoundary validateExcelFileUseCase;

    @Mock
    private ParseExcelDataInputBoundary parseExcelDataUseCase;

    @Mock
    private ValidateImportRowInputBoundary validateImportRowUseCase;

    @Mock
    private GenerateImportReportInputBoundary generateImportReportUseCase;

    @Mock
    private ImportAccessoriesOutputBoundary outputBoundary;

    private ImportAccessoriesUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ImportAccessoriesUseCaseControl(
            validateExcelFileUseCase,
            parseExcelDataUseCase,
            validateImportRowUseCase,
            generateImportReportUseCase,
            outputBoundary
        );
    }

    @Test
    void shouldImportAccessoriesSuccessfully() throws Exception {
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

        ImportAccessoriesInputData inputData = new ImportAccessoriesInputData(file.getInputStream(), file.getOriginalFilename());

        // When
        useCase.execute(inputData);

        // Then
        verify(validateExcelFileUseCase, atLeastOnce()).execute(any());
    }
}

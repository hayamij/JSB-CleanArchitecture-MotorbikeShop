package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.generateexcelfile.GenerateExcelFileInputData;
import com.motorbike.business.dto.generateexcelfile.GenerateExcelFileOutputData;
import com.motorbike.business.usecase.input.GenerateExcelFileInputBoundary;
import com.motorbike.business.usecase.output.GenerateExcelFileOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class GenerateExcelFileUseCaseControl implements GenerateExcelFileInputBoundary {
    private final GenerateExcelFileOutputBoundary outputBoundary;

    public GenerateExcelFileUseCaseControl(GenerateExcelFileOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(GenerateExcelFileInputData inputData) {
        GenerateExcelFileOutputData outputData = generateInternal(inputData);
        outputBoundary.present(outputData);
    }

    public GenerateExcelFileOutputData generateInternal(GenerateExcelFileInputData inputData) {
        GenerateExcelFileOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("GenerateExcelFile");
            }
            if (inputData.getData() == null) {
                throw ValidationException.invalidInput("Data is required");
            }
            if (inputData.getHeaders() == null || inputData.getHeaders().isEmpty()) {
                throw ValidationException.invalidInput("Headers are required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - generate Excel file
        if (errorException == null) {
            try {
                List<Map<String, Object>> data = inputData.getData();
                List<String> headers = inputData.getHeaders();
                String sheetName = inputData.getSheetName() != null ? inputData.getSheetName() : "Sheet1";

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet(sheetName);

                // Create header row style
                CellStyle headerStyle = createHeaderStyle(workbook);

                // Create header row
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers.get(i));
                    cell.setCellStyle(headerStyle);
                }

                // Create data rows
                int rowNum = 1;
                for (Map<String, Object> rowData : data) {
                    Row row = sheet.createRow(rowNum++);
                    
                    for (int i = 0; i < headers.size(); i++) {
                        String header = headers.get(i);
                        Object value = rowData.get(header);
                        
                        Cell cell = row.createCell(i);
                        setCellValue(cell, value);
                    }
                }

                // Auto-size columns
                for (int i = 0; i < headers.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                // Convert workbook to byte array
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);
                workbook.close();

                byte[] fileBytes = outputStream.toByteArray();
                String fileName = generateFileName(sheetName);
                int rowCount = data.size();

                outputData = new GenerateExcelFileOutputData(fileBytes, fileName, rowCount);

            } catch (IOException e) {
                errorException = new RuntimeException("Failed to generate Excel file: " + e.getMessage(), e);
            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "EXCEL_GENERATION_ERROR";
            outputData = GenerateExcelFileOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            cell.setCellValue(((LocalDateTime) value).format(formatter));
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private String generateFileName(String sheetName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String cleanSheetName = sheetName.replaceAll("[^a-zA-Z0-9]", "_");
        return cleanSheetName + "_" + timestamp + ".xlsx";
    }
}

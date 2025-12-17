package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.parseexceldata.ParseExcelDataInputData;
import com.motorbike.business.dto.parseexceldata.ParseExcelDataOutputData;
import com.motorbike.business.usecase.input.ParseExcelDataInputBoundary;
import com.motorbike.business.usecase.output.ParseExcelDataOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ParseExcelDataUseCaseControl implements ParseExcelDataInputBoundary {
    private final ParseExcelDataOutputBoundary outputBoundary;

    public ParseExcelDataUseCaseControl(ParseExcelDataOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ParseExcelDataInputData inputData) {
        ParseExcelDataOutputData outputData = parseInternal(inputData);
        outputBoundary.present(outputData);
    }

    public ParseExcelDataOutputData parseInternal(ParseExcelDataInputData inputData) {
        ParseExcelDataOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("ParseExcelData");
            }
            if (inputData.getFile() == null) {
                throw ValidationException.invalidInput("File is required");
            }
            if (inputData.getSheetIndex() < 0) {
                throw ValidationException.invalidInput("Sheet index must be non-negative");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - parse Excel data
        if (errorException == null) {
            try {
                MultipartFile file = inputData.getFile();
                int sheetIndex = inputData.getSheetIndex();
                
                Workbook workbook = WorkbookFactory.create(file.getInputStream());
                
                if (sheetIndex >= workbook.getNumberOfSheets()) {
                    throw new RuntimeException("Sheet index " + sheetIndex + " not found in workbook");
                }
                
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                List<Map<String, String>> rows = new ArrayList<>();
                
                // Get header row (first row)
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    workbook.close();
                    throw new RuntimeException("Header row not found");
                }
                
                List<String> headers = new ArrayList<>();
                for (Cell cell : headerRow) {
                    headers.add(getCellValueAsString(cell).trim());
                }
                
                // Parse data rows (skip header)
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue; // Skip empty rows
                    }
                    
                    // Check if row is completely empty
                    if (isRowEmpty(row)) {
                        continue;
                    }
                    
                    Map<String, String> rowData = new LinkedHashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        String header = headers.get(j);
                        Cell cell = row.getCell(j);
                        String value = getCellValueAsString(cell);
                        rowData.put(header, value);
                    }
                    
                    rows.add(rowData);
                }
                
                workbook.close();
                
                outputData = new ParseExcelDataOutputData(rows);
                
            } catch (IOException e) {
                errorException = new RuntimeException("Failed to read Excel file: " + e.getMessage(), e);
            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "PARSE_ERROR";
            outputData = ParseExcelDataOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(date);
                } else {
                    // Format numeric value without scientific notation
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    return cell.getStringCellValue();
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private boolean isRowEmpty(Row row) {
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}

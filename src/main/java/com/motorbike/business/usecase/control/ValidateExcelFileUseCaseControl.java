package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateexcelfile.ValidateExcelFileInputData;
import com.motorbike.business.dto.validateexcelfile.ValidateExcelFileOutputData;
import com.motorbike.business.usecase.input.ValidateExcelFileInputBoundary;
import com.motorbike.business.usecase.output.ValidateExcelFileOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValidateExcelFileUseCaseControl implements ValidateExcelFileInputBoundary {
    private final ValidateExcelFileOutputBoundary outputBoundary;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    public ValidateExcelFileUseCaseControl(ValidateExcelFileOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ValidateExcelFileInputData inputData) {
        ValidateExcelFileOutputData outputData = validateInternal(inputData);
        outputBoundary.present(outputData);
    }

    public ValidateExcelFileOutputData validateInternal(ValidateExcelFileInputData inputData) {
        ValidateExcelFileOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("ValidateExcelFile");
            }
            if (inputData.getFile() == null) {
                throw ValidationException.invalidInput("File is required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - validate Excel file
        if (errorException == null) {
            try {
                MultipartFile file = inputData.getFile();
                List<String> expectedColumns = inputData.getExpectedColumns();
                List<String> errors = new ArrayList<>();

                // Validate file is not empty
                if (file.isEmpty()) {
                    errors.add("File không được để trống");
                    outputData = new ValidateExcelFileOutputData(false, errors);
                    return outputData;
                }

                // Validate file size
                if (file.getSize() > MAX_FILE_SIZE) {
                    errors.add("Kích thước file vượt quá giới hạn cho phép (10MB)");
                }

                // Validate file type
                String contentType = file.getContentType();
                if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
                    errors.add("File phải có định dạng Excel (.xls hoặc .xlsx)");
                }

                // Validate file extension
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || 
                    (!originalFilename.toLowerCase().endsWith(".xls") && 
                     !originalFilename.toLowerCase().endsWith(".xlsx"))) {
                    errors.add("Tên file phải có đuôi .xls hoặc .xlsx");
                }

                // Try to read the Excel file to verify it's valid
                if (errors.isEmpty()) {
                    try {
                        Workbook workbook = WorkbookFactory.create(file.getInputStream());
                        
                        // Validate workbook has at least one sheet
                        if (workbook.getNumberOfSheets() == 0) {
                            errors.add("File Excel không chứa sheet nào");
                        } else {
                            Sheet sheet = workbook.getSheetAt(0);
                            
                            // Validate sheet has at least header row
                            if (sheet.getPhysicalNumberOfRows() == 0) {
                                errors.add("Sheet Excel không chứa dữ liệu");
                            } else {
                                // Validate expected columns if provided
                                if (expectedColumns != null && !expectedColumns.isEmpty()) {
                                    Row headerRow = sheet.getRow(0);
                                    if (headerRow == null) {
                                        errors.add("Sheet Excel không có dòng tiêu đề");
                                    } else {
                                        List<String> actualColumns = new ArrayList<>();
                                        for (Cell cell : headerRow) {
                                            if (cell != null) {
                                                actualColumns.add(cell.getStringCellValue().trim());
                                            }
                                        }

                                        // Check if all expected columns are present
                                        for (String expectedColumn : expectedColumns) {
                                            if (!actualColumns.contains(expectedColumn)) {
                                                errors.add("Thiếu cột bắt buộc: " + expectedColumn);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        
                        workbook.close();
                    } catch (IOException e) {
                        errors.add("Không thể đọc file Excel: " + e.getMessage());
                    } catch (Exception e) {
                        errors.add("File Excel không hợp lệ hoặc bị hỏng");
                    }
                }

                boolean isValid = errors.isEmpty();
                outputData = new ValidateExcelFileOutputData(isValid, errors);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "VALIDATION_ERROR";
            outputData = ValidateExcelFileOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
}

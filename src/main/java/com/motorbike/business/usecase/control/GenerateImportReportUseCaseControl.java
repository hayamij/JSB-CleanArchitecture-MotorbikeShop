package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.generateimportreport.GenerateImportReportInputData;
import com.motorbike.business.dto.generateimportreport.GenerateImportReportOutputData;
import com.motorbike.business.usecase.input.GenerateImportReportInputBoundary;
import com.motorbike.business.usecase.output.GenerateImportReportOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateImportReportUseCaseControl implements GenerateImportReportInputBoundary {
    private final GenerateImportReportOutputBoundary outputBoundary;

    public GenerateImportReportUseCaseControl(GenerateImportReportOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(GenerateImportReportInputData inputData) {
        GenerateImportReportOutputData outputData = generateInternal(inputData);
        outputBoundary.present(outputData);
    }

    public GenerateImportReportOutputData generateInternal(GenerateImportReportInputData inputData) {
        GenerateImportReportOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("GenerateImportReport");
            }
            if (inputData.getTotalRows() < 0) {
                throw ValidationException.invalidInput("Total rows cannot be negative");
            }
            if (inputData.getSuccessCount() < 0) {
                throw ValidationException.invalidInput("Success count cannot be negative");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - generate import report
        if (errorException == null) {
            try {
                int totalRows = inputData.getTotalRows();
                int successCount = inputData.getSuccessCount();
                int failureCount = totalRows - successCount;
                double successRate = totalRows > 0 ? ((double) successCount / totalRows) * 100 : 0.0;

                StringBuilder reportBuilder = new StringBuilder();
                
                // Report header
                reportBuilder.append("==============================================\n");
                reportBuilder.append("        BÁO CÁO IMPORT DỮ LIỆU\n");
                reportBuilder.append("==============================================\n");
                reportBuilder.append("Thời gian: ").append(getCurrentTimestamp()).append("\n\n");

                // Summary section
                reportBuilder.append("TỔNG KẾT:\n");
                reportBuilder.append("- Tổng số dòng: ").append(totalRows).append("\n");
                reportBuilder.append("- Thành công: ").append(successCount).append("\n");
                reportBuilder.append("- Thất bại: ").append(failureCount).append("\n");
                reportBuilder.append("- Tỷ lệ thành công: ").append(String.format("%.2f", successRate)).append("%\n\n");

                // Failure details
                if (inputData.getFailures() != null && !inputData.getFailures().isEmpty()) {
                    reportBuilder.append("CHI TIẾT LỖI:\n");
                    reportBuilder.append("----------------------------------------------\n");
                    
                    for (GenerateImportReportInputData.RowFailure failure : inputData.getFailures()) {
                        reportBuilder.append("Dòng ").append(failure.getRowNumber()).append(":\n");
                        
                        // Show row data
                        if (failure.getRowData() != null && !failure.getRowData().isEmpty()) {
                            reportBuilder.append("  Dữ liệu: ");
                            failure.getRowData().forEach((key, value) -> 
                                reportBuilder.append(key).append("=").append(value).append(", ")
                            );
                            reportBuilder.setLength(reportBuilder.length() - 2); // Remove last ", "
                            reportBuilder.append("\n");
                        }
                        
                        // Show errors
                        if (failure.getErrors() != null && !failure.getErrors().isEmpty()) {
                            reportBuilder.append("  Lỗi:\n");
                            for (String error : failure.getErrors()) {
                                reportBuilder.append("    - ").append(error).append("\n");
                            }
                        }
                        
                        reportBuilder.append("\n");
                    }
                } else {
                    reportBuilder.append("✓ Không có lỗi!\n\n");
                }

                // Report footer
                reportBuilder.append("==============================================\n");
                
                String report = reportBuilder.toString();

                outputData = new GenerateImportReportOutputData(
                        report,
                        totalRows,
                        successCount,
                        failureCount,
                        successRate
                );

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "REPORT_GENERATION_ERROR";
            outputData = GenerateImportReportOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }

    private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}

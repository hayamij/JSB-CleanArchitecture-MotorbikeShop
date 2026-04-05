package com.motorbike.business.dto.generateimportreport;

import java.util.List;
import java.util.Map;

public class GenerateImportReportInputData {
    private final int totalRows;
    private final int successCount;
    private final List<RowFailure> failures;

    public static class RowFailure {
        private final int rowNumber;
        private final Map<String, String> rowData;
        private final List<String> errors;

        public RowFailure(int rowNumber, Map<String, String> rowData, List<String> errors) {
            this.rowNumber = rowNumber;
            this.rowData = rowData;
            this.errors = errors;
        }

        public int getRowNumber() {
            return rowNumber;
        }

        public Map<String, String> getRowData() {
            return rowData;
        }

        public List<String> getErrors() {
            return errors;
        }
    }

    public GenerateImportReportInputData(int totalRows, int successCount, List<RowFailure> failures) {
        this.totalRows = totalRows;
        this.successCount = successCount;
        this.failures = failures;
    }

    public GenerateImportReportInputData(int totalRows, List<RowFailure> failures, int successCount) {
        this.totalRows = totalRows;
        this.successCount = successCount;
        this.failures = failures;
    }
    
    // Convenience constructor that takes error messages as strings
    public GenerateImportReportInputData(int totalRows, int successCount, int failureCount, List<String> errorMessages) {
        this.totalRows = totalRows;
        this.successCount = successCount;
        this.failures = errorMessages != null ? 
            errorMessages.stream()
                .map(msg -> new RowFailure(0, null, List.of(msg)))
                .toList() : List.of();
    }
    
    // Static factory method for 3 params (just messages) for tests
    public static GenerateImportReportInputData fromMessages(int totalRows, int successCount, List<String> errorMessages) {
        return new GenerateImportReportInputData(totalRows, successCount, totalRows - successCount, errorMessages);
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public List<RowFailure> getFailures() {
        return failures;
    }
}

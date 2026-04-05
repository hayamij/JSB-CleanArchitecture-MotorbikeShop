package com.motorbike.business.dto.generateimportreport;

public class GenerateImportReportOutputData {
    private final boolean success;
    private final String report;
    private final int totalRows;
    private final int successCount;
    private final int failureCount;
    private final double successRate;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public GenerateImportReportOutputData(
            String report,
            int totalRows,
            int successCount,
            int failureCount,
            double successRate) {
        this.success = true;
        this.report = report;
        this.totalRows = totalRows;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.successRate = successRate;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private GenerateImportReportOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.report = null;
        this.totalRows = 0;
        this.successCount = 0;
        this.failureCount = 0;
        this.successRate = 0.0;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static GenerateImportReportOutputData forError(String errorCode, String errorMessage) {
        return new GenerateImportReportOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReport() {
        return report;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

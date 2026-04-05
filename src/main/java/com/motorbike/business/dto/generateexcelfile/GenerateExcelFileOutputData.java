package com.motorbike.business.dto.generateexcelfile;

public class GenerateExcelFileOutputData {
    private final boolean success;
    private final byte[] fileBytes;
    private final String fileName;
    private final int rowCount;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public GenerateExcelFileOutputData(byte[] fileBytes, String fileName, int rowCount) {
        this.success = true;
        this.fileBytes = fileBytes;
        this.fileName = fileName;
        this.rowCount = rowCount;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private GenerateExcelFileOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.fileBytes = null;
        this.fileName = null;
        this.rowCount = 0;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static GenerateExcelFileOutputData forError(String errorCode, String errorMessage) {
        return new GenerateExcelFileOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public int getRowCount() {
        return rowCount;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

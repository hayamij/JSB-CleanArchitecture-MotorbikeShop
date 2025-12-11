package com.motorbike.business.dto.accessory;

import java.util.ArrayList;
import java.util.List;

/**
 * Output Data cho use case Import phụ kiện.
 * 
 * Chứa kết quả của quá trình import: số lượng thành công, thất bại, và chi tiết lỗi.
 */
public class ImportAccessoriesOutputData {
    private final boolean hasError;
    private final String errorCode;
    private final String errorMessage;
    
    private final int totalRecords;
    private final int successCount;
    private final int failureCount;
    private final List<ImportError> errors;
    
    private ImportAccessoriesOutputData(boolean hasError, String errorCode, String errorMessage,
                                       int totalRecords, int successCount, int failureCount,
                                       List<ImportError> errors) {
        this.hasError = hasError;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.totalRecords = totalRecords;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.errors = errors != null ? errors : new ArrayList<>();
    }
    
    // Factory method cho trường hợp thành công (có thể có một số dòng lỗi)
    public static ImportAccessoriesOutputData forSuccess(int totalRecords, int successCount,
                                                        int failureCount, List<ImportError> errors) {
        return new ImportAccessoriesOutputData(false, null, null,
                                              totalRecords, successCount, failureCount, errors);
    }
    
    // Factory method cho trường hợp lỗi hệ thống (không thể parse file)
    public static ImportAccessoriesOutputData forError(String errorCode, String errorMessage) {
        return new ImportAccessoriesOutputData(true, errorCode, errorMessage,
                                              0, 0, 0, new ArrayList<>());
    }
    
    // Getters
    public boolean hasError() { return hasError; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public int getTotalRecords() { return totalRecords; }
    public int getSuccessCount() { return successCount; }
    public int getFailureCount() { return failureCount; }
    public List<ImportError> getErrors() { return errors; }
    
    /**
     * Lỗi import cho từng dòng cụ thể
     */
    public static class ImportError {
        private final int rowNumber;
        private final String fieldName;
        private final String errorMessage;
        
        public ImportError(int rowNumber, String fieldName, String errorMessage) {
            this.rowNumber = rowNumber;
            this.fieldName = fieldName;
            this.errorMessage = errorMessage;
        }
        
        public int getRowNumber() { return rowNumber; }
        public String getFieldName() { return fieldName; }
        public String getErrorMessage() { return errorMessage; }
    }
}

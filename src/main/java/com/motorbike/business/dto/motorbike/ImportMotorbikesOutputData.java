package com.motorbike.business.dto.motorbike;

import java.util.List;

/**
 * OutputData cho use case Import xe máy từ Excel.
 * 
 * Chứa kết quả sau khi import:
 * - Số lượng thành công
 * - Số lượng thất bại
 * - Danh sách lỗi (nếu có)
 */
public class ImportMotorbikesOutputData {
    private final boolean hasError;
    private final String errorCode;
    private final String errorMessage;
    private final int totalRecords;
    private final int successCount;
    private final int failureCount;
    private final List<ImportError> errors;
    
    private ImportMotorbikesOutputData(boolean hasError, String errorCode, String errorMessage,
                                      int totalRecords, int successCount, int failureCount,
                                      List<ImportError> errors) {
        this.hasError = hasError;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.totalRecords = totalRecords;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.errors = errors;
    }
    
    public static ImportMotorbikesOutputData forSuccess(int totalRecords, int successCount, 
                                                        int failureCount, List<ImportError> errors) {
        return new ImportMotorbikesOutputData(false, null, null, 
                                             totalRecords, successCount, failureCount, errors);
    }
    
    public static ImportMotorbikesOutputData forError(String errorCode, String errorMessage) {
        return new ImportMotorbikesOutputData(true, errorCode, errorMessage, 
                                             0, 0, 0, null);
    }
    
    public boolean hasError() {
        return hasError;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public int getTotalRecords() {
        return totalRecords;
    }
    
    public int getSuccessCount() {
        return successCount;
    }
    
    public int getFailureCount() {
        return failureCount;
    }
    
    public List<ImportError> getErrors() {
        return errors;
    }
    
    /**
     * Lớp đại diện cho một lỗi import ở dòng cụ thể
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
        
        public int getRowNumber() {
            return rowNumber;
        }
        
        public String getFieldName() {
            return fieldName;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
    }
}

package com.motorbike.adapters.dto.response;

import java.util.List;

/**
 * Response DTO cho API Import Accessories.
 * 
 * Đây là contract API trả về cho client.
 */
public class ImportAccessoriesResponse {
    public int totalRecords;
    public int successCount;
    public int failureCount;
    public List<ErrorDetail> errors;
    
    public ImportAccessoriesResponse(int totalRecords, int successCount, 
                                    int failureCount, List<ErrorDetail> errors) {
        this.totalRecords = totalRecords;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.errors = errors;
    }
    
    public static class ErrorDetail {
        public int rowNumber;
        public String fieldName;
        public String errorMessage;
        
        public ErrorDetail(int rowNumber, String fieldName, String errorMessage) {
            this.rowNumber = rowNumber;
            this.fieldName = fieldName;
            this.errorMessage = errorMessage;
        }
    }
}

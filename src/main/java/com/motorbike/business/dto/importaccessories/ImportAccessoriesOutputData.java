package com.motorbike.business.dto.importaccessories;

public class ImportAccessoriesOutputData {
    private final boolean success;
    private final String message;
    private final int importedCount;
    private final int failedCount;
    
    public ImportAccessoriesOutputData(boolean success, String message, int importedCount, int failedCount) {
        this.success = success;
        this.message = message;
        this.importedCount = importedCount;
        this.failedCount = failedCount;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public int getImportedCount() {
        return importedCount;
    }
    
    public int getFailedCount() {
        return failedCount;
    }
}

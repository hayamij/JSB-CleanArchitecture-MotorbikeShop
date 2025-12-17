package com.motorbike.business.dto.exportaccessories;

public class ExportAccessoriesOutputData {
    private final byte[] fileContent;
    private final String fileName;
    private final boolean success;
    private final String message;
    
    public ExportAccessoriesOutputData(byte[] fileContent, String fileName, boolean success, String message) {
        this.fileContent = fileContent;
        this.fileName = fileName;
        this.success = success;
        this.message = message;
    }
    
    public byte[] getFileContent() {
        return fileContent;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
}

package com.motorbike.business.dto.accessory;

import java.io.InputStream;

/**
 * Input Data cho use case Import phụ kiện từ file Excel.
 * 
 * Chứa stream của file và tên file gốc để validate extension.
 */
public class ImportAccessoriesInputData {
    private final InputStream fileInputStream;
    private final String originalFilename;
    
    public ImportAccessoriesInputData(InputStream fileInputStream, String originalFilename) {
        this.fileInputStream = fileInputStream;
        this.originalFilename = originalFilename;
    }
    
    public InputStream getFileInputStream() {
        return fileInputStream;
    }
    
    public String getOriginalFilename() {
        return originalFilename;
    }
}

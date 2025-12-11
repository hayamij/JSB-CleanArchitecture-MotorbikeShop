package com.motorbike.business.dto.motorbike;

import java.io.InputStream;

/**
 * InputData cho use case Import xe máy từ Excel.
 * 
 * Dữ liệu này đến từ Controller (file upload từ UI/API),
 * rồi được truyền vào lớp UseCaseControl.
 */
public class ImportMotorbikesInputData {
    private final InputStream fileInputStream;
    private final String originalFilename;
    
    public ImportMotorbikesInputData(InputStream fileInputStream, String originalFilename) {
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

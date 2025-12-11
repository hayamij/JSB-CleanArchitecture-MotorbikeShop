package com.motorbike.business.dto.motorbike;

/**
 * Output Data cho Use Case: Export xe máy ra Excel
 */
public class ExportMotorbikesOutputData {
    
    private byte[] excelFileBytes;
    private String fileName;
    private int totalRecords;
    
    public ExportMotorbikesOutputData() {
    }
    
    public ExportMotorbikesOutputData(byte[] excelFileBytes, String fileName, int totalRecords) {
        this.excelFileBytes = excelFileBytes;
        this.fileName = fileName;
        this.totalRecords = totalRecords;
    }
    
    // Getters and Setters
    public byte[] getExcelFileBytes() {
        return excelFileBytes;
    }
    
    public void setExcelFileBytes(byte[] excelFileBytes) {
        this.excelFileBytes = excelFileBytes;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public int getTotalRecords() {
        return totalRecords;
    }
    
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}

package com.motorbike.business.dto.parseexceldata;

import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

public class ParseExcelDataInputData {
    private final MultipartFile file;
    private final InputStream inputStream;
    private final int sheetIndex; // 0-based sheet index, default 0

    public ParseExcelDataInputData(MultipartFile file, int sheetIndex) {
        this.file = file;
        this.inputStream = null;
        this.sheetIndex = sheetIndex;
    }
    
    // Constructor for InputStream (backward compatibility)
    public ParseExcelDataInputData(InputStream inputStream) {
        this.file = null;
        this.inputStream = inputStream;
        this.sheetIndex = 0;
    }
    
    // Constructor with just MultipartFile (default sheet index)
    public ParseExcelDataInputData(MultipartFile file) {
        this(file, 0);
    }

    public MultipartFile getFile() {
        return file;
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }
}

package com.motorbike.business.dto.validateexcelfile;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ValidateExcelFileInputData {
    private final MultipartFile file;
    private final List<String> expectedColumns;

    public ValidateExcelFileInputData(MultipartFile file, List<String> expectedColumns) {
        this.file = file;
        this.expectedColumns = expectedColumns;
    }

    // Constructor with InputStream, String, List<String> (for backward compatibility)
    public ValidateExcelFileInputData(java.io.InputStream inputStream, String filename, List<String> expectedColumns) {
        this.file = null;  // Using InputStream instead
        this.expectedColumns = expectedColumns;
    }

    // Constructor with single MultipartFile (for backward compatibility)
    public ValidateExcelFileInputData(MultipartFile file) {
        this.file = file;
        this.expectedColumns = null;
    }

    public MultipartFile getFile() {
        return file;
    }

    public List<String> getExpectedColumns() {
        return expectedColumns;
    }
}

package com.motorbike.business.dto.generateexcelfile;

import java.util.List;
import java.util.Map;

public class GenerateExcelFileInputData {
    private final List<Map<String, Object>> data;
    private final List<String> headers;
    private final String sheetName;

    public GenerateExcelFileInputData(List<Map<String, Object>> data, List<String> headers, String sheetName) {
        this.data = data;
        this.headers = headers;
        this.sheetName = sheetName;
    }

    public GenerateExcelFileInputData(List<Map<String, Object>> data, List<String> headers) {
        this.data = data;
        this.headers = headers;
        this.sheetName = "Sheet1";
    }
    
    // Static factory with swapped parameters (headers first, then data) for tests
    public static GenerateExcelFileInputData fromHeaders(List<String> headers, List<Map<String, Object>> data) {
        return new GenerateExcelFileInputData(data, headers);
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public String getSheetName() {
        return sheetName;
    }
}

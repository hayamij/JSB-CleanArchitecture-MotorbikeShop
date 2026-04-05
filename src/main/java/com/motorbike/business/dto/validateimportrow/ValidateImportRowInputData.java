package com.motorbike.business.dto.validateimportrow;

import java.util.Map;

public class ValidateImportRowInputData {
    private final Map<String, String> rowData;
    private final int rowNumber;
    private final String importType; // "motorbike", "accessory", etc.

    public ValidateImportRowInputData(Map<String, String> rowData, int rowNumber, String importType) {
        this.rowData = rowData;
        this.rowNumber = rowNumber;
        this.importType = importType;
    }
    
    // Constructor (Map<String,Object>, int)
    public ValidateImportRowInputData(Map<String, Object> rowData, int rowNumber) {
        this.rowData = convertMapToString(rowData);
        this.rowNumber = rowNumber;
        this.importType = "unknown";
    }
    
    // Constructor (List<String>, int, String)
    public ValidateImportRowInputData(java.util.List<String> values, int rowNumber, String importType) {
        this.rowData = convertListToMap(values);
        this.rowNumber = rowNumber;
        this.importType = importType;
    }
    
    private static Map<String, String> convertMapToString(Map<String, Object> input) {
        Map<String, String> result = new java.util.HashMap<>();
        if (input != null) {
            input.forEach((k, v) -> result.put(k, v != null ? v.toString() : ""));
        }
        return result;
    }
    
    private static Map<String, String> convertListToMap(java.util.List<String> values) {
        Map<String, String> result = new java.util.HashMap<>();
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                result.put("col" + i, values.get(i));
            }
        }
        return result;
    }

    public Map<String, String> getRowData() {
        return rowData;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getImportType() {
        return importType;
    }
}

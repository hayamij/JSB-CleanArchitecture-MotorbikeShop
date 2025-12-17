package com.motorbike.business.dto.parseexceldata;

import java.util.List;
import java.util.Map;

public class ParseExcelDataOutputData {
    private final boolean success;
    private final List<Map<String, String>> rows;
    private final int totalRows;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public ParseExcelDataOutputData(List<Map<String, String>> rows) {
        this.success = true;
        this.rows = rows;
        this.totalRows = rows != null ? rows.size() : 0;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private ParseExcelDataOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.rows = null;
        this.totalRows = 0;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ParseExcelDataOutputData forError(String errorCode, String errorMessage) {
        return new ParseExcelDataOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

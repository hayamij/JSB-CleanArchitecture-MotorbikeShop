package com.motorbike.business.dto.formatdataforexport;

import java.util.List;
import java.util.Map;

public class FormatDataForExportOutputData {
    private final boolean success;
    private final List<Map<String, Object>> formattedData;
    private final List<String> headers;
    private final int rowCount;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public FormatDataForExportOutputData(
            List<Map<String, Object>> formattedData,
            List<String> headers) {
        this.success = true;
        this.formattedData = formattedData;
        this.headers = headers;
        this.rowCount = formattedData != null ? formattedData.size() : 0;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private FormatDataForExportOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.formattedData = null;
        this.headers = null;
        this.rowCount = 0;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static FormatDataForExportOutputData forError(String errorCode, String errorMessage) {
        return new FormatDataForExportOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Map<String, Object>> getFormattedData() {
        return formattedData;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public int getRowCount() {
        return rowCount;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

package com.motorbike.business.dto.applysearchfilters;

import java.util.List;

public class ApplySearchFiltersOutputData {
    private final boolean success;
    private final List<?> filteredResults;
    private final int originalCount;
    private final int filteredCount;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public ApplySearchFiltersOutputData(List<?> filteredResults, int originalCount) {
        this.success = true;
        this.filteredResults = filteredResults;
        this.originalCount = originalCount;
        this.filteredCount = filteredResults != null ? filteredResults.size() : 0;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private ApplySearchFiltersOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.filteredResults = null;
        this.originalCount = 0;
        this.filteredCount = 0;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ApplySearchFiltersOutputData forError(String errorCode, String errorMessage) {
        return new ApplySearchFiltersOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<?> getFilteredResults() {
        return filteredResults;
    }

    public int getOriginalCount() {
        return originalCount;
    }

    public int getFilteredCount() {
        return filteredCount;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

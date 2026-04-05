package com.motorbike.business.dto.sortsearchresults;

import java.util.List;

public class SortSearchResultsOutputData {
    private final boolean success;
    private final List<?> sortedResults;
    private final String sortBy;
    private final String direction;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public SortSearchResultsOutputData(List<?> sortedResults, String sortBy, String direction) {
        this.success = true;
        this.sortedResults = sortedResults;
        this.sortBy = sortBy;
        this.direction = direction;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private SortSearchResultsOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.sortedResults = null;
        this.sortBy = null;
        this.direction = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static SortSearchResultsOutputData forError(String errorCode, String errorMessage) {
        return new SortSearchResultsOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<?> getSortedResults() {
        return sortedResults;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getDirection() {
        return direction;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

package com.motorbike.business.dto.buildsearchcriteria;

import java.util.Map;

public class BuildSearchCriteriaOutputData {
    private final boolean success;
    private final Map<String, Object> criteria;
    private final String searchType;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public BuildSearchCriteriaOutputData(Map<String, Object> criteria, String searchType) {
        this.success = true;
        this.criteria = criteria;
        this.searchType = searchType;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private BuildSearchCriteriaOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.criteria = null;
        this.searchType = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static BuildSearchCriteriaOutputData forError(String errorCode, String errorMessage) {
        return new BuildSearchCriteriaOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public Map<String, Object> getCriteria() {
        return criteria;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

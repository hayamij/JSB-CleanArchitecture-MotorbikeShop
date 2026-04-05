package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaInputData;
import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaOutputData;
import com.motorbike.business.usecase.input.BuildSearchCriteriaInputBoundary;
import com.motorbike.business.usecase.output.BuildSearchCriteriaOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;

public class BuildSearchCriteriaUseCaseControl implements BuildSearchCriteriaInputBoundary {
    private final BuildSearchCriteriaOutputBoundary outputBoundary;

    public BuildSearchCriteriaUseCaseControl(BuildSearchCriteriaOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(BuildSearchCriteriaInputData inputData) {
        BuildSearchCriteriaOutputData outputData = buildInternal(inputData);
        outputBoundary.present(outputData);
    }

    public BuildSearchCriteriaOutputData buildInternal(BuildSearchCriteriaInputData inputData) {
        BuildSearchCriteriaOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("BuildSearchCriteria");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - build search criteria
        if (errorException == null) {
            try {
                String keyword = inputData.getKeyword();
                Map<String, Object> filters = inputData.getFilters();
                String searchType = inputData.getSearchType();

                Map<String, Object> criteria = new HashMap<>();

                // Add keyword to criteria if present
                if (keyword != null && !keyword.trim().isEmpty()) {
                    criteria.put("keyword", keyword.trim().toLowerCase());
                }

                // Add filters to criteria
                if (filters != null && !filters.isEmpty()) {
                    filters.forEach((key, value) -> {
                        if (value != null) {
                            // Normalize string values
                            if (value instanceof String) {
                                String strValue = ((String) value).trim();
                                if (!strValue.isEmpty()) {
                                    criteria.put(key, strValue.toLowerCase());
                                }
                            } else {
                                criteria.put(key, value);
                            }
                        }
                    });
                }

                // Add search type specific criteria
                if (searchType != null && !searchType.trim().isEmpty()) {
                    criteria.put("searchType", searchType.toLowerCase());
                    
                    // Add type-specific default filters
                    switch (searchType.toLowerCase()) {
                        case "motorbike":
                        case "xe_may":
                            criteria.putIfAbsent("category", "xe_may");
                            break;
                        case "accessory":
                        case "phu_tung":
                            criteria.putIfAbsent("category", "phu_tung");
                            break;
                        case "order":
                        case "don_hang":
                            // Add default order filters if needed
                            break;
                        case "user":
                        case "nguoi_dung":
                            // Add default user filters if needed
                            break;
                    }
                }

                outputData = new BuildSearchCriteriaOutputData(criteria, searchType);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "BUILD_CRITERIA_ERROR";
            outputData = BuildSearchCriteriaOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
}

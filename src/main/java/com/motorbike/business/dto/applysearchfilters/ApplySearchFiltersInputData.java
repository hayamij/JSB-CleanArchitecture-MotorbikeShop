package com.motorbike.business.dto.applysearchfilters;

import java.util.List;
import java.util.Map;

public class ApplySearchFiltersInputData {
    private final List<?> results;
    private final Map<String, Object> filters;

    public ApplySearchFiltersInputData(List<?> results, Map<String, Object> filters) {
        this.results = results;
        this.filters = filters;
    }

    public List<?> getResults() {
        return results;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }
}

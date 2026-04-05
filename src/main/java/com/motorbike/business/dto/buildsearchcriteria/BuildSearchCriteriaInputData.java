package com.motorbike.business.dto.buildsearchcriteria;

import java.util.HashMap;
import java.util.Map;

public class BuildSearchCriteriaInputData {
    private final String keyword;
    private final Map<String, Object> filters;
    private final String searchType; // "motorbike", "accessory", "user", "order"

    // Constructor with 4 parameters for price range and category
    public BuildSearchCriteriaInputData(String keyword, Double minPrice, Double maxPrice, String category) {
        this.keyword = keyword;
        this.filters = new HashMap<>();
        if (minPrice != null) {
            this.filters.put("minPrice", minPrice);
        }
        if (maxPrice != null) {
            this.filters.put("maxPrice", maxPrice);
        }
        if (category != null) {
            this.filters.put("category", category);
        }
        this.searchType = null;
    }

    // Original constructor with 3 parameters
    public BuildSearchCriteriaInputData(String keyword, Map<String, Object> filters, String searchType) {
        this.keyword = keyword;
        this.filters = filters;
        this.searchType = searchType;
    }
    
    // Constructor for SearchMotorbikesUseCaseControl accepting Map<String, String>
    public BuildSearchCriteriaInputData(String keyword, Map<String, String> stringFilters) {
        this.keyword = keyword;
        this.filters = new HashMap<>();
        if (stringFilters != null) {
            stringFilters.forEach((k, v) -> this.filters.put(k, v));
        }
        this.searchType = null;
    }

    public String getKeyword() {
        return keyword;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public String getSearchType() {
        return searchType;
    }
}

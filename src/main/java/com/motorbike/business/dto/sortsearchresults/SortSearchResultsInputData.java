package com.motorbike.business.dto.sortsearchresults;

import java.util.List;

public class SortSearchResultsInputData {
    private final List<?> results;
    private final String sortBy;
    private final SortDirection direction;

    public enum SortDirection {
        ASC,
        DESC
    }

    public SortSearchResultsInputData(List<?> results, String sortBy, SortDirection direction) {
        this.results = results;
        this.sortBy = sortBy;
        this.direction = direction != null ? direction : SortDirection.ASC;
    }

    // Constructor with List<SanPham>, String, String (for backward compatibility)
    public SortSearchResultsInputData(java.util.List<com.motorbike.domain.entities.SanPham> products, String sortBy, String directionStr) {
        this.results = products;
        this.sortBy = sortBy;
        this.direction = "desc".equalsIgnoreCase(directionStr) ? SortDirection.DESC : SortDirection.ASC;
    }

    public List<?> getResults() {
        return results;
    }

    public String getSortBy() {
        return sortBy;
    }

    public SortDirection getDirection() {
        return direction;
    }
}

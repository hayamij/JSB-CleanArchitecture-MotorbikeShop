package com.motorbike.business.dto.searchadminorder;

public class SearchAdminOrderInputData {
    
    private final String searchQuery;
    private final boolean isAdmin;

    private SearchAdminOrderInputData(String searchQuery, boolean isAdmin) {
        this.searchQuery = searchQuery;
        this.isAdmin = isAdmin;
    }

    public static SearchAdminOrderInputData forAdmin(String searchQuery) {
        return new SearchAdminOrderInputData(searchQuery, true);
    }

    public static SearchAdminOrderInputData forNonAdmin(String searchQuery) {
        return new SearchAdminOrderInputData(searchQuery, false);
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}

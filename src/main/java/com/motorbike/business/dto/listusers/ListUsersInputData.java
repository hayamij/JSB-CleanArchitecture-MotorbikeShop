package com.motorbike.business.dto.listusers;

public class ListUsersInputData {
    private final boolean admin;
    private final String keyword;

    private ListUsersInputData(boolean admin, String keyword) {
        this.admin = admin;
        this.keyword = keyword;
    }
    // Factory: admin + optional keyword
    public static ListUsersInputData forAdmin(boolean admin,String keyword) {
        return new ListUsersInputData(admin, keyword);
    }

    public boolean isAdmin() {
        return admin;
    }
    public String getKeyword() {
        return keyword;
    }
}
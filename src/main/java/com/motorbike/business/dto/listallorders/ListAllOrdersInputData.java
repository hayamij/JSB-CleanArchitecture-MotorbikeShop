package com.motorbike.business.dto.listallorders;

/**
 * Input data for listing orders (admin-only use case).
 */
public class ListAllOrdersInputData {

    private final boolean admin;

    private ListAllOrdersInputData(boolean admin) {
        this.admin = admin;
    }

    public static ListAllOrdersInputData forAdmin() {
        return new ListAllOrdersInputData(true);
    }

    public static ListAllOrdersInputData forNonAdmin() {
        return new ListAllOrdersInputData(false);
    }

    public boolean isAdmin() {
        return admin;
    }
}

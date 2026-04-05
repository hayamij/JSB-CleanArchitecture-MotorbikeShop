package com.motorbike.business.dto.listallorders;

/**
 * Input data for listing all orders. This use-case does not accept filters,
 * sorting or pagination — it simply requests all orders.
 */
public class ListAllOrdersInputData {

    private ListAllOrdersInputData() {
    }

    public static ListAllOrdersInputData getAllOrders() {
        return new ListAllOrdersInputData();
    }
}

package com.motorbike.main.shared;

import java.util.List;

public class ListOrdersResponseData {
    private final List<OrderDTO> orders;

    public ListOrdersResponseData(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }
}

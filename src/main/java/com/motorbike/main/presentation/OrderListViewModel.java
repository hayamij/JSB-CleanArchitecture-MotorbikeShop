package com.motorbike.main.presentation;


import java.util.ArrayList;
import java.util.List;

import com.motorbike.main.shared.OrderDTO;
import com.motorbike.main.shared.Publisher;

public class OrderListViewModel extends Publisher {
    private List<OrderDTO> orders = new ArrayList<>();
    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
        notifyAllSubscribers();
    }
}


package com.motorbike.main.persistence;

import java.util.List;

import com.motorbike.main.shared.OrderDTO;

public interface OrderGateway {
    List<OrderDTO> findAll();
}



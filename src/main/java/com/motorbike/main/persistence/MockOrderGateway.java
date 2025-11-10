package com.motorbike.main.persistence;

import java.time.LocalDate;
import java.util.List;

import com.motorbike.main.shared.OrderDTO;


public class MockOrderGateway implements OrderGateway {

    @Override
    public List<OrderDTO> findAll() {
        return List.of(
            new OrderDTO(1, 1001, "Đang xử lý", LocalDate.now()),
            new OrderDTO(2, 1002, "Hoàn thành", LocalDate.now()),
            new OrderDTO(3, 1003, "Đã hủy", LocalDate.now())
        );
    }
}



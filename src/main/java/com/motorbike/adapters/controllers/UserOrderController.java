package com.motorbike.adapters.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.motorbike.adapters.dto.response.ListMyOrdersResponse;
import com.motorbike.adapters.viewmodels.ListMyOrdersViewModel;
import com.motorbike.business.dto.listmyorders.ListMyOrdersInputData;
import com.motorbike.business.usecase.control.ListMyOrdersUseCaseControl;

@RestController
@RequestMapping("/api/user/orders")
@CrossOrigin(origins = "*")
public class UserOrderController {
    private final ListMyOrdersUseCaseControl listMyOrdersUseCase;
    private final ListMyOrdersViewModel listMyOrdersViewModel;

    @Autowired
    public UserOrderController(ListMyOrdersUseCaseControl listMyOrdersUseCase,
                               ListMyOrdersViewModel listMyOrdersViewModel) {
        this.listMyOrdersUseCase = listMyOrdersUseCase;
        this.listMyOrdersViewModel = listMyOrdersViewModel;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ListMyOrdersResponse> listMyOrders(@PathVariable Long userId) {
        // User chỉ xem được đơn hàng của chính họ
        ListMyOrdersInputData inputData = ListMyOrdersInputData.forUser(userId);

        // Gọi use case
        listMyOrdersUseCase.execute(inputData);

        // Nếu thành công
        if (listMyOrdersViewModel.success) {
            List<ListMyOrdersResponse.OrderItemResponse> orderResponses = new ArrayList<>();

            if (listMyOrdersViewModel.orders != null) {
                for (ListMyOrdersViewModel.OrderItemViewModel item : listMyOrdersViewModel.orders) {
                    orderResponses.add(new ListMyOrdersResponse.OrderItemResponse(
                            item.orderId,
                            item.customerId,
                            item.customerName,
                            item.customerPhone,
                            item.shippingAddress,
                            item.orderStatus,
                            item.formattedTotalAmount,
                            item.totalItems,
                            item.totalQuantity,
                            item.formattedOrderDate,
                            item.statusColor
                    ));
                }
            }

            ListMyOrdersResponse response = new ListMyOrdersResponse(
                true,
                orderResponses,
                listMyOrdersViewModel.message,
                null,
                null
            );

            return ResponseEntity.ok(response);
        }

        // Nếu thất bại
        ListMyOrdersResponse errorResponse = new ListMyOrdersResponse(
            false,
            new ArrayList<>(),
            listMyOrdersViewModel.message,
            listMyOrdersViewModel.errorCode,
            listMyOrdersViewModel.errorMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

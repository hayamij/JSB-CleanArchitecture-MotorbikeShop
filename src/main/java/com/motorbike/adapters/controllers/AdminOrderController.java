package com.motorbike.adapters.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.motorbike.adapters.dto.response.ListAllOrdersResponse;
import com.motorbike.adapters.viewmodels.ListAllOrdersViewModel;
import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.usecase.control.ListAllOrdersUseCaseControl;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {
    private final ListAllOrdersUseCaseControl listAllOrdersUseCase;
    private final ListAllOrdersViewModel listAllOrdersViewModel;

    @Autowired
    public AdminOrderController(ListAllOrdersUseCaseControl listAllOrdersUseCase,ListAllOrdersViewModel listAllOrdersViewModel) 
    {
        this.listAllOrdersUseCase = listAllOrdersUseCase;
        this.listAllOrdersViewModel = listAllOrdersViewModel;
    }
    @GetMapping("/all")
    public ResponseEntity<ListAllOrdersResponse> listAllOrders() {
    // Admin only: lấy tất cả đơn hàng
    ListAllOrdersInputData inputData = ListAllOrdersInputData.forAdmin();

    // Gọi use case
    listAllOrdersUseCase.execute(inputData);

    // Nếu thành công
    if (listAllOrdersViewModel.success) {

        List<ListAllOrdersResponse.OrderItemResponse> orderResponses = new ArrayList<>();

        if (listAllOrdersViewModel.orders != null) {
            for (ListAllOrdersViewModel.OrderItemViewModel item : listAllOrdersViewModel.orders) {
                orderResponses.add(new ListAllOrdersResponse.OrderItemResponse(
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

        // Response chỉ có success + orders + message
        ListAllOrdersResponse response = new ListAllOrdersResponse(
            true,
            orderResponses,
            listAllOrdersViewModel.message,
            null,
            null
        );

        return ResponseEntity.ok(response);
    }

    // Nếu thất bại
        ListAllOrdersResponse errorResponse = new ListAllOrdersResponse(
            false,
            new ArrayList<>(),
            listAllOrdersViewModel.message,
            listAllOrdersViewModel.errorCode,
            listAllOrdersViewModel.errorMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
}


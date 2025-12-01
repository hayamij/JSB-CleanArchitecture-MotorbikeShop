package com.motorbike.adapters.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.motorbike.adapters.dto.response.ListAllOrdersResponse;
import com.motorbike.adapters.viewmodels.ListAllOrdersViewModel;
import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.usecase.control.ListAllOrdersUseCaseControl;

@RestController
@RequestMapping("/api/orders")
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
    public ResponseEntity<ListAllOrdersResponse> listAllOrders(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "sortBy", defaultValue = "date_desc") String sortBy) {

        // Validate
        if (page < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ListAllOrdersResponse(false, new ArrayList<>(), 0, 0, 0, 0,
                            "0 ₫", null, "INVALID_INPUT", "Page không được âm"));
        }
        if (pageSize <= 0 || pageSize > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ListAllOrdersResponse(false, new ArrayList<>(), 0, 0, 0, 0,
                            "0 ₫", null, "INVALID_INPUT", "Page size phải từ 1 đến 100"));
        }

        // Create input data
        ListAllOrdersInputData inputData = ListAllOrdersInputData.withFullFilters(
                page, pageSize, status, sortBy
        );

        // Execute use case
        listAllOrdersUseCase.execute(inputData);

        // Convert ViewModel to Response
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

            ListAllOrdersResponse response = new ListAllOrdersResponse(
                    true,
                    orderResponses,
                    listAllOrdersViewModel.totalOrders,
                    listAllOrdersViewModel.totalPages,
                    listAllOrdersViewModel.currentPage,
                    listAllOrdersViewModel.pageSize,
                    listAllOrdersViewModel.formattedTotalRevenue,
                    listAllOrdersViewModel.message,
                    null,
                    null
            );
            return ResponseEntity.ok(response);
        } else {
            ListAllOrdersResponse response = new ListAllOrdersResponse(
                    false,
                    new ArrayList<>(),
                    0, 0, 0, 0,
                    "0 ₫",
                    listAllOrdersViewModel.message,
                    listAllOrdersViewModel.errorCode,
                    listAllOrdersViewModel.errorMessage
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * GET /api/orders/by-status/{status}
     * Lấy đơn hàng theo trạng thái
     */
    @GetMapping("/by-status/{status}")
    public ResponseEntity<ListAllOrdersResponse> listOrdersByStatus(
            @PathVariable String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "date_desc") String sortBy) {

        ListAllOrdersInputData inputData = ListAllOrdersInputData.withFullFilters(
                page, pageSize, status, sortBy
        );

        listAllOrdersUseCase.execute(inputData);

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

            ListAllOrdersResponse response = new ListAllOrdersResponse(
                    true,
                    orderResponses,
                    listAllOrdersViewModel.totalOrders,
                    listAllOrdersViewModel.totalPages,
                    listAllOrdersViewModel.currentPage,
                    listAllOrdersViewModel.pageSize,
                    listAllOrdersViewModel.formattedTotalRevenue,
                    listAllOrdersViewModel.message,
                    null,
                    null
            );
            return ResponseEntity.ok(response);
        } else {
            ListAllOrdersResponse response = new ListAllOrdersResponse(
                    false,
                    new ArrayList<>(),
                    0, 0, 0, 0,
                    "0 ₫",
                    listAllOrdersViewModel.message,
                    listAllOrdersViewModel.errorCode,
                    listAllOrdersViewModel.errorMessage
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * GET /api/orders/revenue/summary
     * Lấy tóm tắt doanh thu
     */
    @GetMapping("/revenue/summary")
    public ResponseEntity<Map<String, Object>> getRevenueSummary(
            @RequestParam(name = "status", required = false) String status) {

        ListAllOrdersInputData inputData;
        if (status != null) {
            inputData = ListAllOrdersInputData.withStatusFilter(status);
        } else {
            inputData = ListAllOrdersInputData.getAllOrders();
        }

        listAllOrdersUseCase.execute(inputData);

        if (listAllOrdersViewModel.success) {
            Map<String, Object> summary = new HashMap<>();
            summary.put("success", true);
            summary.put("totalOrders", listAllOrdersViewModel.totalOrders);
            summary.put("totalRevenue", listAllOrdersViewModel.formattedTotalRevenue);
            summary.put("message", listAllOrdersViewModel.message);
            
            return ResponseEntity.ok(summary);
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("errorCode", listAllOrdersViewModel.errorCode);
            error.put("errorMessage", listAllOrdersViewModel.errorMessage);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}

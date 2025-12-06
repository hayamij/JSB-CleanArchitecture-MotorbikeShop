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

import com.motorbike.adapters.dto.response.ListAllOrdersResponse;
import com.motorbike.adapters.dto.response.OrderDetailResponse;
import com.motorbike.adapters.dto.response.SearchAdminOrderResponse;
import com.motorbike.adapters.viewmodels.ListAllOrdersViewModel;
import com.motorbike.adapters.viewmodels.OrderDetailViewModel;
import com.motorbike.adapters.viewmodels.SearchAdminOrderViewModel;
import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.dto.orderdetail.OrderDetailInputData;
import com.motorbike.business.dto.searchadminorder.SearchAdminOrderInputData;
import com.motorbike.business.usecase.control.ListAllOrdersUseCaseControl;
import com.motorbike.business.usecase.control.OrderDetailUseCaseControl;
import com.motorbike.business.usecase.control.SearchAdminOrderUseCaseControl;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {
    private final ListAllOrdersUseCaseControl listAllOrdersUseCase;
    private final ListAllOrdersViewModel listAllOrdersViewModel;
    private final OrderDetailUseCaseControl orderDetailUseCase;
    private final OrderDetailViewModel orderDetailViewModel;
    private final SearchAdminOrderUseCaseControl searchAdminOrderUseCase;
    private final SearchAdminOrderViewModel searchAdminOrderViewModel;

    @Autowired
    public AdminOrderController(ListAllOrdersUseCaseControl listAllOrdersUseCase,
                                ListAllOrdersViewModel listAllOrdersViewModel,
                                OrderDetailUseCaseControl orderDetailUseCase,
                                OrderDetailViewModel orderDetailViewModel,
                                SearchAdminOrderUseCaseControl searchAdminOrderUseCase,
                                SearchAdminOrderViewModel searchAdminOrderViewModel) {
        this.listAllOrdersUseCase = listAllOrdersUseCase;
        this.listAllOrdersViewModel = listAllOrdersViewModel;
        this.orderDetailUseCase = orderDetailUseCase;
        this.orderDetailViewModel = orderDetailViewModel;
        this.searchAdminOrderUseCase = searchAdminOrderUseCase;
        this.searchAdminOrderViewModel = searchAdminOrderViewModel;
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

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable Long orderId) {
        OrderDetailInputData inputData = OrderDetailInputData.forAdmin(orderId);
        orderDetailUseCase.execute(inputData);

        if (orderDetailViewModel.success) {
            List<OrderDetailResponse.OrderItemResponse> itemResponses = new ArrayList<>();
            if (orderDetailViewModel.items != null) {
                for (OrderDetailViewModel.OrderItemViewModel item : orderDetailViewModel.items) {
                    itemResponses.add(new OrderDetailResponse.OrderItemResponse(
                            item.orderItemId,
                            item.productId,
                            item.productName,
                            item.formattedUnitPrice,
                            item.quantity,
                            item.formattedLineTotal
                    ));
                }
            }

            OrderDetailResponse response = new OrderDetailResponse(
                    true,
                    orderDetailViewModel.message,
                    orderDetailViewModel.orderId,
                    orderDetailViewModel.customerId,
                    orderDetailViewModel.receiverName,
                    orderDetailViewModel.phoneNumber,
                    orderDetailViewModel.shippingAddress,
                    orderDetailViewModel.orderStatus,
                    orderDetailViewModel.statusColor,
                    orderDetailViewModel.formattedTotalAmount,
                    orderDetailViewModel.totalItems,
                    orderDetailViewModel.totalQuantity,
                    orderDetailViewModel.note,
                    orderDetailViewModel.formattedOrderDate,
                    orderDetailViewModel.formattedUpdatedDate,
                    itemResponses,
                    null,
                    null
            );

            return ResponseEntity.ok(response);
        }

        OrderDetailResponse errorResponse = new OrderDetailResponse(
                false,
                orderDetailViewModel.message,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                0,
                null,
                null,
                null,
                new ArrayList<>(),
                orderDetailViewModel.errorCode,
                orderDetailViewModel.errorMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchAdminOrderResponse> searchOrders(@org.springframework.web.bind.annotation.RequestParam String query) {
        // Admin tìm kiếm tất cả đơn hàng
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin(query);

        // Gọi use case
        searchAdminOrderUseCase.execute(inputData);

        // Nếu thành công
        if (searchAdminOrderViewModel.success) {
            List<SearchAdminOrderResponse.OrderItemResponse> orderResponses = new ArrayList<>();

            if (searchAdminOrderViewModel.orders != null) {
                for (SearchAdminOrderViewModel.OrderItemViewModel item : searchAdminOrderViewModel.orders) {
                    orderResponses.add(new SearchAdminOrderResponse.OrderItemResponse(
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

            SearchAdminOrderResponse response = new SearchAdminOrderResponse(
                true,
                orderResponses,
                searchAdminOrderViewModel.message,
                null,
                null
            );

            return ResponseEntity.ok(response);
        }

        // Nếu thất bại
        SearchAdminOrderResponse errorResponse = new SearchAdminOrderResponse(
            false,
            new ArrayList<>(),
            searchAdminOrderViewModel.message,
            searchAdminOrderViewModel.errorCode,
            searchAdminOrderViewModel.errorMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}


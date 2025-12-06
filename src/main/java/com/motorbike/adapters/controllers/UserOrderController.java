package com.motorbike.adapters.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.motorbike.adapters.dto.request.CancelOrderRequest;
import com.motorbike.adapters.dto.request.UpdateOrderInforRequest;
import com.motorbike.adapters.dto.response.CancelOrderResponse;
import com.motorbike.adapters.dto.response.ListMyOrdersResponse;
import com.motorbike.adapters.dto.response.OrderDetailResponse;
import com.motorbike.adapters.dto.response.UpdateOrderInforResponse;
import com.motorbike.adapters.viewmodels.CancelOrderViewModel;
import com.motorbike.adapters.viewmodels.ListMyOrdersViewModel;
import com.motorbike.adapters.viewmodels.OrderDetailViewModel;
import com.motorbike.adapters.viewmodels.UpdateOrderInforViewModel;
import com.motorbike.business.dto.cancelorder.CancelOrderInputData;
import com.motorbike.business.dto.listmyorders.ListMyOrdersInputData;
import com.motorbike.business.dto.orderdetail.OrderDetailInputData;
import com.motorbike.business.dto.updateorderinfor.UpdateOrderInforInputData;
import com.motorbike.business.usecase.control.CancelOrderUseCaseControl;
import com.motorbike.business.usecase.control.ListMyOrdersUseCaseControl;
import com.motorbike.business.usecase.control.OrderDetailUseCaseControl;
import com.motorbike.business.usecase.control.UpdateOrderInforUseCaseControl;

@RestController
@RequestMapping("/api/user/orders")
@CrossOrigin(origins = "*")
public class UserOrderController {
    private final ListMyOrdersUseCaseControl listMyOrdersUseCase;
    private final ListMyOrdersViewModel listMyOrdersViewModel;
    private final OrderDetailUseCaseControl orderDetailUseCase;
    private final OrderDetailViewModel orderDetailViewModel;
    private final CancelOrderUseCaseControl cancelOrderUseCase;
    private final CancelOrderViewModel cancelOrderViewModel;
    private final UpdateOrderInforUseCaseControl updateOrderInforUseCase;
    private final UpdateOrderInforViewModel updateOrderInforViewModel;

    @Autowired
    public UserOrderController(ListMyOrdersUseCaseControl listMyOrdersUseCase,
                               ListMyOrdersViewModel listMyOrdersViewModel,
                               OrderDetailUseCaseControl orderDetailUseCase,
                               OrderDetailViewModel orderDetailViewModel,
                               CancelOrderUseCaseControl cancelOrderUseCase,
                               CancelOrderViewModel cancelOrderViewModel,
                               UpdateOrderInforUseCaseControl updateOrderInforUseCase,
                               UpdateOrderInforViewModel updateOrderInforViewModel) {
        this.listMyOrdersUseCase = listMyOrdersUseCase;
        this.listMyOrdersViewModel = listMyOrdersViewModel;
        this.orderDetailUseCase = orderDetailUseCase;
        this.orderDetailViewModel = orderDetailViewModel;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.cancelOrderViewModel = cancelOrderViewModel;
        this.updateOrderInforUseCase = updateOrderInforUseCase;
        this.updateOrderInforViewModel = updateOrderInforViewModel;
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

    @GetMapping("/{userId}/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@PathVariable Long userId,
                                                              @PathVariable Long orderId) {
        OrderDetailInputData inputData = OrderDetailInputData.forUser(orderId, userId);
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

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<CancelOrderResponse> cancelOrder(@PathVariable Long orderId,
                                                           @RequestBody CancelOrderRequest request) {
        // Validate input
        if (request.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new CancelOrderResponse(false, null, null, null, null,
                    "INVALID_INPUT", "User ID không được để trống")
            );
        }
        
        if (request.getCancelReason() == null || request.getCancelReason().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new CancelOrderResponse(false, null, null, null, null,
                    "INVALID_INPUT", "Lý do hủy đơn không được để trống")
            );
        }

        CancelOrderInputData inputData = new CancelOrderInputData(
                orderId,
                request.getUserId(),
                request.getCancelReason()
        );

        cancelOrderUseCase.execute(inputData);

        if (cancelOrderViewModel.success) {
            CancelOrderResponse response = new CancelOrderResponse(
                    true,
                    cancelOrderViewModel.orderId,
                    cancelOrderViewModel.orderStatus,
                    cancelOrderViewModel.formattedRefundAmount,
                    cancelOrderViewModel.message,
                    null,
                    null
            );
            return ResponseEntity.ok(response);
        }

        CancelOrderResponse errorResponse = new CancelOrderResponse(
                false,
                null,
                null,
                null,
                null,
                cancelOrderViewModel.errorCode,
                cancelOrderViewModel.errorMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @PutMapping("/{orderId}/shipping")
    public ResponseEntity<UpdateOrderInforResponse> updateOrderInformation(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderInforRequest request) {

        if (request.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new UpdateOrderInforResponse(false, null, null, null, null, null, null,
                    null, null, null, "INVALID_INPUT", "User ID không được để trống")
            );
        }

        if (request.getReceiverName() == null || request.getReceiverName().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new UpdateOrderInforResponse(false, null, null, null, null, null, null,
                    null, null, null, "MISSING_RECEIVER_NAME", "Tên người nhận không được để trống")
            );
        }

        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new UpdateOrderInforResponse(false, null, null, null, null, null, null,
                    null, null, null, "MISSING_PHONE", "Số điện thoại không được để trống")
            );
        }

        if (request.getShippingAddress() == null || request.getShippingAddress().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new UpdateOrderInforResponse(false, null, null, null, null, null, null,
                    null, null, null, "MISSING_ADDRESS", "Địa chỉ giao hàng không được để trống")
            );
        }

        UpdateOrderInforInputData inputData = new UpdateOrderInforInputData(
            orderId,
            request.getUserId(),
            request.getReceiverName(),
            request.getPhoneNumber(),
            request.getShippingAddress(),
            request.getNote()
        );

        updateOrderInforUseCase.execute(inputData);

        if (updateOrderInforViewModel.success) {
            UpdateOrderInforResponse response = new UpdateOrderInforResponse(
                true,
                updateOrderInforViewModel.orderId,
                updateOrderInforViewModel.customerId,
                updateOrderInforViewModel.receiverName,
                updateOrderInforViewModel.phoneNumber,
                updateOrderInforViewModel.shippingAddress,
                updateOrderInforViewModel.note,
                updateOrderInforViewModel.orderStatus,
                updateOrderInforViewModel.updatedAtDisplay,
                updateOrderInforViewModel.message,
                null,
                null
            );

            return ResponseEntity.ok(response);
        }

        UpdateOrderInforResponse errorResponse = new UpdateOrderInforResponse(
            false,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            updateOrderInforViewModel.message,
            updateOrderInforViewModel.errorCode,
            updateOrderInforViewModel.errorMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

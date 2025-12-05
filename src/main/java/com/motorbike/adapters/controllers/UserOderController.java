package com.motorbike.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.motorbike.adapters.dto.request.CancelOrderRequest;
import com.motorbike.adapters.dto.request.UpdateOrderInforRequest;
import com.motorbike.adapters.dto.response.CancelOrderResponse;
import com.motorbike.adapters.dto.response.UpdateOrderInforResponse;
import com.motorbike.adapters.viewmodels.CancelOrderViewModel;
import com.motorbike.adapters.viewmodels.UpdateOrderInforViewModel;
import com.motorbike.business.dto.cancelorder.CancelOrderInputData;
import com.motorbike.business.dto.updateorderinfor.UpdateOrderInforInputData;
import com.motorbike.business.usecase.control.CancelOrderUseCaseControl;
import com.motorbike.business.usecase.control.UpdateOrderInforUseCaseControl;

@RestController
@RequestMapping("/api/user/orders")
@CrossOrigin(origins = "*")
public class UserOderController {
    private final CancelOrderUseCaseControl cancelOrderUseCase;
    private final CancelOrderViewModel cancelOrderViewModel;
    private final UpdateOrderInforUseCaseControl updateOrderInforUseCase;
    private final UpdateOrderInforViewModel updateOrderInforViewModel;
    @Autowired
    public UserOderController(
            CancelOrderUseCaseControl cancelOrderUseCase,
            CancelOrderViewModel cancelOrderViewModel,
            UpdateOrderInforUseCaseControl updateOrderInforUseCase,
            UpdateOrderInforViewModel updateOrderInforViewModel) {
        
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.cancelOrderViewModel = cancelOrderViewModel;
        this.updateOrderInforUseCase = updateOrderInforUseCase;
        this.updateOrderInforViewModel = updateOrderInforViewModel;
    }
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<CancelOrderResponse> cancelOrder(
            @PathVariable Long orderId,
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
        
        // Execute use case
        CancelOrderInputData inputData = new CancelOrderInputData(
            orderId,
            request.getUserId(),
            request.getCancelReason()
        );
        
        cancelOrderUseCase.execute(inputData);
        
        // Convert ViewModel to Response DTO
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
        } else {
            CancelOrderResponse response = new CancelOrderResponse(
                false,
                null,
                null,
                null,
                null,
                cancelOrderViewModel.errorCode,
                cancelOrderViewModel.errorMessage
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
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

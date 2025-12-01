package com.motorbike.adapters.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.motorbike.adapters.dto.request.CancelOrderRequest;
import com.motorbike.adapters.dto.response.CancelOrderResponse;
import com.motorbike.adapters.viewmodels.CancelOrderViewModel;
import com.motorbike.business.dto.cancelorder.CancelOrderInputData;
import com.motorbike.business.usecase.control.CancelOrderUseCaseControl;

@RestController
@RequestMapping("/api/user/orders")
@CrossOrigin(origins = "*")
public class UserOderController {
    private final CancelOrderUseCaseControl cancelOrderUseCase;
    private final CancelOrderViewModel cancelOrderViewModel;
    @Autowired
    public UserOderController(
            CancelOrderUseCaseControl cancelOrderUseCase,
            CancelOrderViewModel cancelOrderViewModel) {
        
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.cancelOrderViewModel = cancelOrderViewModel;
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
}

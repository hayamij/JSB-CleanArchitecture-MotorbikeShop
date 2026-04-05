package com.motorbike.adapters.controllers;

import com.motorbike.business.usecase.control.SearchOrdersUseCaseControl;
import com.motorbike.business.usecase.control.CancelOrderUseCaseControl;
import com.motorbike.business.dto.order.SearchOrdersInputData;
import com.motorbike.business.dto.cancelorder.CancelOrderInputData;
import com.motorbike.adapters.viewmodels.SearchOrdersViewModel;
import com.motorbike.adapters.viewmodels.CancelOrderViewModel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user/orders")
@CrossOrigin(origins = "*")
public class CustomerOrderController {

    private final SearchOrdersUseCaseControl searchOrdersUseCase;
    private final SearchOrdersViewModel searchOrdersViewModel;
    private final CancelOrderUseCaseControl cancelOrderUseCase;
    private final CancelOrderViewModel cancelOrderViewModel;

    public CustomerOrderController(
            SearchOrdersUseCaseControl searchOrdersUseCase,
            SearchOrdersViewModel searchOrdersViewModel,
            CancelOrderUseCaseControl cancelOrderUseCase,
            CancelOrderViewModel cancelOrderViewModel) {
        this.searchOrdersUseCase = searchOrdersUseCase;
        this.searchOrdersViewModel = searchOrdersViewModel;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.cancelOrderViewModel = cancelOrderViewModel;
    }

    // Use Case: SearchOrders (for customer - filter by userId)
    @GetMapping("/{userId}")
    public ResponseEntity<?> getMyOrders(@PathVariable Long userId) {
        // Search orders for this specific user - pass userId as 3rd parameter
        SearchOrdersInputData input = new SearchOrdersInputData(null, null, userId);
        
        searchOrdersUseCase.execute(input);

        if (searchOrdersViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of("success", false,
                        "errorCode", searchOrdersViewModel.errorCode,
                        "errorMessage", searchOrdersViewModel.errorMessage));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "orders", searchOrdersViewModel.orders
        ));
    }

    // Use Case: CancelOrder (customer can only cancel PENDING orders)
    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId, @RequestParam Long userId) {
        CancelOrderInputData input = new CancelOrderInputData(orderId, userId, "Khách hàng hủy đơn");
        
        cancelOrderUseCase.execute(input);

        if (cancelOrderViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of("success", false,
                        "errorCode", cancelOrderViewModel.errorCode,
                        "errorMessage", cancelOrderViewModel.errorMessage));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", cancelOrderViewModel.message,
            "orderId", cancelOrderViewModel.orderId,
            "orderStatus", cancelOrderViewModel.orderStatus,
            "refundAmount", cancelOrderViewModel.formattedRefundAmount
        ));
    }
}

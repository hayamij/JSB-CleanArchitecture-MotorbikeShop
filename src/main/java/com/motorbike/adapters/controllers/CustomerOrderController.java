package com.motorbike.adapters.controllers;

import com.motorbike.business.usecase.control.SearchOrdersUseCaseControl;
import com.motorbike.business.dto.order.SearchOrdersInputData;
import com.motorbike.adapters.viewmodels.SearchOrdersViewModel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user/orders")
@CrossOrigin(origins = "*")
public class CustomerOrderController {

    private final SearchOrdersUseCaseControl searchOrdersUseCase;
    private final SearchOrdersViewModel searchOrdersViewModel;

    public CustomerOrderController(
            SearchOrdersUseCaseControl searchOrdersUseCase,
            SearchOrdersViewModel searchOrdersViewModel) {
        this.searchOrdersUseCase = searchOrdersUseCase;
        this.searchOrdersViewModel = searchOrdersViewModel;
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
}

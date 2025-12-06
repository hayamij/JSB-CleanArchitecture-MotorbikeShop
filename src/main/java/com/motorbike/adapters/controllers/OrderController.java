package com.motorbike.adapters.controllers;

import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.usecase.control.CheckoutUseCaseControl;
import com.motorbike.adapters.viewmodels.CheckoutViewModel;
import com.motorbike.adapters.dto.request.CheckoutRequest;
import com.motorbike.adapters.dto.response.CheckoutResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final CheckoutUseCaseControl checkoutUseCase;
    private final CheckoutViewModel checkoutViewModel;

    @Autowired
    public OrderController(CheckoutUseCaseControl checkoutUseCase,
                          CheckoutViewModel checkoutViewModel) {
        this.checkoutUseCase = checkoutUseCase;
        this.checkoutViewModel = checkoutViewModel;
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody CheckoutRequest request) {
        CheckoutInputData inputData = new CheckoutInputData(
            request.getUserId(),
            request.getReceiverName(),
            request.getPhoneNumber(),
            request.getShippingAddress(),
            request.getNote(),
            request.getPaymentMethod()
        );
        
        checkoutUseCase.execute(inputData);

        if (checkoutViewModel.success) {
            List<CheckoutResponse.OrderItemResponse> responseItems = null;
            if (checkoutViewModel.items != null) {
                responseItems = checkoutViewModel.items.stream()
                    .map(item -> new CheckoutResponse.OrderItemResponse(
                        item.productId, item.productName, null,
                        item.quantity, null
                    ))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new CheckoutResponse(true, checkoutViewModel.message, checkoutViewModel.orderId,
                    checkoutViewModel.customerId, checkoutViewModel.customerName,
                    checkoutViewModel.customerEmail, checkoutViewModel.customerPhone,
                    checkoutViewModel.shippingAddress, checkoutViewModel.orderStatus,
                    null,
                    checkoutViewModel.totalItems, checkoutViewModel.totalQuantity,
                    checkoutViewModel.formattedOrderDate, responseItems,
                    checkoutViewModel.paymentMethod, checkoutViewModel.paymentMethodDisplay,
                    null, null)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new CheckoutResponse(false, null, null, null, null, null, null, null, null,
                    null, 0, 0, null, null, null, null, 
                    checkoutViewModel.errorCode, checkoutViewModel.errorMessage)
            );
        }
    }
    
}

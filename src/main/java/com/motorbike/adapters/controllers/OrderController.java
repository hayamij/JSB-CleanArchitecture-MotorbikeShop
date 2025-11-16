package com.motorbike.adapters.controllers;

import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.usecase.control.CheckoutUseCaseControl;
import com.motorbike.adapters.viewmodels.CheckoutViewModel;
import com.motorbike.adapters.dto.request.CheckoutRequest;
import com.motorbike.adapters.dto.response.CheckoutResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Order operations
 * Handles checkout/payment process
 */
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

    /**
     * POST /api/orders/checkout
     * Thanh toán đơn hàng (Checkout)
     * 
     * Business Rules:
     * - Bắt buộc phải đăng nhập (userId không null)
     * - Giỏ hàng phải có ít nhất 1 sản phẩm
     * - Kiểm tra tồn kho trước khi thanh toán
     * - Tạo đơn hàng với trạng thái CHO_XAC_NHAN
     * - Trừ tồn kho và xóa giỏ hàng sau khi thành công
     * 
     * Request Body:
     * {
     *   "userId": 1,
     *   "receiverName": "Nguyen Van A",
     *   "phoneNumber": "0123456789",
     *   "shippingAddress": "123 Nguyen Trai, Quan 1, TP.HCM",
     *   "note": "Giao giờ hành chính"
     * }
     * 
     * Success Response (201):
     * {
     *   "success": true,
     *   "orderId": 1,
     *   "customerId": 1,
     *   "customerName": "Nguyen Van A",
     *   "customerPhone": "0123456789",
     *   "shippingAddress": "123 Nguyen Trai, Quan 1, TP.HCM",
     *   "orderStatus": "CHO_XAC_NHAN",
     *   "totalAmount": 60000000,
     *   "totalItems": 2,
     *   "totalQuantity": 3,
     *   "items": [
     *     {
     *       "productId": 1,
     *       "productName": "Honda Wave",
     *       "unitPrice": 30000000,
     *       "quantity": 2,
     *       "subtotal": 60000000
     *     }
     *   ],
     *   "orderDate": "2025-11-14T10:30:00"
     * }
     * 
     * Error Responses:
     * 
     * 400 - Empty Cart:
     * {
     *   "success": false,
     *   "errorCode": "EMPTY_CART",
     *   "errorMessage": "Giỏ hàng trống"
     * }
     * 
     * 400 - Insufficient Stock:
     * {
     *   "success": false,
     *   "errorCode": "INSUFFICIENT_STOCK",
     *   "errorMessage": "Sản phẩm 'Honda Wave' chỉ còn 5 trong kho"
     * }
     * 
     * 400 - Invalid Input:
     * {
     *   "success": false,
     *   "errorCode": "INVALID_INPUT",
     *   "errorMessage": "Thiếu thông tin người nhận"
     * }
     */
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody CheckoutRequest request) {
        CheckoutInputData inputData = new CheckoutInputData(
            request.getUserId(),
            request.getReceiverName(),
            request.getPhoneNumber(),
            request.getShippingAddress(),
            request.getNote()
        );
        
        checkoutUseCase.execute(inputData);
        
        // Get data from ViewModel populated by Presenter
        if (checkoutViewModel.success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new CheckoutResponse(true, checkoutViewModel.message)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new CheckoutResponse(false, checkoutViewModel.message)
            );
        }
    }
}

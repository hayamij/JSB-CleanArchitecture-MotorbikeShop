package com.motorbike.adapters.controllers;

import com.motorbike.business.dto.addtocart.AddToCartInputData;
import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.usecase.control.AddToCartUseCaseControl;
import com.motorbike.business.usecase.control.ViewCartUseCaseControl;
import com.motorbike.business.usecase.control.UpdateCartQuantityUseCaseControl;
import com.motorbike.adapters.viewmodels.AddToCartViewModel;
import com.motorbike.adapters.dto.request.AddToCartRequest;
import com.motorbike.adapters.dto.request.UpdateCartRequest;
import com.motorbike.adapters.dto.response.AddToCartResponse;
import com.motorbike.adapters.dto.response.ViewCartResponse;
import com.motorbike.adapters.dto.response.UpdateCartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Cart operations
 * Handles add to cart, view cart, and update cart quantity
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final AddToCartUseCaseControl addToCartUseCase;
    private final ViewCartUseCaseControl viewCartUseCase;
    private final UpdateCartQuantityUseCaseControl updateCartQuantityUseCase;
    private final AddToCartViewModel addToCartViewModel;

    @Autowired
    public CartController(AddToCartUseCaseControl addToCartUseCase,
                         ViewCartUseCaseControl viewCartUseCase,
                         UpdateCartQuantityUseCaseControl updateCartQuantityUseCase,
                         AddToCartViewModel addToCartViewModel) {
        this.addToCartUseCase = addToCartUseCase;
        this.viewCartUseCase = viewCartUseCase;
        this.updateCartQuantityUseCase = updateCartQuantityUseCase;
        this.addToCartViewModel = addToCartViewModel;
    }

    /**
     * POST /api/cart/add
     * Thêm sản phẩm vào giỏ hàng
     * 
     * Request Body:
     * {
     *   "userId": 1,
     *   "productId": 1,
     *   "quantity": 2
     * }
     * 
     * Success Response (200):
     * {
     *   "success": true,
     *   "cartId": 1,
     *   "productId": 1,
     *   "productName": "Honda Wave",
     *   "quantity": 2,
     *   "totalItemsInCart": 3,
     *   "message": "Đã thêm sản phẩm vào giỏ hàng"
     * }
     * 
     * Error Response (400):
     * {
     *   "success": false,
     *   "errorCode": "INSUFFICIENT_STOCK",
     *   "errorMessage": "Không đủ hàng trong kho"
     * }
     */
    @PostMapping("/add")
    public ResponseEntity<AddToCartResponse> addToCart(@RequestBody AddToCartRequest request) {
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(
            request.getProductId(),
            request.getQuantity(),
            request.getUserId()
        );
        
        addToCartUseCase.execute(inputData);
        
        // Convert ViewModel to Response DTO
        if (addToCartViewModel.success) {
            AddToCartResponse response = new AddToCartResponse(
                true, addToCartViewModel.message, null, null
            );
            return ResponseEntity.ok(response);
        } else {
            AddToCartResponse response = new AddToCartResponse(
                false, null, addToCartViewModel.errorCode, addToCartViewModel.errorMessage
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * GET /api/cart/{userId}
     * Xem giỏ hàng của user
     * 
     * Path Parameter:
     * - userId: ID của người dùng (Long)
     * 
     * Success Response (200):
     * {
     *   "success": true,
     *   "cartId": 1,
     *   "userId": 1,
     *   "items": [
     *     {
     *       "productId": 1,
     *       "productName": "Honda Wave",
     *       "price": 30000000,
     *       "quantity": 2,
     *       "subtotal": 60000000
     *     }
     *   ],
     *   "totalAmount": 60000000,
     *   "totalItems": 1
     * }
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ViewCartResponse> viewCart(@PathVariable Long userId) {
        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(userId);
        
        viewCartUseCase.execute(inputData);
        
        // Lấy viewModel từ presenter (đã được inject qua constructor nếu có)
        // Hoặc tạo response từ outputBoundary
        // Tạm thời return success với userId
        return ResponseEntity.ok(new ViewCartResponse(true, userId));
    }

    /**
     * PUT /api/cart/update
     * Cập nhật số lượng sản phẩm trong giỏ hàng
     * 
     * Request Body:
     * {
     *   "userId": 1,
     *   "productId": 1,
     *   "newQuantity": 5
     * }
     * 
     * Note: Nếu newQuantity = 0, sản phẩm sẽ bị xóa khỏi giỏ hàng
     * 
     * Success Response (200):
     * {
     *   "success": true,
     *   "cartId": 1,
     *   "productId": 1,
     *   "newQuantity": 5,
     *   "newSubtotal": 150000000,
     *   "newTotalAmount": 150000000,
     *   "message": "Đã cập nhật số lượng"
     * }
     */
    @PutMapping("/update")
    public ResponseEntity<UpdateCartResponse> updateCartQuantity(@RequestBody UpdateCartRequest request) {
        UpdateCartQuantityInputData inputData = UpdateCartQuantityInputData.forLoggedInUser(
            request.getUserId(),
            request.getProductId(),
            request.getNewQuantity()
        );
        
        updateCartQuantityUseCase.execute(inputData);
        
        // Tạm thời return success response
        return ResponseEntity.ok(new UpdateCartResponse(true, "Đã cập nhật số lượng"));
    }
}

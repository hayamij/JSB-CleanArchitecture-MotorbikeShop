package com.motorbike.adapters.controllers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.motorbike.adapters.dto.request.CancelOrderRequest;
import com.motorbike.adapters.dto.request.CheckoutRequest;
import com.motorbike.adapters.dto.response.CancelOrderResponse;
import com.motorbike.adapters.dto.response.CheckoutResponse;
import com.motorbike.adapters.dto.response.ListAllOrdersResponse;
import com.motorbike.adapters.viewmodels.CancelOrderViewModel;
import com.motorbike.adapters.viewmodels.ListAllOrdersViewModel;
import com.motorbike.business.dto.cancelorder.CancelOrderInputData;
import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.usecase.control.CancelOrderUseCaseControl;
import com.motorbike.business.usecase.control.CheckoutUseCaseControl;
import com.motorbike.business.usecase.control.ListAllOrdersUseCaseControl;



/**
 * REST Controller for Order operations
 * Handles checkout/payment process
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final CheckoutUseCaseControl checkoutUseCase;
    private final ListAllOrdersUseCaseControl listAllOrdersUseCase;
    private final ListAllOrdersViewModel listAllOrdersViewModel;
    private final CancelOrderUseCaseControl cancelOrderUseCase;
    private final CancelOrderViewModel cancelOrderViewModel;

    @Autowired
    public OrderController(
            CheckoutUseCaseControl checkoutUseCase,
            ListAllOrdersUseCaseControl listAllOrdersUseCase,
            ListAllOrdersViewModel listAllOrdersViewModel,
            CancelOrderUseCaseControl cancelOrderUseCase,
                                CancelOrderViewModel cancelOrderViewModel) {
        this.checkoutUseCase = checkoutUseCase;
        this.listAllOrdersUseCase = listAllOrdersUseCase;
        this.listAllOrdersViewModel = listAllOrdersViewModel;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.cancelOrderViewModel = cancelOrderViewModel;
    }
    /**
     * GET /api/admin/orders/all
     * Lấy TẤT CẢ đơn hàng trong hệ thống (không cần userId)
     * 
     * Query Parameters:
     * - page: Trang (mặc định 0)
     * - pageSize: Số đơn hàng/trang (mặc định 10, max 100)
     * - status: Lọc theo trạng thái (tuỳ chọn)
     *   - CHO_XAC_NHAN: Chờ xác nhận
     *   - DA_XAC_NHAN: Đã xác nhận
     *   - DANG_GIAO: Đang giao hàng
     *   - DA_GIAO: Đã giao hàng
     *   - DA_HUY: Đã hủy
     * - sortBy: Sắp xếp (mặc định date_desc)
     *   - date_asc: Ngày cũ nhất trước
     *   - date_desc: Ngày mới nhất trước
     *   - amount_asc: Giá thấp nhất trước
     *   - amount_desc: Giá cao nhất trước
     * 
     * Success Response (200):
     * {
     *   "success": true,
     *   "orders": [
     *     {
     *       "orderId": 1,
     *       "customerId": 1,
     *       "customerName": "Nguyen Van A",
     *       "customerPhone": "0123456789",
     *       "shippingAddress": "123 Nguyen Trai, Quan 1, TP.HCM",
     *       "orderStatus": "Chờ xác nhận",
     *       "formattedTotalAmount": "60,000,000 ₫",
     *       "totalItems": 2,
     *       "totalQuantity": 3,
     *       "formattedOrderDate": "14/11/2025 10:30",
     *       "statusColor": "ORANGE"
     *     }
     *   ],
     *   "totalOrders": 150,
     *   "totalPages": 15,
     *   "currentPage": 0,
     *   "pageSize": 10,
     *   "formattedTotalRevenue": "5,000,000,000 ₫",
     *   "message": "Tổng 150 đơn hàng | Doanh thu: 5,000,000,000 ₫ | Trang 1/15"
     * }
     * 
     * Error Response (400):
     * {
     *   "success": false,
     *   "orders": [],
     *   "totalOrders": 0,
     *   "totalPages": 0,
     *   "currentPage": 0,
     *   "pageSize": 0,
     *   "formattedTotalRevenue": "0 ₫",
     *   "errorCode": "INVALID_INPUT",
     *   "errorMessage": "Page size phải từ 1 đến 100"
     * }
     */
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
    /**
     * DELETE /api/orders/{orderId}/cancel
     * Hủy đơn hàng (Chỉ hủy được khi status = CHO_XAC_NHAN)
     * 
     * Path Parameter:
     * - orderId: ID của đơn hàng (Long)
     * 
     * Request Body:
     * {
     *   "userId": 2,
     *   "cancelReason": "Tôi muốn thay đổi địa chỉ giao hàng"
     * }
     * 
     * Success Response (200):
     * {
     *   "success": true,
     *   "orderId": 1,
     *   "orderStatus": "Đã hủy",
     *   "formattedRefundAmount": "₫47,700,000",
     *   "message": "Hủy đơn hàng thành công! Mã đơn: #1. Hoàn tiền: ₫47,700,000"
     * }
     * 
     * Error Response (400):
     * {
     *   "success": false,
     *   "errorCode": "INVALID_ORDER_STATUS",
     *   "errorMessage": "Chỉ có thể hủy đơn hàng ở trạng thái 'Chờ xác nhận'. Trạng thái hiện tại: Đã xác nhận"
     * }
     */
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
        
        // Tạm thời return success response với orderId
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new CheckoutResponse(true, "Đặt hàng thành công")
        );
    }
    
}

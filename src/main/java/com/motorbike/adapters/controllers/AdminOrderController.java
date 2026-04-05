package com.motorbike.adapters.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.motorbike.adapters.dto.response.ListAllOrdersResponse;
import com.motorbike.adapters.viewmodels.ListAllOrdersViewModel;
import com.motorbike.adapters.viewmodels.UpdateOrderViewModel;
import com.motorbike.adapters.viewmodels.GetOrderDetailViewModel;
import com.motorbike.adapters.viewmodels.GetValidOrderStatusesViewModel;
import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.dto.order.UpdateOrderInputData;
import com.motorbike.business.dto.order.GetOrderDetailInputData;
import com.motorbike.business.dto.order.GetValidOrderStatusesInputData;
import com.motorbike.business.usecase.input.GetOrderDetailInputBoundary;
import com.motorbike.business.usecase.input.GetValidOrderStatusesInputBoundary;
import com.motorbike.business.usecase.input.ListAllOrdersInputBoundary;
import com.motorbike.business.usecase.input.UpdateOrderInputBoundary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "*")
public class AdminOrderController {
    private final ListAllOrdersInputBoundary listAllOrdersUseCase;
    private final ListAllOrdersViewModel listAllOrdersViewModel;
    private final UpdateOrderInputBoundary updateOrderUseCase;
    private final UpdateOrderViewModel updateOrderViewModel;
    private final GetOrderDetailInputBoundary getOrderDetailUseCase;
    private final GetOrderDetailViewModel getOrderDetailViewModel;
    private final GetValidOrderStatusesInputBoundary getValidOrderStatusesUseCase;
    private final GetValidOrderStatusesViewModel getValidOrderStatusesViewModel;
    private final com.motorbike.business.usecase.control.GetTopProductsUseCaseControl getTopProductsUseCase;
    private final com.motorbike.adapters.viewmodels.GetTopProductsViewModel getTopProductsViewModel;

    @Autowired
        public AdminOrderController(
            ListAllOrdersInputBoundary listAllOrdersUseCase,
            ListAllOrdersViewModel listAllOrdersViewModel,
            UpdateOrderInputBoundary updateOrderUseCase,
            UpdateOrderViewModel updateOrderViewModel,
            GetOrderDetailInputBoundary getOrderDetailUseCase,
            GetOrderDetailViewModel getOrderDetailViewModel,
            GetValidOrderStatusesInputBoundary getValidOrderStatusesUseCase,
            GetValidOrderStatusesViewModel getValidOrderStatusesViewModel,
            com.motorbike.business.usecase.control.GetTopProductsUseCaseControl getTopProductsUseCase,
            com.motorbike.adapters.viewmodels.GetTopProductsViewModel getTopProductsViewModel) 
    {
        this.listAllOrdersUseCase = listAllOrdersUseCase;
        this.listAllOrdersViewModel = listAllOrdersViewModel;
        this.updateOrderUseCase = updateOrderUseCase;
        this.updateOrderViewModel = updateOrderViewModel;
        this.getOrderDetailUseCase = getOrderDetailUseCase;
        this.getOrderDetailViewModel = getOrderDetailViewModel;
        this.getValidOrderStatusesUseCase = getValidOrderStatusesUseCase;
        this.getValidOrderStatusesViewModel = getValidOrderStatusesViewModel;
        this.getTopProductsUseCase = getTopProductsUseCase;
        this.getTopProductsViewModel = getTopProductsViewModel;
    }
    @GetMapping("/all")
    public ResponseEntity<ListAllOrdersResponse> listAllOrders() {
    ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();

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
                        item.totalAmount,
                        item.totalItems,
                        item.totalQuantity,
                        item.formattedOrderDate,
                        item.orderDate != null ? item.orderDate.toString() : null,
                        item.statusColor,
                        item.paymentMethodText
                ));
            }
        }

        ListAllOrdersResponse response = new ListAllOrdersResponse(
            true,
            orderResponses,
            listAllOrdersViewModel.message,
            null,
            null
        );

        return ResponseEntity.ok(response);
    }

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
    public ResponseEntity<Map<String, Object>> getOrderDetail(@PathVariable Long orderId) {
        GetOrderDetailInputData inputData = new GetOrderDetailInputData(orderId);
        getOrderDetailUseCase.execute(inputData);
        
        if (getOrderDetailViewModel.success) {
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("orderId", getOrderDetailViewModel.orderDetail.orderId);
            orderData.put("customerId", getOrderDetailViewModel.orderDetail.customerId);
            orderData.put("customerName", getOrderDetailViewModel.orderDetail.customerName);
            orderData.put("customerPhone", getOrderDetailViewModel.orderDetail.customerPhone);
            orderData.put("shippingAddress", getOrderDetailViewModel.orderDetail.shippingAddress);
            orderData.put("orderStatus", getOrderDetailViewModel.orderDetail.orderStatus);
            orderData.put("orderStatusCode", getOrderDetailViewModel.orderDetail.orderStatusCode);
            orderData.put("totalAmount", getOrderDetailViewModel.orderDetail.totalAmount);
            orderData.put("formattedOrderDate", getOrderDetailViewModel.orderDetail.formattedOrderDate);
            orderData.put("note", getOrderDetailViewModel.orderDetail.note);
            orderData.put("paymentMethod", getOrderDetailViewModel.orderDetail.paymentMethod);
            orderData.put("paymentMethodText", getOrderDetailViewModel.orderDetail.paymentMethodText);
            
            List<Map<String, Object>> items = new ArrayList<>();
            for (GetOrderDetailViewModel.OrderItemDisplay item : getOrderDetailViewModel.orderDetail.items) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("productId", item.productId);
                itemData.put("productName", item.productName);
                itemData.put("quantity", item.quantity);
                itemData.put("price", item.price);
                itemData.put("subtotal", item.subtotal);
                items.add(itemData);
            }
            orderData.put("items", items);
            orderData.put("totalItems", getOrderDetailViewModel.orderDetail.totalItems);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("order", orderData);
            
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", getOrderDetailViewModel.errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping("/stats/top-products")
    public ResponseEntity<Map<String, Object>> getTopProducts() {
        com.motorbike.business.dto.topproducts.GetTopProductsInputData inputData = 
                new com.motorbike.business.dto.topproducts.GetTopProductsInputData(10);
        
        getTopProductsUseCase.execute(inputData);
        
        Map<String, Object> response = new HashMap<>();
        if (getTopProductsViewModel.success) {
            response.put("success", true);
            response.put("products", getTopProductsViewModel.products.stream()
                    .map(product -> {
                        Map<String, Object> productMap = new HashMap<>();
                        productMap.put("id", product.productId);
                        productMap.put("name", product.productName);
                        productMap.put("sold", product.totalSold);
                        return productMap;
                    })
                    .collect(java.util.stream.Collectors.toList()));
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("errorMessage", getTopProductsViewModel.errorMessage);
            response.put("errorCode", getTopProductsViewModel.errorCode);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/{orderId}/update")
    public ResponseEntity<Map<String, Object>> updateOrder(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            String note = request.get("note");
            
            UpdateOrderInputData inputData = new UpdateOrderInputData(orderId, status, note);
            updateOrderUseCase.execute(inputData);
            
            Map<String, Object> response = new HashMap<>();
            if (updateOrderViewModel.success) {
                response.put("success", true);
                response.put("message", updateOrderViewModel.message);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("errorMessage", updateOrderViewModel.errorMessage);
                response.put("errorCode", updateOrderViewModel.errorCode);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("errorMessage", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/{orderId}/valid-statuses")
    public ResponseEntity<Map<String, Object>> getValidOrderStatuses(@PathVariable Long orderId) {
        GetOrderDetailInputData orderInputData = new GetOrderDetailInputData(orderId);
        getOrderDetailUseCase.execute(orderInputData);
        
        if (!getOrderDetailViewModel.success) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", getOrderDetailViewModel.errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        String currentStatus = getOrderDetailViewModel.orderDetail.orderStatusCode;
        GetValidOrderStatusesInputData inputData = new GetValidOrderStatusesInputData(currentStatus);
        getValidOrderStatusesUseCase.execute(inputData);
        
        if (getValidOrderStatusesViewModel.success) {
            List<Map<String, String>> statuses = new ArrayList<>();
            for (GetValidOrderStatusesViewModel.StatusOption option : getValidOrderStatusesViewModel.validStatuses) {
                Map<String, String> status = new HashMap<>();
                status.put("code", option.code);
                status.put("display", option.display);
                statuses.add(status);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("validStatuses", statuses);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", getValidOrderStatusesViewModel.errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}


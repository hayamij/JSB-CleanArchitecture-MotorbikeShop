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
import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.dto.order.UpdateOrderInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.control.ListAllOrdersUseCaseControl;
import com.motorbike.business.usecase.control.UpdateOrderUseCaseControl;
import com.motorbike.domain.entities.PhuongThucThanhToan;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.DonHang;

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
    private final ListAllOrdersUseCaseControl listAllOrdersUseCase;
    private final ListAllOrdersViewModel listAllOrdersViewModel;
    private final UpdateOrderUseCaseControl updateOrderUseCase;
    private final UpdateOrderViewModel updateOrderViewModel;
    private final OrderRepository orderRepository;

    @Autowired
    public AdminOrderController(
            ListAllOrdersUseCaseControl listAllOrdersUseCase,
            ListAllOrdersViewModel listAllOrdersViewModel,
            UpdateOrderUseCaseControl updateOrderUseCase,
            UpdateOrderViewModel updateOrderViewModel,
            OrderRepository orderRepository) 
    {
        this.listAllOrdersUseCase = listAllOrdersUseCase;
        this.listAllOrdersViewModel = listAllOrdersViewModel;
        this.updateOrderUseCase = updateOrderUseCase;
        this.updateOrderViewModel = updateOrderViewModel;
        this.orderRepository = orderRepository;
    }
    @GetMapping("/all")
    public ResponseEntity<ListAllOrdersResponse> listAllOrders() {
// InputData mới: lấy tất cả đơn hàng, không filter
    ListAllOrdersInputData inputData = ListAllOrdersInputData.getAllOrders();

    // Gọi use case
    listAllOrdersUseCase.execute(inputData);

    // Nếu thành công
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

        // Response chỉ có success + orders + message
        ListAllOrdersResponse response = new ListAllOrdersResponse(
            true,
            orderResponses,
            listAllOrdersViewModel.message,
            null,
            null
        );

        return ResponseEntity.ok(response);
    }

    // Nếu thất bại
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
        try {
            Optional<DonHang> orderOpt = orderRepository.findById(orderId);
            
            if (!orderOpt.isPresent()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Không tìm thấy đơn hàng");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            DonHang order = orderOpt.get();
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("orderId", order.getMaDonHang());
            orderData.put("customerId", order.getMaTaiKhoan());
            orderData.put("customerName", order.getTenNguoiNhan());
            orderData.put("customerPhone", order.getSoDienThoai());
            orderData.put("shippingAddress", order.getDiaChiGiaoHang());
            orderData.put("orderStatus", order.getTrangThai().getMoTa()); // Use formatted description
            orderData.put("orderStatusCode", order.getTrangThai().name()); // Keep code for form submission
            orderData.put("formattedTotalAmount", currencyFormat.format(order.getTongTien()));
            orderData.put("totalAmount", order.getTongTien());
            orderData.put("formattedOrderDate", order.getNgayDat().format(dateFormatter));
            orderData.put("note", order.getGhiChu());
            orderData.put("paymentMethod", order.getPhuongThucThanhToan().name());
            orderData.put("paymentMethodText", order.getPhuongThucThanhToan() == PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP ? "COD" : "Chuyển khoản");
            
            // Add order items
            List<Map<String, Object>> items = new ArrayList<>();
            for (ChiTietDonHang item : order.getDanhSachSanPham()) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("productId", item.getMaSanPham());
                itemData.put("productName", item.getTenSanPham());
                itemData.put("quantity", item.getSoLuong());
                itemData.put("price", item.getGiaBan());
                itemData.put("formattedPrice", currencyFormat.format(item.getGiaBan()));
                itemData.put("subtotal", item.getThanhTien());
                itemData.put("formattedSubtotal", currencyFormat.format(item.getThanhTien()));
                items.add(itemData);
            }
            orderData.put("items", items);
            orderData.put("totalItems", order.getDanhSachSanPham().size());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("order", orderData);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/stats/top-products")
    public ResponseEntity<Map<String, Object>> getTopProducts() {
        try {
            List<DonHang> orders = orderRepository.findAll();
            Map<String, Integer> productSales = new HashMap<>();
            
            for (DonHang order : orders) {
                if (order.getTrangThai().name().equals("DA_XAC_NHAN") || 
                    order.getTrangThai().name().equals("DANG_GIAO") ||
                    order.getTrangThai().name().equals("DA_GIAO")) {
                    
                    for (ChiTietDonHang item : order.getDanhSachSanPham()) {
                        String productName = item.getTenSanPham();
                        productSales.put(productName, productSales.getOrDefault(productName, 0) + item.getSoLuong());
                    }
                }
            }
            
            // Sort and get top 5
            List<Map<String, Object>> topProducts = productSales.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(entry -> {
                    Map<String, Object> product = new HashMap<>();
                    product.put("name", entry.getKey());
                    product.put("sold", entry.getValue());
                    return product;
                })
                .collect(java.util.stream.Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("products", topProducts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
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
}


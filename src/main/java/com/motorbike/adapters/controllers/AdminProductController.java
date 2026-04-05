package com.motorbike.adapters.controllers;

import com.motorbike.business.usecase.input.DeleteMotorbikeInputBoundary;
import com.motorbike.business.usecase.input.UpdateMotorbikeInputBoundary;
import com.motorbike.business.usecase.input.AddAccessoryInputBoundary;
import com.motorbike.business.usecase.input.UpdateAccessoryInputBoundary;
import com.motorbike.business.usecase.input.DeleteAccessoryInputBoundary;
import com.motorbike.business.usecase.input.ToggleProductVisibilityInputBoundary;
import com.motorbike.business.usecase.control.SearchOrdersUseCaseControl;
import com.motorbike.business.dto.motorbike.DeleteMotorbikeInputData;
import com.motorbike.business.dto.motorbike.UpdateMotorbikeInputData;
import com.motorbike.business.dto.accessory.AddAccessoryInputData;
import com.motorbike.business.dto.accessory.UpdateAccessoryInputData;
import com.motorbike.business.dto.accessory.DeleteAccessoryInputData;
import com.motorbike.business.dto.order.SearchOrdersInputData;
import com.motorbike.business.dto.product.ToggleProductVisibilityInputData;
import com.motorbike.adapters.viewmodels.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminProductController {

    private final DeleteMotorbikeInputBoundary deleteMotorbikeUseCase;
    private final DeleteMotorbikeViewModel deleteMotorbikeViewModel;
    
    private final UpdateMotorbikeInputBoundary updateMotorbikeUseCase;
    private final UpdateMotorbikeViewModel updateMotorbikeViewModel;
    
    private final AddAccessoryInputBoundary addAccessoryUseCase;
    private final AddAccessoryViewModel addAccessoryViewModel;
    
    private final UpdateAccessoryInputBoundary updateAccessoryUseCase;
    private final UpdateAccessoryViewModel updateAccessoryViewModel;
    
    private final DeleteAccessoryInputBoundary deleteAccessoryUseCase;
    private final DeleteAccessoryViewModel deleteAccessoryViewModel;
    
    private final SearchOrdersUseCaseControl searchOrdersUseCase;
    private final SearchOrdersViewModel searchOrdersViewModel;
    
    private final ToggleProductVisibilityInputBoundary toggleProductVisibilityUseCase;
    private final ToggleProductVisibilityViewModel toggleProductVisibilityViewModel;

    public AdminProductController(
            DeleteMotorbikeInputBoundary deleteMotorbikeUseCase,
            DeleteMotorbikeViewModel deleteMotorbikeViewModel,
            UpdateMotorbikeInputBoundary updateMotorbikeUseCase,
            UpdateMotorbikeViewModel updateMotorbikeViewModel,
            AddAccessoryInputBoundary addAccessoryUseCase,
            AddAccessoryViewModel addAccessoryViewModel,
            UpdateAccessoryInputBoundary updateAccessoryUseCase,
            UpdateAccessoryViewModel updateAccessoryViewModel,
            DeleteAccessoryInputBoundary deleteAccessoryUseCase,
            DeleteAccessoryViewModel deleteAccessoryViewModel,
            SearchOrdersUseCaseControl searchOrdersUseCase,
            SearchOrdersViewModel searchOrdersViewModel,
            ToggleProductVisibilityInputBoundary toggleProductVisibilityUseCase,
            ToggleProductVisibilityViewModel toggleProductVisibilityViewModel) {
        this.deleteMotorbikeUseCase = deleteMotorbikeUseCase;
        this.deleteMotorbikeViewModel = deleteMotorbikeViewModel;
        this.updateMotorbikeUseCase = updateMotorbikeUseCase;
        this.updateMotorbikeViewModel = updateMotorbikeViewModel;
        this.addAccessoryUseCase = addAccessoryUseCase;
        this.addAccessoryViewModel = addAccessoryViewModel;
        this.updateAccessoryUseCase = updateAccessoryUseCase;
        this.updateAccessoryViewModel = updateAccessoryViewModel;
        this.deleteAccessoryUseCase = deleteAccessoryUseCase;
        this.deleteAccessoryViewModel = deleteAccessoryViewModel;
        this.searchOrdersUseCase = searchOrdersUseCase;
        this.searchOrdersViewModel = searchOrdersViewModel;
        this.toggleProductVisibilityUseCase = toggleProductVisibilityUseCase;
        this.toggleProductVisibilityViewModel = toggleProductVisibilityViewModel;
    }

    // Use Case: DeleteMotorbike
    @DeleteMapping("/motorbikes/{id}/delete")
    public ResponseEntity<?> deleteMotorbike(@PathVariable Long id) {
        DeleteMotorbikeInputData input = new DeleteMotorbikeInputData(id);
        deleteMotorbikeUseCase.execute(input);

        if (deleteMotorbikeViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of("success", false, 
                        "errorCode", deleteMotorbikeViewModel.errorCode,
                        "errorMessage", deleteMotorbikeViewModel.errorMessage));
        }

        return ResponseEntity.ok(Map.of("success", true, "message", deleteMotorbikeViewModel.successMessage));
    }

    // Use Case: UpdateMotorbike
    @PostMapping("/motorbikes/{id}/update")
    public ResponseEntity<?> updateMotorbike(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        System.out.println("Received update request for motorbike ID: " + id);
        System.out.println("Request body: " + request);
        
        UpdateMotorbikeInputData input;
        try {
            input = new UpdateMotorbikeInputData(
                id,
                (String) request.get("name"),
                (String) request.get("description"),
                new BigDecimal(request.get("price").toString()),
                (String) request.get("imageUrl"),
                request.get("stock") != null ? ((Number) request.get("stock")).intValue() : 0,
                (String) request.get("brand"),
                (String) request.get("model"),
                (String) request.get("color"),
                request.get("year") != null ? ((Number) request.get("year")).intValue() : 0,
                request.get("displacement") != null ? ((Number) request.get("displacement")).intValue() : 0
            );
        } catch (Exception e) {
            System.err.println("Error parsing request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(400)
                    .body(Map.of("success", false,
                        "errorCode", "INVALID_REQUEST",
                        "errorMessage", "Dữ liệu không hợp lệ: " + e.getMessage()));
        }
        
        updateMotorbikeUseCase.execute(input);

        if (updateMotorbikeViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of("success", false,
                        "errorCode", updateMotorbikeViewModel.errorCode,
                        "errorMessage", updateMotorbikeViewModel.errorMessage));
        }

        return ResponseEntity.ok(Map.of("success", true, "message", updateMotorbikeViewModel.successMessage));
    }

    // Use Case: AddAccessory
    @PostMapping("/accessories/add")
    public ResponseEntity<?> addAccessory(@RequestBody Map<String, Object> request) {
        AddAccessoryInputData input = new AddAccessoryInputData(
            (String) request.get("name"),
            (String) request.get("description"),
            new BigDecimal(request.get("price").toString()),
            (String) request.get("imageUrl"),
            ((Number) request.get("stock")).intValue(),
            (String) request.get("loaiPhuKien"),
            (String) request.get("thuongHieu"),
            (String) request.get("chatLieu"),
            (String) request.get("kichThuoc")
        );
        
        addAccessoryUseCase.execute(input);

        if (addAccessoryViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of("success", false,
                        "errorCode", addAccessoryViewModel.errorCode,
                        "errorMessage", addAccessoryViewModel.errorMessage));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "productId", addAccessoryViewModel.maSanPham,
            "message", addAccessoryViewModel.successMessage
        ));
    }

    // Use Case: UpdateAccessory
    @PostMapping("/accessories/{id}/update")
    public ResponseEntity<?> updateAccessory(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        UpdateAccessoryInputData input = new UpdateAccessoryInputData(
            id,
            (String) request.get("name"),
            (String) request.get("description"),
            new BigDecimal(request.get("price").toString()),
            (String) request.get("imageUrl"),
            ((Number) request.get("stock")).intValue(),
            (String) request.get("loaiPhuKien"),
            (String) request.get("thuongHieu"),
            (String) request.get("chatLieu"),
            (String) request.get("kichThuoc")
        );
        
        updateAccessoryUseCase.execute(input);

        if (updateAccessoryViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of("success", false,
                        "errorCode", updateAccessoryViewModel.errorCode,
                        "errorMessage", updateAccessoryViewModel.errorMessage));
        }

        return ResponseEntity.ok(Map.of("success", true, "message", updateAccessoryViewModel.successMessage));
    }

    // Use Case: DeleteAccessory
    @DeleteMapping("/accessories/{id}/delete")
    public ResponseEntity<?> deleteAccessory(@PathVariable Long id) {
        DeleteAccessoryInputData input = new DeleteAccessoryInputData(id);
        deleteAccessoryUseCase.execute(input);

        if (deleteAccessoryViewModel.hasError) {
            return ResponseEntity.status(400)
                    .body(Map.of("success", false,
                        "errorCode", deleteAccessoryViewModel.errorCode,
                        "errorMessage", deleteAccessoryViewModel.errorMessage));
        }

        return ResponseEntity.ok(Map.of("success", true, "message", deleteAccessoryViewModel.message));
    }

    // Use Case: SearchOrders
    @GetMapping("/orders/search")
    public ResponseEntity<?> searchOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {

        SearchOrdersInputData input = new SearchOrdersInputData(keyword, status, null);
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

    // Toggle product visibility (Ẩn/Hiện sản phẩm)
    @PatchMapping("/motorbikes/{id}/visibility")
    public ResponseEntity<?> toggleMotorbikeVisibility(@PathVariable Long id) {
        ToggleProductVisibilityInputData input = new ToggleProductVisibilityInputData(id);
        toggleProductVisibilityUseCase.execute(input);
        
        if (toggleProductVisibilityViewModel.success) {
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", toggleProductVisibilityViewModel.message,
                "isVisible", toggleProductVisibilityViewModel.isVisible
            ));
        } else {
            return ResponseEntity.status(400)
                .body(Map.of(
                    "success", false, 
                    "errorCode", toggleProductVisibilityViewModel.errorCode,
                    "errorMessage", toggleProductVisibilityViewModel.errorMessage
                ));
        }
    }

    @PatchMapping("/accessories/{id}/visibility")
    public ResponseEntity<?> toggleAccessoryVisibility(@PathVariable Long id) {
        ToggleProductVisibilityInputData input = new ToggleProductVisibilityInputData(id);
        toggleProductVisibilityUseCase.execute(input);
        
        if (toggleProductVisibilityViewModel.success) {
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", toggleProductVisibilityViewModel.message,
                "isVisible", toggleProductVisibilityViewModel.isVisible
            ));
        } else {
            return ResponseEntity.status(400)
                .body(Map.of(
                    "success", false, 
                    "errorCode", toggleProductVisibilityViewModel.errorCode,
                    "errorMessage", toggleProductVisibilityViewModel.errorMessage
                ));
        }
    }
}
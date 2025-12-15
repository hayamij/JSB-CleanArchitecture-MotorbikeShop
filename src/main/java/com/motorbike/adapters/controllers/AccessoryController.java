package com.motorbike.adapters.controllers;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.motorbike.adapters.viewmodels.AddAccessoryViewModel;
import com.motorbike.adapters.viewmodels.DeleteAccessoryViewModel;
import com.motorbike.adapters.viewmodels.GetAllAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.SearchAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.UpdateAccessoryViewModel;
import com.motorbike.business.dto.accessory.AddAccessoryInputData;
import com.motorbike.business.dto.accessory.DeleteAccessoryInputData;
import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;
import com.motorbike.business.dto.accessory.UpdateAccessoryInputData;
import com.motorbike.business.usecase.input.AddAccessoryInputBoundary;
import com.motorbike.business.usecase.input.DeleteAccessoryInputBoundary;
import com.motorbike.business.usecase.input.GetAllAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.SearchAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.UpdateAccessoryInputBoundary;

@RestController
@RequestMapping("/api/accessories")
@CrossOrigin(origins = "*")
public class AccessoryController {

    private final GetAllAccessoriesInputBoundary getAllAccessoriesUseCase;
    private final GetAllAccessoriesViewModel getAllViewModel;

    private final SearchAccessoriesInputBoundary searchAccessoriesUseCase;
    private final SearchAccessoriesViewModel searchViewModel;

    private final AddAccessoryInputBoundary addAccessoryUseCase;
    private final AddAccessoryViewModel addViewModel;

    private final UpdateAccessoryInputBoundary updateAccessoryUseCase;
    private final UpdateAccessoryViewModel updateViewModel;

    private final DeleteAccessoryInputBoundary deleteAccessoryUseCase;
    private final DeleteAccessoryViewModel deleteViewModel;

    public AccessoryController(
            GetAllAccessoriesInputBoundary getAllAccessoriesUseCase,
            GetAllAccessoriesViewModel getAllViewModel,
            SearchAccessoriesInputBoundary searchAccessoriesUseCase,
            SearchAccessoriesViewModel searchViewModel,
            AddAccessoryInputBoundary addAccessoryUseCase,
            AddAccessoryViewModel addViewModel,
            UpdateAccessoryInputBoundary updateAccessoryUseCase,
            UpdateAccessoryViewModel updateViewModel,
            DeleteAccessoryInputBoundary deleteAccessoryUseCase,
            DeleteAccessoryViewModel deleteViewModel
    ) {
        this.getAllAccessoriesUseCase = getAllAccessoriesUseCase;
        this.getAllViewModel = getAllViewModel;
        this.searchAccessoriesUseCase = searchAccessoriesUseCase;
        this.searchViewModel = searchViewModel;
        this.addAccessoryUseCase = addAccessoryUseCase;
        this.addViewModel = addViewModel;
        this.updateAccessoryUseCase = updateAccessoryUseCase;
        this.updateViewModel = updateViewModel;
        this.deleteAccessoryUseCase = deleteAccessoryUseCase;
        this.deleteViewModel = deleteViewModel;
    }

    @GetMapping
    public ResponseEntity<?> getAllAccessories() {
        getAllAccessoriesUseCase.execute(null);

        if (getAllViewModel.hasError) {
            return ResponseEntity.status(500).body(new ErrorResponse(getAllViewModel.errorCode, getAllViewModel.errorMessage));
        }

        return ResponseEntity.ok(getAllViewModel.accessories);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAccessories(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {

        SearchAccessoriesInputData input = new SearchAccessoriesInputData(
                keyword, type, brand, material, minPrice, maxPrice
        );

        searchAccessoriesUseCase.execute(input);

        if (searchViewModel.hasError) {
            return ResponseEntity.status(400).body(new ErrorResponse(searchViewModel.errorCode, searchViewModel.errorMessage));
        }

        return ResponseEntity.ok(searchViewModel.accessories);
    }

    @PostMapping
    public ResponseEntity<?> addAccessory(@RequestBody AddAccessoryRequest request) {
        AddAccessoryInputData input = new AddAccessoryInputData(
                request.name,
                request.description,
                request.price,
                request.imageUrl,
                request.stock,
                request.type,
                request.brand,
                request.material,
                request.size,
                "PHU_KIEN"
        );

        addAccessoryUseCase.execute(input);

        if (addViewModel.hasError) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(addViewModel.errorCode, addViewModel.errorMessage));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(addViewModel.accessory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccessory(@PathVariable Long id, @RequestBody UpdateAccessoryRequest request) {
        UpdateAccessoryInputData input = new UpdateAccessoryInputData(
                id,
                request.name,
                request.description,
                request.price,
                request.imageUrl,
                request.stock,
                request.type,
                request.brand,
                request.material,
                request.size
        );

        updateAccessoryUseCase.execute(input);

        if (updateViewModel.hasError) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(updateViewModel.errorCode, updateViewModel.errorMessage));
        }

        return ResponseEntity.ok(updateViewModel.accessory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccessory(@PathVariable Long id) {
        DeleteAccessoryInputData input = new DeleteAccessoryInputData(id);

        deleteAccessoryUseCase.execute(input);

        if (!deleteViewModel.success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(deleteViewModel.errorCode, deleteViewModel.errorMessage));
        }

        return ResponseEntity.ok(new SuccessResponse(deleteViewModel.message));
    }

    // Request DTOs
    private static class AddAccessoryRequest {
        public String name;
        public String description;
        public BigDecimal price;
        public String imageUrl;
        public int stock;
        public String type;
        public String brand;
        public String material;
        public String size;
    }

    private static class UpdateAccessoryRequest {
        public String name;
        public String description;
        public BigDecimal price;
        public String imageUrl;
        public int stock;
        public String type;
        public String brand;
        public String material;
        public String size;
    }

    // Response DTOs
    private static class ErrorResponse {
        public String errorCode;
        public String errorMessage;

        public ErrorResponse(String errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }
    }

    private static class SuccessResponse {
        public String message;

        public SuccessResponse(String message) {
            this.message = message;
        }
    }
}

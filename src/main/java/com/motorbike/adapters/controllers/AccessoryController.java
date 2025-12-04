package com.motorbike.adapters.controllers;

import com.motorbike.adapters.viewmodels.GetAllAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.SearchAccessoriesViewModel;
import com.motorbike.business.usecase.input.GetAllAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.SearchAccessoriesInputBoundary;
import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accessories")
@CrossOrigin(origins = "*")
public class AccessoryController {

    private final GetAllAccessoriesInputBoundary getAllAccessoriesUseCase;
    private final GetAllAccessoriesViewModel getAllViewModel;

    private final SearchAccessoriesInputBoundary searchAccessoriesUseCase;
    private final SearchAccessoriesViewModel searchViewModel;

    public AccessoryController(
            GetAllAccessoriesInputBoundary getAllAccessoriesUseCase,
            GetAllAccessoriesViewModel getAllViewModel,
            SearchAccessoriesInputBoundary searchAccessoriesUseCase,
            SearchAccessoriesViewModel searchViewModel
    ) {
        this.getAllAccessoriesUseCase = getAllAccessoriesUseCase;
        this.getAllViewModel = getAllViewModel;
        this.searchAccessoriesUseCase = searchAccessoriesUseCase;
        this.searchViewModel = searchViewModel;
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

    private static class ErrorResponse {
        public String errorCode;
        public String errorMessage;

        public ErrorResponse(String errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }
    }
}

package com.motorbike.adapters.controllers;

import com.motorbike.adapters.viewmodels.GetAllMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.SearchMotorbikesViewModel;

import com.motorbike.business.usecase.input.GetAllMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.SearchMotorbikesInputBoundary;
import com.motorbike.business.dto.motorbike.SearchMotorbikesInputData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/motorbikes")
@CrossOrigin(origins = "*")
public class MotorbikeController {

    private final GetAllMotorbikesInputBoundary getAllMotorbikesUseCase;
    private final GetAllMotorbikesViewModel getAllViewModel;

    private final SearchMotorbikesInputBoundary searchMotorbikesUseCase;
    private final SearchMotorbikesViewModel searchViewModel;

    public MotorbikeController(
            GetAllMotorbikesInputBoundary getAllMotorbikesUseCase,
            GetAllMotorbikesViewModel getAllViewModel,
            SearchMotorbikesInputBoundary searchMotorbikesUseCase,
            SearchMotorbikesViewModel searchViewModel
    ) {
        this.getAllMotorbikesUseCase = getAllMotorbikesUseCase;
        this.getAllViewModel = getAllViewModel;
        this.searchMotorbikesUseCase = searchMotorbikesUseCase;
        this.searchViewModel = searchViewModel;
    }

    // ============================
    // 1) GET ALL MOTORBIKES
    // ============================
    @GetMapping
    public ResponseEntity<?> getAllMotorbikes() {

        getAllMotorbikesUseCase.execute(null); // Không có input

        if (getAllViewModel.hasError) {
            return ResponseEntity
                    .status(500)
                    .body(new ErrorResponse(getAllViewModel.errorCode, getAllViewModel.errorMessage));
        }

        return ResponseEntity.ok(getAllViewModel.motorbikes);
    }

    // ============================
    // 2) SEARCH MOTORBIKES
    // ============================
    @GetMapping("/search")
    public ResponseEntity<?> searchMotorbikes(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer minCC,
            @RequestParam(required = false) Integer maxCC
    ) {

        SearchMotorbikesInputData input = new SearchMotorbikesInputData(
                keyword,
                brand,
                model,
                color,
                minCC,
                maxCC
        );

        searchMotorbikesUseCase.execute(input);

        if (searchViewModel.hasError) {
            return ResponseEntity
                    .status(400)
                    .body(new ErrorResponse(searchViewModel.errorCode, searchViewModel.errorMessage));
        }

        return ResponseEntity.ok(searchViewModel.motorbikes);
    }

    // ============================
    // ERROR RESPONSE
    // ============================
    private static class ErrorResponse {
        public String errorCode;
        public String errorMessage;

        public ErrorResponse(String errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }
    }
}

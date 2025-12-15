package com.motorbike.adapters.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.motorbike.adapters.dto.request.AddMotorbikeRequest;
import com.motorbike.adapters.dto.response.ImportMotorbikesResponse;
import com.motorbike.business.dto.motorbike.AddMotorbikeInputData;
import com.motorbike.business.dto.motorbike.ImportMotorbikesInputData;
import com.motorbike.business.dto.motorbike.ExportMotorbikesInputData;
import com.motorbike.business.usecase.input.AddMotorbikeInputBoundary;
import com.motorbike.business.usecase.input.ImportMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.ExportMotorbikesInputBoundary;
import com.motorbike.adapters.viewmodels.AddMotorbikeViewModel;
import com.motorbike.adapters.viewmodels.ImportMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.ExportMotorbikesViewModel;


import com.motorbike.adapters.viewmodels.GetAllMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.SearchMotorbikesViewModel;

import com.motorbike.business.usecase.input.GetAllMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.SearchMotorbikesInputBoundary;
import com.motorbike.business.dto.motorbike.SearchMotorbikesInputData;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/motorbikes")
@CrossOrigin(origins = "*")
public class MotorbikeController {

    private final AddMotorbikeInputBoundary addMotorbikeUseCase;
    private final AddMotorbikeViewModel addMotorbikeViewModel;

    private final ImportMotorbikesInputBoundary importMotorbikesUseCase;
    private final ImportMotorbikesViewModel importMotorbikesViewModel;

    private final ExportMotorbikesInputBoundary exportMotorbikesUseCase;
    private final ExportMotorbikesViewModel exportMotorbikesViewModel;

    private final GetAllMotorbikesInputBoundary getAllMotorbikesUseCase;
    private final GetAllMotorbikesViewModel getAllViewModel;

    private final SearchMotorbikesInputBoundary searchMotorbikesUseCase;
    private final SearchMotorbikesViewModel searchViewModel;

    public MotorbikeController(
            GetAllMotorbikesInputBoundary getAllMotorbikesUseCase,
            GetAllMotorbikesViewModel getAllViewModel,
            SearchMotorbikesInputBoundary searchMotorbikesUseCase,
            SearchMotorbikesViewModel searchViewModel,
            AddMotorbikeInputBoundary addMotorbikeUseCase,
            AddMotorbikeViewModel addMotorbikeViewModel,
            ImportMotorbikesInputBoundary importMotorbikesUseCase,
            ImportMotorbikesViewModel importMotorbikesViewModel,
            ExportMotorbikesInputBoundary exportMotorbikesUseCase,
            ExportMotorbikesViewModel exportMotorbikesViewModel
            
    ) {
        this.getAllMotorbikesUseCase = getAllMotorbikesUseCase;
        this.getAllViewModel = getAllViewModel;
        this.searchMotorbikesUseCase = searchMotorbikesUseCase;
        this.searchViewModel = searchViewModel;
        this.addMotorbikeUseCase = addMotorbikeUseCase;
        this.addMotorbikeViewModel = addMotorbikeViewModel;
        this.importMotorbikesUseCase = importMotorbikesUseCase;
        this.importMotorbikesViewModel = importMotorbikesViewModel;
        this.exportMotorbikesUseCase = exportMotorbikesUseCase;
        this.exportMotorbikesViewModel = exportMotorbikesViewModel;
    }

    // ============================
    // 1) GET ALL MOTORBIKES
    // ============================
    @GetMapping
    public ResponseEntity<?> getAllMotorbikes() {

        getAllMotorbikesUseCase.execute(); // Không có input

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
    // 3) ADD MOTORBIKE
    // ============================
    @PostMapping("/add")
    public ResponseEntity<?> addMotorbike(@RequestBody AddMotorbikeRequest request) {

        AddMotorbikeInputData input = new AddMotorbikeInputData(
                request.name,
                request.description,
                new BigDecimal(request.price),
                request.imageUrl,
                request.stock,
                request.brand,
                request.model,
                request.color,
                request.year,
                request.displacement
        );

        addMotorbikeUseCase.execute(input);

        if (addMotorbikeViewModel.hasError) {
            return ResponseEntity
                    .status(400)
                    .body(new ErrorResponse(addMotorbikeViewModel.errorCode, addMotorbikeViewModel.errorMessage));
        }

        return ResponseEntity.ok(java.util.Map.of(
            "success", true,
            "productId", addMotorbikeViewModel.maSanPham,
            "message", addMotorbikeViewModel.successMessage
        ));

    }

    // ============================
    // 4) IMPORT MOTORBIKES FROM EXCEL
    // ============================
    @PostMapping("/import")
    public ResponseEntity<?> importMotorbikes(@RequestParam("file") MultipartFile file) {
        
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("EMPTY_FILE", "File không được để trống"));
            }
            
            // Create input data
            ImportMotorbikesInputData inputData = new ImportMotorbikesInputData(
                file.getInputStream(),
                file.getOriginalFilename()
            );
            
            // Execute use case
            importMotorbikesUseCase.execute(inputData);
            
            // Handle errors
            if (importMotorbikesViewModel.hasError) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse(
                            importMotorbikesViewModel.errorCode,
                            importMotorbikesViewModel.errorMessage
                        ));
            }
            
            // Success - map to response
            var result = importMotorbikesViewModel.importResult;
            var errorDetails = result.getErrors() != null
                ? result.getErrors().stream()
                    .map(e -> new ImportMotorbikesResponse.ErrorDetail(
                        e.getRowNumber(),
                        e.getFieldName(),
                        e.getErrorMessage()
                    ))
                    .collect(Collectors.toList())
                : null;
            
            var response = new ImportMotorbikesResponse(
                result.getTotalRecords(),
                result.getSuccessCount(),
                result.getFailureCount(),
                errorDetails
            );
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            return ResponseEntity
                    .status(500)
                    .body(new ErrorResponse("FILE_READ_ERROR", "Không thể đọc file: " + e.getMessage()));
        }
    }

    // ============================
    // 5) EXPORT MOTORBIKES TO EXCEL
    // ============================
    @GetMapping("/export")
    public ResponseEntity<?> exportMotorbikes(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String color
    ) {
        try {
            // Create input data with filters
            ExportMotorbikesInputData inputData = new ExportMotorbikesInputData(
                keyword,
                brand,
                model,
                color
            );
            
            // Execute use case
            exportMotorbikesUseCase.execute(inputData);
            
            // Handle errors
            if (exportMotorbikesViewModel.hasError) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("EXPORT_ERROR", exportMotorbikesViewModel.errorMessage));
            }
            
            // Success - return file download
            var result = exportMotorbikesViewModel.exportResult;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDisposition(
                org.springframework.http.ContentDisposition.attachment()
                    .filename(result.getFileName())
                    .build()
            );
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);
            
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(result.getExcelFileBytes());
            
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(new ErrorResponse("EXPORT_ERROR", "Lỗi khi export: " + e.getMessage()));
        }
    }



    // ============================
    // ERROR RESPONSE
    // ============================

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

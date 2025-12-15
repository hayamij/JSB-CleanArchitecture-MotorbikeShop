package com.motorbike.adapters.controllers;

import com.motorbike.adapters.viewmodels.GetAllAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.SearchAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.ExportAccessoriesViewModel;
import com.motorbike.adapters.viewmodels.ImportAccessoriesViewModel;
import com.motorbike.adapters.dto.response.ImportAccessoriesResponse;
import com.motorbike.business.usecase.input.GetAllAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.SearchAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.ExportAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.ImportAccessoriesInputBoundary;
import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;
import com.motorbike.business.dto.accessory.ExportAccessoriesInputData;
import com.motorbike.business.dto.accessory.ImportAccessoriesInputData;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accessories")
@CrossOrigin(origins = "*")
public class AccessoryController {

    private final GetAllAccessoriesInputBoundary getAllAccessoriesUseCase;
    private final GetAllAccessoriesViewModel getAllViewModel;

    private final SearchAccessoriesInputBoundary searchAccessoriesUseCase;
    private final SearchAccessoriesViewModel searchViewModel;

    private final ExportAccessoriesInputBoundary exportAccessoriesUseCase;
    private final ExportAccessoriesViewModel exportAccessoriesViewModel;

    private final ImportAccessoriesInputBoundary importAccessoriesUseCase;
    private final ImportAccessoriesViewModel importAccessoriesViewModel;

    public AccessoryController(
            GetAllAccessoriesInputBoundary getAllAccessoriesUseCase,
            GetAllAccessoriesViewModel getAllViewModel,
            SearchAccessoriesInputBoundary searchAccessoriesUseCase,
            SearchAccessoriesViewModel searchViewModel,
            ExportAccessoriesInputBoundary exportAccessoriesUseCase,
            ExportAccessoriesViewModel exportAccessoriesViewModel,
            ImportAccessoriesInputBoundary importAccessoriesUseCase,
            ImportAccessoriesViewModel importAccessoriesViewModel
    ) {
        this.getAllAccessoriesUseCase = getAllAccessoriesUseCase;
        this.getAllViewModel = getAllViewModel;
        this.searchAccessoriesUseCase = searchAccessoriesUseCase;
        this.searchViewModel = searchViewModel;
        this.exportAccessoriesUseCase = exportAccessoriesUseCase;
        this.exportAccessoriesViewModel = exportAccessoriesViewModel;
        this.importAccessoriesUseCase = importAccessoriesUseCase;
        this.importAccessoriesViewModel = importAccessoriesViewModel;
    }

    @GetMapping
    public ResponseEntity<?> getAllAccessories() {
        getAllAccessoriesUseCase.execute();

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

    // ============================
    // EXPORT ACCESSORIES TO EXCEL
    // ============================
    @GetMapping("/export")
    public ResponseEntity<?> exportAccessories(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String material
    ) {
        try {
            // Create input data with filters
            ExportAccessoriesInputData inputData = new ExportAccessoriesInputData(
                keyword,
                type,
                brand,
                material
            );
            
            // Execute use case
            exportAccessoriesUseCase.execute(inputData);
            
            // Handle errors
            if (exportAccessoriesViewModel.hasError) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("EXPORT_ERROR", exportAccessoriesViewModel.errorMessage));
            }
            
            // Success - return file download
            var result = exportAccessoriesViewModel.exportResult;
            
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
    // IMPORT ACCESSORIES FROM EXCEL/CSV
    // ============================
    @PostMapping("/import")
    public ResponseEntity<?> importAccessories(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse("EMPTY_FILE", "File không được để trống"));
            }
            
            // Create input data
            InputStream fileInputStream = file.getInputStream();
            ImportAccessoriesInputData inputData = new ImportAccessoriesInputData(
                    fileInputStream,
                    file.getOriginalFilename()
            );
            
            // Execute use case
            importAccessoriesUseCase.execute(inputData);
            
            // Handle system errors (file parsing errors)
            if (importAccessoriesViewModel.hasError) {
                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse(
                                importAccessoriesViewModel.errorCode,
                                importAccessoriesViewModel.errorMessage
                        ));
            }
            
            // Success - return import result
            var result = importAccessoriesViewModel.importResult;
            
            // Map to Response DTO
            List<ImportAccessoriesResponse.ErrorDetail> errorDetails = result.getErrors()
                    .stream()
                    .map(e -> new ImportAccessoriesResponse.ErrorDetail(
                            e.getRowNumber(),
                            e.getFieldName(),
                            e.getErrorMessage()
                    ))
                    .collect(Collectors.toList());
            
            ImportAccessoriesResponse response = new ImportAccessoriesResponse(
                    result.getTotalRecords(),
                    result.getSuccessCount(),
                    result.getFailureCount(),
                    errorDetails
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(new ErrorResponse("IMPORT_ERROR", "Lỗi khi import: " + e.getMessage()));
        }
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

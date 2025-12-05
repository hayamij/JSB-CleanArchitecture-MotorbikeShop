package com.motorbike.adapters.controllers;

import java.math.BigDecimal;

import com.motorbike.adapters.dto.request.UpdateMotorbikeRequest;

import com.motorbike.adapters.dto.request.AddMotorbikeRequest;
import com.motorbike.adapters.presenters.DeleteMotorbikePresenter;
import com.motorbike.adapters.presenters.UpdateMotorbikePresenter;
import com.motorbike.business.dto.motorbike.AddMotorbikeInputData;
import com.motorbike.business.dto.motorbike.DeleteMotorbikeInputData;
import com.motorbike.business.usecase.input.AddMotorbikeInputBoundary;
import com.motorbike.business.usecase.input.DeleteMotorbikeInputBoundary;
import com.motorbike.adapters.viewmodels.AddMotorbikeViewModel;
import com.motorbike.adapters.viewmodels.DeleteMotorbikeViewModel;
import com.motorbike.adapters.viewmodels.GetAllMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.SearchMotorbikesViewModel;
import com.motorbike.adapters.viewmodels.UpdateMotorbikeViewModel;
import com.motorbike.business.usecase.input.GetAllMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.SearchMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.UpdateMotorbikeInputBoundary;
import com.motorbike.business.usecase.output.DeleteMotorbikeOutputBoundary;
import com.motorbike.business.dto.motorbike.SearchMotorbikesInputData;
import com.motorbike.business.dto.motorbike.UpdateMotorbikeInputData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/motorbikes")
@CrossOrigin(origins = "*")
public class MotorbikeController {

    private final AddMotorbikeInputBoundary addMotorbikeUseCase;
    private final AddMotorbikeViewModel addMotorbikeViewModel;

    private final DeleteMotorbikeOutputBoundary deleteMotorbikePresenter;

    private final DeleteMotorbikeViewModel deleteMotorbikeViewModel;


    private final UpdateMotorbikePresenter updateMotorbikePresenter;
    private final UpdateMotorbikeViewModel updateMotorbikeViewModel;


    private final GetAllMotorbikesInputBoundary getAllMotorbikesUseCase;
    private final GetAllMotorbikesViewModel getAllViewModel;

    private final SearchMotorbikesInputBoundary searchMotorbikesUseCase;
    private final SearchMotorbikesViewModel searchViewModel;

    private final UpdateMotorbikeInputBoundary updateMotorbikeUseCase;
    private final DeleteMotorbikeInputBoundary deleteMotorbikeUseCase;



    public MotorbikeController(
            GetAllMotorbikesInputBoundary getAllMotorbikesUseCase,
            GetAllMotorbikesViewModel getAllViewModel,
            SearchMotorbikesInputBoundary searchMotorbikesUseCase,
            SearchMotorbikesViewModel searchViewModel,
            AddMotorbikeInputBoundary addMotorbikeUseCase,
            AddMotorbikeViewModel addMotorbikeViewModel,
            UpdateMotorbikeInputBoundary updateMotorbikeUseCase,
            UpdateMotorbikePresenter updateMotorbikePresenter,
            UpdateMotorbikeViewModel updateMotorbikeViewModel,
            DeleteMotorbikeOutputBoundary deleteMotorbikePresenter,

            DeleteMotorbikeViewModel deleteMotorbikeViewModel,
            DeleteMotorbikeInputBoundary deleteMotorbikeUseCase



            
    ) {
        this.getAllMotorbikesUseCase = getAllMotorbikesUseCase;
        this.getAllViewModel = getAllViewModel;
        this.searchMotorbikesUseCase = searchMotorbikesUseCase;
        this.searchViewModel = searchViewModel;
        this.addMotorbikeUseCase = addMotorbikeUseCase;
        this.addMotorbikeViewModel = addMotorbikeViewModel;
        this.updateMotorbikeUseCase = updateMotorbikeUseCase;
        this.updateMotorbikePresenter = updateMotorbikePresenter;
        this.updateMotorbikeViewModel = updateMotorbikeViewModel;
        this.deleteMotorbikePresenter = deleteMotorbikePresenter;
        this.deleteMotorbikeViewModel = deleteMotorbikeViewModel;
        this.deleteMotorbikeUseCase = deleteMotorbikeUseCase;


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
    // 3) ADD MOTORBIKE
    // ============================
    @PostMapping("/add")
    public ResponseEntity<?> addMotorbike(@RequestBody AddMotorbikeRequest request) {

        AddMotorbikeInputData input = new AddMotorbikeInputData(
                request.name,
                request.description,
                new BigDecimal(request.price),  // ✔ CONVERT DOUBLE → BigDecimal
                request.imageUrl,
                request.stock,
                request.brand,
                request.model,
                request.color,
                request.year,
                request.displacement,
                request.productType 
        );

        addMotorbikeUseCase.execute(input);

        if (addMotorbikeViewModel.hasError) {
            return ResponseEntity
                    .status(400)
                    .body(new ErrorResponse(addMotorbikeViewModel.errorCode, addMotorbikeViewModel.errorMessage));
        }

        return ResponseEntity.ok(addMotorbikeViewModel.motorbike);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMotorbike(
            @PathVariable Long id,
            @RequestBody UpdateMotorbikeRequest request
    ) {

        UpdateMotorbikeInputData input = new UpdateMotorbikeInputData(
                id,
                request.name,
                request.description,
                request.price == null ? null : new BigDecimal(request.price.toString()),
                request.imageUrl,
                request.stock == null ? 0 : request.stock,
                request.brand,
                request.model,
                request.color,
                request.year == null ? 0 : request.year,
                request.displacement == null ? 0 : request.displacement
        );

        // Gọi use case -> đi qua presenter -> presenter GÁN DATA vào bean updateMotorbikeViewModel (request-scope)
        updateMotorbikeUseCase.execute(input);

        // Lấy đúng bean đã được Spring inject ở constructor
        UpdateMotorbikeViewModel vm = this.updateMotorbikeViewModel;

        if (vm.hasError) {
            return ResponseEntity
                    .badRequest()
                    .body(vm);
        }

        // Trả ra đúng object motorbike, không phải số 1 nữa
        return ResponseEntity.ok(vm.motorbike);
    }


    //delete motorbike
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMotorbike(@PathVariable Long id) {

        DeleteMotorbikeInputData input = new DeleteMotorbikeInputData(id);

        deleteMotorbikeUseCase.execute(input);

        DeleteMotorbikeViewModel vm = this.deleteMotorbikeViewModel;

        if (!vm.success) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse(vm.errorCode, vm.errorMessage));
        }

        return ResponseEntity.ok("Deleted successfully");
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

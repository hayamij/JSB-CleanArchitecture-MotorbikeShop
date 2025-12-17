package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateorder.ValidateOrderInputData;
import com.motorbike.business.dto.validateorder.ValidateOrderOutputData;
import com.motorbike.business.usecase.input.ValidateOrderInputBoundary;
import com.motorbike.business.usecase.output.ValidateOrderOutputBoundary;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import java.util.Optional;

public class ValidateOrderUseCaseControl implements ValidateOrderInputBoundary {
    
    private final ValidateOrderOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;
    
    public ValidateOrderUseCaseControl(ValidateOrderOutputBoundary outputBoundary, OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
    }
    
    // Constructor for tests with swapped parameters (repository first)
    public ValidateOrderUseCaseControl(
            com.motorbike.business.ports.SanPhamRepository sanPhamRepository,
            ValidateOrderOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = null;
    }

    // Constructor with 1 parameter (for backward compatibility)
    public ValidateOrderUseCaseControl(ValidateOrderOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = null;
    }
    
    @Override
    public void execute(ValidateOrderInputData inputData) {
        ValidateOrderOutputData outputData = validateInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public ValidateOrderOutputData validateInternal(ValidateOrderInputData inputData) {
        ValidateOrderOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null || inputData.getOrderId() == null) {
                throw ValidationException.invalidInput();
            }
            
            Long orderId = inputData.getOrderId();
            Optional<DonHang> orderOpt = orderRepository.findById(orderId);
            
            if (orderOpt.isEmpty()) {
                throw DomainException.orderNotFound(orderId);
            }
            
            DonHang order = orderOpt.get();
            
            // Validate order has items
            if (order.getDanhSachSanPham() == null || order.getDanhSachSanPham().isEmpty()) {
                throw DomainException.emptyOrder();
            }
            
            outputData = new ValidateOrderOutputData(true, "Order is valid", orderId);
            
        } catch (Exception e) {
            errorException = e;
            outputData = new ValidateOrderOutputData(false, e.getMessage(), inputData != null ? inputData.getOrderId() : null);
        }
        
        return outputData;
    }
}

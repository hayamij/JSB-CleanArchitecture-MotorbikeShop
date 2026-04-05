package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updateorderstatus.UpdateOrderStatusInputData;
import com.motorbike.business.dto.updateorderstatus.UpdateOrderStatusOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.input.UpdateOrderStatusInputBoundary;
import com.motorbike.business.usecase.output.UpdateOrderStatusOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class UpdateOrderStatusUseCaseControl implements UpdateOrderStatusInputBoundary {
    
    private final UpdateOrderStatusOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;
    
    public UpdateOrderStatusUseCaseControl(
            UpdateOrderStatusOutputBoundary outputBoundary,
            OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
    }
    
    @Override
    public void execute(UpdateOrderStatusInputData inputData) {
        UpdateOrderStatusOutputData outputData = updateStatusInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public UpdateOrderStatusOutputData updateStatusInternal(UpdateOrderStatusInputData inputData) {
        UpdateOrderStatusOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            if (inputData.getOrderId() == null) {
                throw ValidationException.invalidOrderId();
            }
            if (inputData.getNewStatus() == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        DonHang donHang = null;
        String previousStatus = null;
        if (errorException == null) {
            try {
                Long orderId = inputData.getOrderId();
                donHang = orderRepository.findById(orderId)
                    .orElseThrow(() -> DomainException.orderNotFound(orderId));
                
                previousStatus = donHang.getTrangThai().name();
                donHang.capNhatTrangThai(inputData.getNewStatus());
                DonHang savedOrder = orderRepository.save(donHang);
                
                outputData = new UpdateOrderStatusOutputData(savedOrder, previousStatus);
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            }
            
            outputData = UpdateOrderStatusOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
}

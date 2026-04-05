package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.order.UpdateOrderInputData;
import com.motorbike.business.dto.order.UpdateOrderOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.UpdateOrderOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.business.usecase.input.UpdateOrderInputBoundary;
import com.motorbike.domain.exceptions.*;

public class UpdateOrderUseCaseControl implements UpdateOrderInputBoundary {
    
    private final UpdateOrderOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;
    
    public UpdateOrderUseCaseControl(
            UpdateOrderOutputBoundary outputBoundary,
            OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
    }
    
    public void execute(UpdateOrderInputData inputData) {
        UpdateOrderOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            if (inputData.getMaDonHang() == null) {
                throw ValidationException.invalidOrderId();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic
        if (errorException == null) {
            try {
                DonHang donHang = orderRepository.findById(inputData.getMaDonHang())
                    .orElseThrow(() -> DomainException.orderNotFound(inputData.getMaDonHang()));
                
                // Update status if provided
                if (inputData.getTrangThai() != null && !inputData.getTrangThai().isEmpty()) {
                    TrangThaiDonHang trangThaiMoi = TrangThaiDonHang.valueOf(inputData.getTrangThai().toUpperCase());
                    donHang.chuyenTrangThai(trangThaiMoi);
                }
                
                orderRepository.save(donHang);
                
                outputData = UpdateOrderOutputData.forSuccess(
                    donHang.getMaDonHang(),
                    "Cập nhật đơn hàng thành công"
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = UpdateOrderOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 4: Present result
        outputBoundary.present(outputData);
    }
    
    private String extractErrorCode(Exception e) {
        if (e instanceof ValidationException) {
            return ((ValidationException) e).getErrorCode();
        } else if (e instanceof DomainException) {
            return ((DomainException) e).getErrorCode();
        } else if (e instanceof SystemException) {
            return ((SystemException) e).getErrorCode();
        }
        return "SYSTEM_ERROR";
    }
}

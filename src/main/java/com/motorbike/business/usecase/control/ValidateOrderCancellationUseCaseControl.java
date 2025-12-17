package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateordercancellation.ValidateOrderCancellationInputData;
import com.motorbike.business.dto.validateordercancellation.ValidateOrderCancellationOutputData;
import com.motorbike.business.usecase.input.ValidateOrderCancellationInputBoundary;
import com.motorbike.business.usecase.output.ValidateOrderCancellationOutputBoundary;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.domain.exceptions.ValidationException;

public class ValidateOrderCancellationUseCaseControl implements ValidateOrderCancellationInputBoundary {
    
    private final ValidateOrderCancellationOutputBoundary outputBoundary;
    
    public ValidateOrderCancellationUseCaseControl(ValidateOrderCancellationOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(ValidateOrderCancellationInputData inputData) {
        ValidateOrderCancellationOutputData outputData = validateInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public ValidateOrderCancellationOutputData validateInternal(ValidateOrderCancellationInputData inputData) {
        ValidateOrderCancellationOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            if (inputData.getOrderId() == null) {
                throw ValidationException.invalidOrderId();
            }
            if (inputData.getCurrentStatus() == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        if (errorException == null) {
            try {
                TrangThaiDonHang status = inputData.getCurrentStatus();
                boolean canCancel = false;
                String reason = "";
                
                switch (status) {
                    case CHO_XAC_NHAN:
                        canCancel = true;
                        reason = "Đơn hàng đang chờ xác nhận, có thể hủy";
                        break;
                    case DA_XAC_NHAN:
                        canCancel = true;
                        reason = "Đơn hàng đã xác nhận nhưng chưa giao, có thể hủy";
                        break;
                    case DANG_GIAO:
                        canCancel = false;
                        reason = "Đơn hàng đang giao, không thể hủy";
                        break;
                    case DA_GIAO:
                        canCancel = false;
                        reason = "Đơn hàng đã giao, không thể hủy";
                        break;
                    case DA_HUY:
                        canCancel = false;
                        reason = "Đơn hàng đã hủy trước đó";
                        break;
                    default:
                        canCancel = false;
                        reason = "Trạng thái đơn hàng không xác định";
                }
                
                outputData = new ValidateOrderCancellationOutputData(canCancel, reason);
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            }
            
            outputData = ValidateOrderCancellationOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
}

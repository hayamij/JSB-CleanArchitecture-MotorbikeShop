package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.ValidateMotorbikeFieldsInputData;
import com.motorbike.business.dto.motorbike.ValidateMotorbikeFieldsOutputData;
import com.motorbike.business.usecase.input.ValidateMotorbikeFieldsInputBoundary;
import com.motorbike.business.usecase.output.ValidateMotorbikeFieldsOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * UC-75: Validate Motorbike Specific Fields
 * Secondary usecase to validate motorbike-specific fields (brand, model, color, year, CC)
 */
public class ValidateMotorbikeFieldsUseCaseControl implements ValidateMotorbikeFieldsInputBoundary {

    private final ValidateMotorbikeFieldsOutputBoundary outputBoundary;

    public ValidateMotorbikeFieldsUseCaseControl(ValidateMotorbikeFieldsOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ValidateMotorbikeFieldsInputData inputData) {
        ValidateMotorbikeFieldsOutputData outputData = validateInternal(inputData);
        if (outputBoundary != null) {
            outputBoundary.present(outputData);
        }
    }

    /**
     * Internal method for composition by other usecases
     */
    public ValidateMotorbikeFieldsOutputData validateInternal(ValidateMotorbikeFieldsInputData inputData) {
        try {
            List<String> errors = new ArrayList<>();
            
            // Validate Hãng xe (Brand)
            if (inputData.getHangXe() == null || inputData.getHangXe().trim().isEmpty()) {
                errors.add("Hãng xe là bắt buộc");
            }
            
            // Validate Dòng xe (Model)
            if (inputData.getDongXe() == null || inputData.getDongXe().trim().isEmpty()) {
                errors.add("Dòng xe là bắt buộc");
            }
            
            // Validate Màu sắc (Color)
            if (inputData.getMauSac() == null || inputData.getMauSac().trim().isEmpty()) {
                errors.add("Màu sắc là bắt buộc");
            }
            
            // Validate Năm sản xuất (Year)
            if (inputData.getNamSanXuat() == null) {
                errors.add("Năm sản xuất là bắt buộc");
            } else if (inputData.getNamSanXuat() < 2000 || inputData.getNamSanXuat() > 2100) {
                errors.add("Năm sản xuất không hợp lệ (phải từ 2000 đến 2100)");
            }
            
            // Validate Dung tích (Engine Capacity)
            if (inputData.getDungTich() == null) {
                errors.add("Dung tích là bắt buộc");
            } else if (inputData.getDungTich() <= 0) {
                errors.add("Dung tích phải lớn hơn 0");
            }
            
            boolean isValid = errors.isEmpty();
            return ValidateMotorbikeFieldsOutputData.forSuccess(isValid, errors);
            
        } catch (Exception e) {
            return ValidateMotorbikeFieldsOutputData.forError(
                "VALIDATION_ERROR", 
                "Lỗi khi validate các trường xe máy: " + e.getMessage()
            );
        }
    }
}

package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.AddMotorbikeInputData;
import com.motorbike.business.dto.motorbike.AddMotorbikeOutputData;
import com.motorbike.business.dto.motorbike.ValidateMotorbikeFieldsInputData;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.input.AddMotorbikeInputBoundary;
import com.motorbike.business.usecase.input.ValidateMotorbikeFieldsInputBoundary;
import com.motorbike.business.usecase.output.AddMotorbikeOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class AddMotorbikeUseCaseControl implements AddMotorbikeInputBoundary {
    
    private final AddMotorbikeOutputBoundary outputBoundary;
    private final MotorbikeRepository motorbikeRepository;
    private final ValidateMotorbikeFieldsInputBoundary validateMotorbikeFieldsUseCase;
    
    public AddMotorbikeUseCaseControl(
            AddMotorbikeOutputBoundary outputBoundary,
            MotorbikeRepository motorbikeRepository,
            ValidateMotorbikeFieldsInputBoundary validateMotorbikeFieldsUseCase) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = motorbikeRepository;
        this.validateMotorbikeFieldsUseCase = validateMotorbikeFieldsUseCase;
    }

    // Backward compatibility constructor
    public AddMotorbikeUseCaseControl(
            AddMotorbikeOutputBoundary outputBoundary,
            MotorbikeRepository motorbikeRepository) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = motorbikeRepository;
        this.validateMotorbikeFieldsUseCase = new ValidateMotorbikeFieldsUseCaseControl(null);
    }
    
    @Override
    public void execute(AddMotorbikeInputData inputData) {
        AddMotorbikeOutputData outputData = null;
        Exception errorException = null;
        XeMay xeMay = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            SanPham.validateTenSanPham(inputData.getTenSanPham());
            SanPham.validateGia(inputData.getGia());
            SanPham.validateSoLuongTonKho(inputData.getSoLuongTonKho());
            
            // UC-75 Validate motorbike-specific fields
            ValidateMotorbikeFieldsInputData validateFieldsInput = new ValidateMotorbikeFieldsInputData(
                inputData.getHangXe(),
                inputData.getDongXe(),
                inputData.getMauSac(),
                inputData.getNamSanXuat(),
                inputData.getDungTich()
            );
            var validateFieldsResult = ((ValidateMotorbikeFieldsUseCaseControl) validateMotorbikeFieldsUseCase)
                .validateInternal(validateFieldsInput);
            
            if (!validateFieldsResult.isSuccess()) {
                throw new SystemException(validateFieldsResult.getErrorMessage(), validateFieldsResult.getErrorCode());
            }
            
            if (!validateFieldsResult.isValid()) {
                throw new ValidationException(
                    String.join("; ", validateFieldsResult.getErrors()),
                    "INVALID_MOTORBIKE_FIELDS"
                );
            }
            
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Create motorbike entity
        if (errorException == null) {
            try {
                xeMay = new XeMay(
                    inputData.getTenSanPham(),
                    inputData.getMoTa(),
                    inputData.getGia(),
                    inputData.getHinhAnh(),
                    inputData.getSoLuongTonKho(),
                    inputData.getHangXe(),
                    inputData.getDongXe(),
                    inputData.getMauSac(),
                    inputData.getNamSanXuat(),
                    inputData.getDungTich()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Save to database
        if (errorException == null && xeMay != null) {
            try {
                xeMay = motorbikeRepository.save(xeMay);
                
                outputData = AddMotorbikeOutputData.forSuccess(
                    xeMay.getMaSanPham(),
                    xeMay.getTenSanPham(),
                    xeMay.getHangXe(),
                    xeMay.getDongXe(),
                    xeMay.getMauSac(),
                    xeMay.getNamSanXuat(),
                    xeMay.getDungTich(),
                    xeMay.getGia(),
                    xeMay.getNgayTao()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = AddMotorbikeOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 5: Present result
        outputBoundary.present(outputData);
    }
    
    private String extractErrorCode(Exception exception) {
        if (exception instanceof ValidationException) {
            return ((ValidationException) exception).getErrorCode();
        } else if (exception instanceof DomainException) {
            return ((DomainException) exception).getErrorCode();
        } else if (exception instanceof SystemException) {
            return ((SystemException) exception).getErrorCode();
        }
        return "SYSTEM_ERROR";
    }
}

package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.AddMotorbikeInputData;
import com.motorbike.business.dto.motorbike.AddMotorbikeOutputData;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.input.AddMotorbikeInputBoundary;
import com.motorbike.business.usecase.output.AddMotorbikeOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class AddMotorbikeUseCaseControl implements AddMotorbikeInputBoundary {
    
    private final AddMotorbikeOutputBoundary outputBoundary;
    private final MotorbikeRepository motorbikeRepository;
    
    public AddMotorbikeUseCaseControl(AddMotorbikeOutputBoundary outputBoundary,
                                     MotorbikeRepository motorbikeRepository) {
        this.outputBoundary = outputBoundary;
        this.motorbikeRepository = motorbikeRepository;
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
            
            validateMotorbikeFields(inputData);
            
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
    
    private void validateMotorbikeFields(AddMotorbikeInputData inputData) {
        if (inputData.getHangXe() == null || inputData.getHangXe().trim().isEmpty()) {
            throw ValidationException.fieldRequired("Hãng xe");
        }
        if (inputData.getDongXe() == null || inputData.getDongXe().trim().isEmpty()) {
            throw ValidationException.fieldRequired("Dòng xe");
        }
        if (inputData.getMauSac() == null || inputData.getMauSac().trim().isEmpty()) {
            throw ValidationException.fieldRequired("Màu sắc");
        }
        if (inputData.getNamSanXuat() < 2000 || inputData.getNamSanXuat() > 2100) {
            throw ValidationException.invalidYear();
        }
        if (inputData.getDungTich() <= 0) {
            throw ValidationException.invalidEngineCapacity();
        }
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

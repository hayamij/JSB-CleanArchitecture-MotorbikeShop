package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.CreateMotorbikeInputData;
import com.motorbike.business.dto.motorbike.CreateMotorbikeOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.CreateMotorbikeOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.exceptions.*;

public class CreateMotorbikeUseCaseControl {
    
    private final CreateMotorbikeOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    public CreateMotorbikeUseCaseControl(
            CreateMotorbikeOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    public void execute(CreateMotorbikeInputData inputData) {
        CreateMotorbikeOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            if (inputData.getTenSanPham() == null || inputData.getTenSanPham().trim().isEmpty()) {
                throw ValidationException.emptyProductName();
            }
            
            if (inputData.getGia() == null || inputData.getGia().doubleValue() <= 0) {
                throw ValidationException.invalidPrice();
            }
            
            if (inputData.getSoLuongTonKho() < 0) {
                throw ValidationException.invalidStock();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic
        if (errorException == null) {
            try {
                XeMay xeMay = new XeMay(
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
                
                XeMay savedXeMay = (XeMay) productRepository.save(xeMay);
                
                outputData = CreateMotorbikeOutputData.forSuccess(
                    savedXeMay.getMaSanPham(),
                    savedXeMay.getTenSanPham(),
                    savedXeMay.getGia()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = CreateMotorbikeOutputData.forError(errorCode, errorException.getMessage());
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

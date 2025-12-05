package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.CreateAccessoryInputData;
import com.motorbike.business.dto.accessory.CreateAccessoryOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.CreateAccessoryOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.exceptions.*;

public class CreateAccessoryUseCaseControl {
    
    private final CreateAccessoryOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    public CreateAccessoryUseCaseControl(
            CreateAccessoryOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    public void execute(CreateAccessoryInputData inputData) {
        CreateAccessoryOutputData outputData = null;
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
                PhuKienXeMay phuKien = new PhuKienXeMay(
                    inputData.getTenSanPham(),
                    inputData.getMoTa(),
                    inputData.getGia(),
                    inputData.getHinhAnh(),
                    inputData.getSoLuongTonKho(),
                    inputData.getLoaiPhuKien(),
                    inputData.getThuongHieu(),
                    inputData.getChatLieu(),
                    inputData.getKichThuoc()
                );
                
                PhuKienXeMay savedPhuKien = (PhuKienXeMay) productRepository.save(phuKien);
                
                outputData = CreateAccessoryOutputData.forSuccess(
                    savedPhuKien.getMaSanPham(),
                    savedPhuKien.getTenSanPham(),
                    savedPhuKien.getGia()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = CreateAccessoryOutputData.forError(errorCode, errorException.getMessage());
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

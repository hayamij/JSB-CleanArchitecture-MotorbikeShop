package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.UpdateMotorbikeInputData;
import com.motorbike.business.dto.motorbike.UpdateMotorbikeOutputData;
import com.motorbike.business.dto.validateproductdata.ValidateProductDataInputData;
import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.UpdateMotorbikeInputBoundary;
import com.motorbike.business.usecase.input.ValidateProductDataInputBoundary;
import com.motorbike.business.usecase.input.CheckProductDuplicationInputBoundary;
import com.motorbike.business.usecase.output.UpdateMotorbikeOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class UpdateMotorbikeUseCaseControl implements UpdateMotorbikeInputBoundary {
    
    private final UpdateMotorbikeOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final ValidateProductDataInputBoundary validateProductDataUseCase;
    private final CheckProductDuplicationInputBoundary checkDuplicationUseCase;
    
    public UpdateMotorbikeUseCaseControl(UpdateMotorbikeOutputBoundary outputBoundary,
                                        ProductRepository productRepository,
                                        ValidateProductDataInputBoundary validateProductDataUseCase,
                                        CheckProductDuplicationInputBoundary checkDuplicationUseCase) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.validateProductDataUseCase = validateProductDataUseCase;
        this.checkDuplicationUseCase = checkDuplicationUseCase;
    }

    // Constructor with 2 parameters (for backward compatibility)
    public UpdateMotorbikeUseCaseControl(
            UpdateMotorbikeOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.validateProductDataUseCase = new ValidateProductDataUseCaseControl(null);
        this.checkDuplicationUseCase = new CheckProductDuplicationUseCaseControl(null, productRepository);
    }
    
    @Override
    public void execute(UpdateMotorbikeInputData inputData) {
        UpdateMotorbikeOutputData outputData = null;
        Exception errorException = null;
        XeMay xeMay = null;
        
        // Step 1: Basic validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            if (inputData.getMaSanPham() == null) {
                throw ValidationException.invalidProductId();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: UC-51 - Validate product data
        if (errorException == null) {
            try {
                ValidateProductDataInputData validateInput = new ValidateProductDataInputData(
                    inputData.getTenSanPham(),
                    null,
                    inputData.getGia(),
                    inputData.getSoLuongTonKho(),
                    "xe_may"
                );
                var validateResult = ((ValidateProductDataUseCaseControl) validateProductDataUseCase)
                    .validateInternal(validateInput);
                
                if (!validateResult.isSuccess()) {
                    throw new DomainException(validateResult.getErrorMessage(), validateResult.getErrorCode());
                }
                
                if (!validateResult.isValid()) {
                    throw new ValidationException(
                        String.join("; ", validateResult.getErrors()),
                        "INVALID_PRODUCT_DATA"
                    );
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Check if motorbike exists
        if (errorException == null) {
            try {
                SanPham sanPham = productRepository.findById(inputData.getMaSanPham())
                    .orElseThrow(() -> DomainException.productNotFound(inputData.getMaSanPham()));
                    
                if (!(sanPham instanceof XeMay)) {
                    throw DomainException.productNotMotorbike();
                }
                
                xeMay = (XeMay) sanPham;
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3.5: UC-52 - Check duplication ONLY if product name is changing
        if (errorException == null && xeMay != null) {
            try {
                String nameToCheck = (inputData.getTenSanPham() != null && !xeMay.getTenSanPham().equals(inputData.getTenSanPham())) 
                    ? inputData.getTenSanPham() : null;
                
                if (nameToCheck != null) {
                    CheckProductDuplicationInputData checkDupInput = new CheckProductDuplicationInputData(
                        nameToCheck,
                        null, // productCode - not used for motorbikes
                        inputData.getMaSanPham() // Exclude current product from duplication check
                    );
                    var dupResult = ((CheckProductDuplicationUseCaseControl) checkDuplicationUseCase)
                        .checkInternal(checkDupInput);
                    
                    if (dupResult.isDuplicate()) {
                        throw DomainException.productAlreadyExists(inputData.getTenSanPham());
                    }
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Update motorbike entity
        if (errorException == null && xeMay != null) {
            try {
                xeMay.setTenSanPham(inputData.getTenSanPham());
                xeMay.setMoTa(inputData.getMoTa());
                xeMay.capNhatGia(inputData.getGia());
                xeMay.setHinhAnh(inputData.getHinhAnh());
                xeMay.setSoLuongTonKho(inputData.getSoLuongTonKho());
                xeMay.setHangXe(inputData.getHangXe());
                xeMay.setDongXe(inputData.getDongXe());
                xeMay.setMauSac(inputData.getMauSac());
                xeMay.setNamSanXuat(inputData.getNamSanXuat());
                xeMay.setDungTich(inputData.getDungTich());
                
                xeMay = (XeMay) productRepository.save(xeMay);
                
                outputData = UpdateMotorbikeOutputData.forSuccess(
                    xeMay.getMaSanPham(),
                    xeMay.getTenSanPham(),
                    xeMay.getHangXe(),
                    xeMay.getDongXe(),
                    xeMay.getMauSac(),
                    xeMay.getNamSanXuat(),
                    xeMay.getDungTich(),
                    xeMay.getGia(),
                    xeMay.getNgayCapNhat()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = UpdateMotorbikeOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 5: Present result
        outputBoundary.present(outputData);
    }
    
    private void validateMotorbikeFields(UpdateMotorbikeInputData inputData) {
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

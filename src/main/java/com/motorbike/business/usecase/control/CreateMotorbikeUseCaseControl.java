package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.CreateMotorbikeInputData;
import com.motorbike.business.dto.motorbike.CreateMotorbikeOutputData;
import com.motorbike.business.dto.validateproductdata.ValidateProductDataInputData;
import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.AddMotorbikeInputBoundary;
import com.motorbike.business.usecase.output.CreateMotorbikeOutputBoundary;
import com.motorbike.business.usecase.input.ValidateProductDataInputBoundary;
import com.motorbike.business.usecase.input.CheckProductDuplicationInputBoundary;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.exceptions.*;
import com.motorbike.business.dto.motorbike.AddMotorbikeInputData;

public class CreateMotorbikeUseCaseControl implements AddMotorbikeInputBoundary {
    
    private final CreateMotorbikeOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final ValidateProductDataInputBoundary validateProductDataUseCase;
    private final CheckProductDuplicationInputBoundary checkDuplicationUseCase;
    
    public CreateMotorbikeUseCaseControl(
            CreateMotorbikeOutputBoundary outputBoundary,
            ProductRepository productRepository,
            ValidateProductDataInputBoundary validateProductDataUseCase,
            CheckProductDuplicationInputBoundary checkDuplicationUseCase) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.validateProductDataUseCase = validateProductDataUseCase;
        this.checkDuplicationUseCase = checkDuplicationUseCase;
    }
    
    // Constructor for tests with 2 params
    public CreateMotorbikeUseCaseControl(
            CreateMotorbikeOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.validateProductDataUseCase = new ValidateProductDataUseCaseControl(null);
        this.checkDuplicationUseCase = new CheckProductDuplicationUseCaseControl(null, productRepository);
    }
    
    @Override
    public void execute(AddMotorbikeInputData inputData) {
        if (inputData == null) {
            throw ValidationException.invalidInput();
        }
        // Convert AddMotorbikeInputData to CreateMotorbikeInputData
        CreateMotorbikeInputData createInput = new CreateMotorbikeInputData(
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
        executeInternal(createInput);
    }
    
    private void executeInternal(CreateMotorbikeInputData inputData) {
        CreateMotorbikeOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Basic validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: UC-51 - Validate product data
        if (errorException == null) {
            try {
                ValidateProductDataInputData validateInput = new ValidateProductDataInputData(
                    inputData.getTenSanPham(),
                    null, // productCode - not used for motorbike creation
                    inputData.getGia(),
                    inputData.getSoLuongTonKho(),
                    "xe_may" // category
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
        
        // Step 3: UC-52 - Check product duplication
        if (errorException == null) {
            try {
                CheckProductDuplicationInputData checkInput = new CheckProductDuplicationInputData(
                    inputData.getTenSanPham(),
                    null, // productCode - not used for motorbike
                    null  // excludeProductId - not applicable for creation
                );
                var duplicationResult = ((CheckProductDuplicationUseCaseControl) checkDuplicationUseCase)
                    .checkInternal(checkInput);
                
                if (!duplicationResult.isSuccess()) {
                    throw new DomainException(duplicationResult.getErrorMessage(), duplicationResult.getErrorCode());
                }
                
                if (duplicationResult.isDuplicate()) {
                    throw DomainException.productAlreadyExists(inputData.getTenSanPham());
                }
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Create motorbike entity (core business logic)
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
        
        // Step 5: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = CreateMotorbikeOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 6: Present result
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

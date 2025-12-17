package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.AddAccessoryInputData;
import com.motorbike.business.dto.accessory.CreateAccessoryInputData;
import com.motorbike.business.dto.accessory.CreateAccessoryOutputData;
import com.motorbike.business.dto.validateproductdata.ValidateProductDataInputData;
import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.AddAccessoryInputBoundary;
import com.motorbike.business.usecase.output.CreateAccessoryOutputBoundary;
import com.motorbike.business.usecase.input.ValidateProductDataInputBoundary;
import com.motorbike.business.usecase.input.CheckProductDuplicationInputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.exceptions.*;

public class CreateAccessoryUseCaseControl implements AddAccessoryInputBoundary {
    
    private final CreateAccessoryOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final ValidateProductDataInputBoundary validateProductDataUseCase;
    private final CheckProductDuplicationInputBoundary checkDuplicationUseCase;
    
    public CreateAccessoryUseCaseControl(
            CreateAccessoryOutputBoundary outputBoundary,
            ProductRepository productRepository,
            ValidateProductDataInputBoundary validateProductDataUseCase,
            CheckProductDuplicationInputBoundary checkDuplicationUseCase) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.validateProductDataUseCase = validateProductDataUseCase;
        this.checkDuplicationUseCase = checkDuplicationUseCase;
    }
    
    // Constructor for tests with 2 params
    public CreateAccessoryUseCaseControl(
            CreateAccessoryOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.validateProductDataUseCase = new ValidateProductDataUseCaseControl(null);
        this.checkDuplicationUseCase = new CheckProductDuplicationUseCaseControl(null, productRepository);
    }
    
    @Override
    public void execute(AddAccessoryInputData inputData) {
        if (inputData == null) {
            throw ValidationException.invalidInput();
        }
        // Convert AddAccessoryInputData to CreateAccessoryInputData
        CreateAccessoryInputData createInput = new CreateAccessoryInputData(
            inputData.getTenSanPham(),
            inputData.getMoTa(),
            inputData.getGia(),
            inputData.getHinhAnh(),
            inputData.getSoLuongTonKho(),
            inputData.getNhaCungCap(),
            inputData.getXuatXu(),
            inputData.getBaoHanh()
        );
        executeInternal(createInput);
    }
    
    private void executeInternal(CreateAccessoryInputData inputData) {
        CreateAccessoryOutputData outputData = null;
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
                    null,
                    inputData.getGia(),
                    inputData.getSoLuongTonKho(),
                    "phu_tung"
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
                    null,
                    null
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
        
        // Step 4: Create accessory entity
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
        
        // Step 5: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = CreateAccessoryOutputData.forError(errorCode, errorException.getMessage());
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

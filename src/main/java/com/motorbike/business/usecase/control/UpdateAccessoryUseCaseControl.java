package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.UpdateAccessoryInputData;
import com.motorbike.business.dto.accessory.UpdateAccessoryOutputData;
import com.motorbike.business.dto.validateproductdata.ValidateProductDataInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.UpdateAccessoryInputBoundary;
import com.motorbike.business.usecase.input.ValidateProductDataInputBoundary;
import com.motorbike.business.usecase.output.UpdateAccessoryOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.*;

public class UpdateAccessoryUseCaseControl implements UpdateAccessoryInputBoundary {

    private final UpdateAccessoryOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final ValidateProductDataInputBoundary validateProductDataUseCase;

    public UpdateAccessoryUseCaseControl(
            UpdateAccessoryOutputBoundary outputBoundary,
            ProductRepository productRepository,
            ValidateProductDataInputBoundary validateProductDataUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.validateProductDataUseCase = validateProductDataUseCase;
    }

    // Constructor with 2 parameters (for backward compatibility)
    public UpdateAccessoryUseCaseControl(
            UpdateAccessoryOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.validateProductDataUseCase = new ValidateProductDataUseCaseControl(null);
    }

    @Override
    public void execute(UpdateAccessoryInputData inputData) {
        UpdateAccessoryOutputData outputData = null;
        Exception errorException = null;
        PhuKienXeMay phuKien = null;

        // Step 1: Basic validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            if (inputData.getMaSanPham() == null) {
                throw ValidationException.nullProductId();
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

        // Step 3: Check if accessory exists and update
        if (errorException == null) {
            try {
                SanPham sanPham = productRepository.findById(inputData.getMaSanPham())
                        .orElseThrow(() -> DomainException.productNotFound(inputData.getMaSanPham()));
                        
                if (!(sanPham instanceof PhuKienXeMay)) {
                    throw DomainException.productNotAccessory();
                }
                
                phuKien = (PhuKienXeMay) sanPham;

                // Cập nhật thông tin
                phuKien.setTenSanPham(inputData.getTenSanPham());
                phuKien.setMoTa(inputData.getMoTa());
                phuKien.capNhatGia(inputData.getGia());
                phuKien.setHinhAnh(inputData.getHinhAnh());
                phuKien.setSoLuongTonKho(inputData.getSoLuongTonKho());
                phuKien.setLoaiPhuKien(inputData.getLoaiPhuKien());
                phuKien.setThuongHieu(inputData.getThuongHieu());
                phuKien.setChatLieu(inputData.getChatLieu());
                phuKien.setKichThuoc(inputData.getKichThuoc());

                phuKien = (PhuKienXeMay) productRepository.save(phuKien);

                outputData = UpdateAccessoryOutputData.forSuccess(
                        phuKien.getMaSanPham(),
                        phuKien.getTenSanPham(),
                        phuKien.getLoaiPhuKien(),
                        phuKien.getThuongHieu(),
                        phuKien.getChatLieu(),
                        phuKien.getKichThuoc(),
                        phuKien.getGia(),
                        phuKien.getNgayCapNhat()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 4: Handle error
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();

            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            } else if (errorException instanceof SystemException) {
                errorCode = ((SystemException) errorException).getErrorCode();
            }

            outputData = UpdateAccessoryOutputData.forError(errorCode, message);
        }

        // Step 5: Present result
        outputBoundary.present(outputData);
    }
}

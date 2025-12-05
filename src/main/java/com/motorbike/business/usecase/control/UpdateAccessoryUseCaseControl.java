package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.UpdateAccessoryInputData;
import com.motorbike.business.dto.accessory.UpdateAccessoryOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.UpdateAccessoryInputBoundary;
import com.motorbike.business.usecase.output.UpdateAccessoryOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.*;

public class UpdateAccessoryUseCaseControl implements UpdateAccessoryInputBoundary {

    private final UpdateAccessoryOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public UpdateAccessoryUseCaseControl(
            UpdateAccessoryOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }

    @Override
    public void execute(UpdateAccessoryInputData inputData) {
        UpdateAccessoryOutputData outputData = null;
        Exception errorException = null;
        PhuKienXeMay phuKien = null;

        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            if (inputData.getMaSanPham() == null) {
                throw ValidationException.nullProductId();
            }
            
            // Validation
            SanPham.validateTenSanPham(inputData.getTenSanPham());
            SanPham.validateGia(inputData.getGia());
            SanPham.validateSoLuongTonKho(inputData.getSoLuongTonKho());
            
        } catch (Exception e) {
            errorException = e;
        }

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
                phuKien.tangTonKho(inputData.getSoLuongTonKho() - phuKien.getSoLuongTonKho());
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

        outputBoundary.present(outputData);
    }
}

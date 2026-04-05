package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.AddAccessoryInputData;
import com.motorbike.business.dto.accessory.AddAccessoryOutputData;
import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.business.usecase.input.AddAccessoryInputBoundary;
import com.motorbike.business.usecase.output.AddAccessoryOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.*;

public class AddAccessoryUseCaseControl implements AddAccessoryInputBoundary {

    private final AddAccessoryOutputBoundary outputBoundary;
    private final AccessoryRepository accessoryRepository;

    public AddAccessoryUseCaseControl(
            AddAccessoryOutputBoundary outputBoundary,
            AccessoryRepository accessoryRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.accessoryRepository = accessoryRepository;
    }

    @Override
    public void execute(AddAccessoryInputData inputData) {
        AddAccessoryOutputData outputData = null;
        Exception errorException = null;
        PhuKienXeMay phuKien = null;

        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
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
                phuKien = new PhuKienXeMay(
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

                phuKien = accessoryRepository.save(phuKien);

                outputData = AddAccessoryOutputData.forSuccess(
                        phuKien.getMaSanPham(),
                        phuKien.getTenSanPham(),
                        phuKien.getLoaiPhuKien(),
                        phuKien.getThuongHieu(),
                        phuKien.getChatLieu(),
                        phuKien.getKichThuoc(),
                        phuKien.getGia(),
                        phuKien.getNgayTao()
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

            outputData = AddAccessoryOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}

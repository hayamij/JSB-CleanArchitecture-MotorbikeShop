package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.FormatAccessoriesForDisplayInputData;
import com.motorbike.business.dto.accessory.FormatAccessoriesForDisplayOutputData;
import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData.AccessoryItem;
import com.motorbike.business.usecase.input.FormatAccessoriesForDisplayInputBoundary;
import com.motorbike.business.usecase.output.FormatAccessoriesForDisplayOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UC-76: Format Accessories For Display
 * Secondary UseCase that maps PhuKienXeMay entities to AccessoryItem DTOs
 * Used by: GetAllAccessories, SearchAccessories
 */
public class FormatAccessoriesForDisplayUseCaseControl implements FormatAccessoriesForDisplayInputBoundary {

    private final FormatAccessoriesForDisplayOutputBoundary outputBoundary;

    public FormatAccessoriesForDisplayUseCaseControl(FormatAccessoriesForDisplayOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(FormatAccessoriesForDisplayInputData inputData) {
        FormatAccessoriesForDisplayOutputData outputData = formatInternal(inputData);
        if (outputBoundary != null) {
            outputBoundary.present(outputData);
        }
    }

    /**
     * Internal method for direct delegation from parent usecases
     */
    public FormatAccessoriesForDisplayOutputData formatInternal(FormatAccessoriesForDisplayInputData inputData) {
        try {
            if (inputData == null || inputData.getAccessories() == null) {
                return FormatAccessoriesForDisplayOutputData.forError("INVALID_INPUT", "Input data cannot be null");
            }

            List<AccessoryItem> items = inputData.getAccessories().stream()
                    .map(pk -> new AccessoryItem(
                            pk.getMaSanPham(),
                            pk.getTenSanPham(),
                            pk.getMoTa(),
                            pk.getGia(),
                            pk.getSoLuongTonKho(),
                            pk.getHinhAnh(),
                            pk.getLoaiPhuKien(),
                            pk.getThuongHieu(),
                            pk.getChatLieu(),
                            pk.getKichThuoc()
                    ))
                    .collect(Collectors.toList());

            return FormatAccessoriesForDisplayOutputData.forSuccess(items);
        } catch (Exception e) {
            return FormatAccessoriesForDisplayOutputData.forError("FORMAT_ERROR", e.getMessage());
        }
    }
}

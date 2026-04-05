package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.FormatMotorbikesForDisplayInputData;
import com.motorbike.business.dto.motorbike.FormatMotorbikesForDisplayOutputData;
import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData.MotorbikeItem;
import com.motorbike.business.usecase.input.FormatMotorbikesForDisplayInputBoundary;
import com.motorbike.business.usecase.output.FormatMotorbikesForDisplayOutputBoundary;
import com.motorbike.domain.entities.XeMay;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UC-74: Format Motorbikes For Display
 * Secondary usecase to format motorbike entities to display DTOs
 */
public class FormatMotorbikesForDisplayUseCaseControl implements FormatMotorbikesForDisplayInputBoundary {

    private final FormatMotorbikesForDisplayOutputBoundary outputBoundary;

    public FormatMotorbikesForDisplayUseCaseControl(FormatMotorbikesForDisplayOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(FormatMotorbikesForDisplayInputData inputData) {
        FormatMotorbikesForDisplayOutputData outputData = formatInternal(inputData);
        if (outputBoundary != null) {
            outputBoundary.present(outputData);
        }
    }

    /**
     * Internal method for composition by other usecases
     */
    public FormatMotorbikesForDisplayOutputData formatInternal(FormatMotorbikesForDisplayInputData inputData) {
        try {
            List<MotorbikeItem> motorbikeItems = inputData.getMotorbikes().stream()
                    .map(x -> new MotorbikeItem(
                            x.getMaSanPham(),
                            x.getTenSanPham(),
                            x.getMoTa(),
                            x.getGia(),
                            x.getSoLuongTonKho(),
                            x.getHinhAnh(),
                            x.getHangXe(),
                            x.getDongXe(),
                            x.getMauSac(),
                            x.getNamSanXuat(),
                            x.getDungTich()
                    ))
                    .collect(Collectors.toList());

            return FormatMotorbikesForDisplayOutputData.forSuccess(motorbikeItems);
        } catch (Exception e) {
            return FormatMotorbikesForDisplayOutputData.forError(
                "FORMAT_ERROR", 
                "Lỗi khi format danh sách xe máy: " + e.getMessage()
            );
        }
    }
}

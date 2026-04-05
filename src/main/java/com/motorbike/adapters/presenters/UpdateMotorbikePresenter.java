package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.UpdateMotorbikeViewModel;
import com.motorbike.business.dto.motorbike.UpdateMotorbikeOutputData;
import com.motorbike.business.usecase.output.UpdateMotorbikeOutputBoundary;
import java.time.format.DateTimeFormatter;

public class UpdateMotorbikePresenter implements UpdateMotorbikeOutputBoundary {
    
    private final UpdateMotorbikeViewModel viewModel;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public UpdateMotorbikePresenter(UpdateMotorbikeViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(UpdateMotorbikeOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(UpdateMotorbikeOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maSanPham = outputData.getMaSanPham();
        viewModel.tenSanPham = outputData.getTenSanPham();
        viewModel.hangXe = outputData.getHangXe();
        viewModel.dongXe = outputData.getDongXe();
        viewModel.mauSac = outputData.getMauSac();
        viewModel.namSanXuat = outputData.getNamSanXuat();
        viewModel.dungTich = outputData.getDungTich();
        viewModel.gia = outputData.getGia();
        viewModel.ngayCapNhat = outputData.getNgayCapNhat() != null ? 
            outputData.getNgayCapNhat().format(FORMATTER) : null;
        viewModel.successMessage = "Cập nhật xe máy thành công: " + outputData.getTenSanPham();
    }
    
    private void presentError(UpdateMotorbikeOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(outputData);
    }
    
    private String formatErrorMessage(UpdateMotorbikeOutputData outputData) {
        String message = outputData.getErrorMessage();
        if (message == null || message.isEmpty()) {
            return "Có lỗi xảy ra khi cập nhật xe máy";
        }
        return message;
    }
}

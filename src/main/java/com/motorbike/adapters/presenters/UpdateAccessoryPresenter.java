package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.UpdateAccessoryViewModel;
import com.motorbike.business.dto.accessory.UpdateAccessoryOutputData;
import com.motorbike.business.usecase.output.UpdateAccessoryOutputBoundary;
import java.time.format.DateTimeFormatter;

public class UpdateAccessoryPresenter implements UpdateAccessoryOutputBoundary {
    
    private final UpdateAccessoryViewModel viewModel;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public UpdateAccessoryPresenter(UpdateAccessoryViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(UpdateAccessoryOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(UpdateAccessoryOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maSanPham = outputData.getMaSanPham();
        viewModel.tenSanPham = outputData.getTenSanPham();
        viewModel.loaiPhuKien = outputData.getLoaiPhuKien();
        viewModel.thuongHieu = outputData.getThuongHieu();
        viewModel.chatLieu = outputData.getChatLieu();
        viewModel.kichThuoc = outputData.getKichThuoc();
        viewModel.gia = outputData.getGia();
        viewModel.ngayCapNhat = outputData.getNgayCapNhat() != null ? 
            outputData.getNgayCapNhat().format(FORMATTER) : null;
        viewModel.successMessage = "Cập nhật phụ kiện thành công: " + outputData.getTenSanPham();
    }
    
    private void presentError(UpdateAccessoryOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(outputData);
    }
    
    private String formatErrorMessage(UpdateAccessoryOutputData outputData) {
        String message = outputData.getErrorMessage();
        if (message == null || message.isEmpty()) {
            return "Có lỗi xảy ra khi cập nhật phụ kiện";
        }
        return message;
    }
}

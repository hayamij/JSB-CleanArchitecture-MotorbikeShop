package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.AddAccessoryViewModel;
import com.motorbike.business.dto.accessory.AddAccessoryOutputData;
import com.motorbike.business.usecase.output.AddAccessoryOutputBoundary;
import java.time.format.DateTimeFormatter;

public class AddAccessoryPresenter implements AddAccessoryOutputBoundary {
    
    private final AddAccessoryViewModel viewModel;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public AddAccessoryPresenter(AddAccessoryViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(AddAccessoryOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(AddAccessoryOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maSanPham = outputData.getMaSanPham();
        viewModel.tenSanPham = outputData.getTenSanPham();
        viewModel.loaiPhuKien = outputData.getLoaiPhuKien();
        viewModel.thuongHieu = outputData.getThuongHieu();
        viewModel.chatLieu = outputData.getChatLieu();
        viewModel.kichThuoc = outputData.getKichThuoc();
        viewModel.gia = outputData.getGia();
        viewModel.ngayTao = outputData.getNgayTao() != null ? 
            outputData.getNgayTao().format(FORMATTER) : null;
        viewModel.successMessage = "Thêm phụ kiện thành công: " + outputData.getTenSanPham();
    }
    
    private void presentError(AddAccessoryOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(outputData);
    }
    
    private String formatErrorMessage(AddAccessoryOutputData outputData) {
        String message = outputData.getErrorMessage();
        if (message == null || message.isEmpty()) {
            return "Có lỗi xảy ra khi thêm phụ kiện";
        }
        return message;
    }
}

package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.UpdateUserViewModel;
import com.motorbike.business.dto.user.UpdateUserOutputData;
import com.motorbike.business.usecase.output.UpdateUserOutputBoundary;
import java.time.format.DateTimeFormatter;

public class UpdateUserPresenter implements UpdateUserOutputBoundary {
    
    private final UpdateUserViewModel viewModel;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public UpdateUserPresenter(UpdateUserViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(UpdateUserOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(UpdateUserOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maTaiKhoan = outputData.getMaTaiKhoan();
        viewModel.email = outputData.getEmail();
        viewModel.tenDangNhap = outputData.getTenDangNhap();
        viewModel.soDienThoai = outputData.getSoDienThoai();
        viewModel.vaiTro = outputData.getVaiTro();
        viewModel.hoatDong = outputData.isHoatDong();
        viewModel.ngayCapNhat = outputData.getNgayCapNhat() != null ? 
            outputData.getNgayCapNhat().format(FORMATTER) : null;
        viewModel.successMessage = "Cập nhật người dùng thành công: " + outputData.getTenDangNhap();
    }
    
    private void presentError(UpdateUserOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(outputData);
    }
    
    private String formatErrorMessage(UpdateUserOutputData outputData) {
        String message = outputData.getErrorMessage();
        if (message == null || message.isEmpty()) {
            return "Có lỗi xảy ra khi cập nhật người dùng";
        }
        return message;
    }
}

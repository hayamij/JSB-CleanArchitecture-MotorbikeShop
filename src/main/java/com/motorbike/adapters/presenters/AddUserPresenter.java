package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.AddUserViewModel;
import com.motorbike.business.dto.user.AddUserOutputData;
import com.motorbike.business.usecase.output.AddUserOutputBoundary;
import java.time.format.DateTimeFormatter;

public class AddUserPresenter implements AddUserOutputBoundary {
    
    private final AddUserViewModel viewModel;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public AddUserPresenter(AddUserViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(AddUserOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(AddUserOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maTaiKhoan = outputData.getMaTaiKhoan();
        viewModel.email = outputData.getEmail();
        viewModel.tenDangNhap = outputData.getTenDangNhap();
        viewModel.soDienThoai = outputData.getSoDienThoai();
        viewModel.vaiTro = outputData.getVaiTro();
        viewModel.hoatDong = outputData.isHoatDong();
        viewModel.ngayTao = outputData.getNgayTao() != null ? 
            outputData.getNgayTao().format(FORMATTER) : null;
        viewModel.successMessage = "Thêm người dùng thành công: " + outputData.getTenDangNhap();
    }
    
    private void presentError(AddUserOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(outputData);
    }
    
    private String formatErrorMessage(AddUserOutputData outputData) {
        String message = outputData.getErrorMessage();
        if (message == null || message.isEmpty()) {
            return "Có lỗi xảy ra khi thêm người dùng";
        }
        return message;
    }
}

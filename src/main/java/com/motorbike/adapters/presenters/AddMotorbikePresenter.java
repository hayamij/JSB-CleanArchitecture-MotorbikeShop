package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.AddMotorbikeViewModel;
import com.motorbike.business.dto.motorbike.AddMotorbikeOutputData;
import com.motorbike.business.usecase.output.AddMotorbikeOutputBoundary;
<<<<<<< HEAD
import java.time.format.DateTimeFormatter;

public class AddMotorbikePresenter implements AddMotorbikeOutputBoundary {
    
    private final AddMotorbikeViewModel viewModel;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public AddMotorbikePresenter(AddMotorbikeViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(AddMotorbikeOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(AddMotorbikeOutputData outputData) {
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
        viewModel.ngayTao = outputData.getNgayTao() != null ? 
            outputData.getNgayTao().format(FORMATTER) : null;
        viewModel.successMessage = "Thêm xe máy thành công: " + outputData.getTenSanPham();
    }
    
    private void presentError(AddMotorbikeOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(outputData);
    }
    
    private String formatErrorMessage(AddMotorbikeOutputData outputData) {
        String message = outputData.getErrorMessage();
        if (message == null || message.isEmpty()) {
            return "Có lỗi xảy ra khi thêm xe máy";
        }
        return message;
    }
=======

public class AddMotorbikePresenter implements AddMotorbikeOutputBoundary {

    private final AddMotorbikeViewModel viewModel;

    public AddMotorbikePresenter(AddMotorbikeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AddMotorbikeOutputData outputData) {

        if (outputData.hasError) {
            viewModel.hasError = true;
            viewModel.errorCode = outputData.errorCode;
            viewModel.errorMessage = outputData.errorMessage;
            viewModel.motorbike = null;
        } else {
            viewModel.hasError = false;
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
            viewModel.motorbike = outputData.motorbike; 
        }
    }
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
}

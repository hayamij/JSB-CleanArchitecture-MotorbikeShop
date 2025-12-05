package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updateorderinfor.UpdateOrderInforInputData;
import com.motorbike.business.dto.updateorderinfor.UpdateOrderInforOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.UpdateOrderInforOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.SystemException;
import com.motorbike.domain.exceptions.ValidationException;

public class UpdateOrderInforUseCaseControl {

    private final UpdateOrderInforOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;

    public UpdateOrderInforUseCaseControl(UpdateOrderInforOutputBoundary outputBoundary,
                                          OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
    }

    public void execute(UpdateOrderInforInputData inputData) {
        UpdateOrderInforOutputData outputData = null;
        Exception errorException = null;

        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }

            DonHang.checkInput(
                inputData.getUserId(),
                inputData.getReceiverName(),
                inputData.getPhoneNumber(),
                inputData.getShippingAddress()
            );

            if (inputData.getOrderId() == null) {
                throw ValidationException.nullOrderId();
            }
        } catch (Exception e) {
            errorException = e;
        }

        DonHang donHang = null;
        if (errorException == null) {
            try {
                donHang = orderRepository.findById(inputData.getOrderId())
                    .orElseThrow(() -> DomainException.cannotUpdateOrder(
                        "Không tìm thấy đơn hàng: " + inputData.getOrderId()
                    ));

                if (!donHang.getMaTaiKhoan().equals(inputData.getUserId())) {
                    throw DomainException.cannotUpdateOrder("Bạn không có quyền sửa đơn hàng này");
                }

                donHang.capNhatThongTinGiaoHang(
                    inputData.getReceiverName(),
                    inputData.getPhoneNumber(),
                    inputData.getShippingAddress(),
                    inputData.getNote()
                );

                DonHang saved = orderRepository.save(donHang);

                outputData = UpdateOrderInforOutputData.forSuccess(
                    saved.getMaDonHang(),
                    saved.getMaTaiKhoan(),
                    saved.getTenNguoiNhan(),
                    saved.getSoDienThoai(),
                    saved.getDiaChiGiaoHang(),
                    saved.getGhiChu(),
                    saved.getTrangThai().name(),
                    saved.getNgayCapNhat()
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

            outputData = UpdateOrderInforOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}

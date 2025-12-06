package com.motorbike.business.usecase.control;

import java.util.List;
import java.util.stream.Collectors;

import com.motorbike.business.dto.orderdetail.OrderDetailInputData;
import com.motorbike.business.dto.orderdetail.OrderDetailOutputData;
import com.motorbike.business.dto.orderdetail.OrderDetailOutputData.OrderData;
import com.motorbike.business.dto.orderdetail.OrderDetailOutputData.OrderItemData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.OrderDetailOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.SystemException;
import com.motorbike.domain.exceptions.ValidationException;

public class OrderDetailUseCaseControl {

    private final OrderDetailOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;

    public OrderDetailUseCaseControl(OrderDetailOutputBoundary outputBoundary,
                                     OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
    }

    public void execute(OrderDetailInputData inputData) {
        OrderDetailOutputData outputData = null;
        Exception errorException = null;

        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            if (inputData.getOrderId() == null) {
                throw ValidationException.nullOrderId();
            }
            if (!inputData.isAdmin() && inputData.getUserId() == null) {
                throw ValidationException.invalidUserId();
            }
        } catch (Exception e) {
            errorException = e;
        }

        DonHang donHang = null;
        if (errorException == null) {
            try {
                donHang = orderRepository.findById(inputData.getOrderId())
                        .orElseThrow(() -> DomainException.orderNotFound(inputData.getOrderId()));

                if (!inputData.isAdmin() && !donHang.getMaTaiKhoan().equals(inputData.getUserId())) {
                    throw DomainException.cannotUpdateOrder("Bạn không có quyền xem đơn hàng này");
                }
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException == null && donHang != null) {
            try {
                List<OrderItemData> items = donHang.getDanhSachSanPham().stream()
                    .map(chiTiet -> new OrderItemData(
                        chiTiet.getMaChiTiet(),
                        chiTiet.getMaSanPham(),
                        chiTiet.getTenSanPham(),
                        chiTiet.getGiaBan(),
                        chiTiet.getSoLuong(),
                        chiTiet.getThanhTien()
                    ))
                    .collect(Collectors.toList());

                int totalItems = items.size();
                int totalQuantity = items.stream().mapToInt(OrderItemData::getQuantity).sum();

                OrderData orderData = new OrderData(
                        donHang.getMaDonHang(),
                        donHang.getMaTaiKhoan(),
                        donHang.getTenNguoiNhan(),
                        donHang.getSoDienThoai(),
                        donHang.getDiaChiGiaoHang(),
                        donHang.getTrangThai().name(),
                        donHang.getTongTien(),
                        totalItems,
                        totalQuantity,
                        donHang.getGhiChu(),
                        donHang.getNgayDat(),
                        donHang.getNgayCapNhat(),
                        items
                );

                outputData = OrderDetailOutputData.forSuccess(orderData);
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

            outputData = OrderDetailOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}

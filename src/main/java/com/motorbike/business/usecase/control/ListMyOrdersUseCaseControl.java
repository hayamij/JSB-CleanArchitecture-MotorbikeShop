package com.motorbike.business.usecase.control;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.motorbike.business.dto.listmyorders.ListMyOrdersInputData;
import com.motorbike.business.dto.listmyorders.ListMyOrdersOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.ListMyOrdersOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.exceptions.ValidationException;

public class ListMyOrdersUseCaseControl {
    
    private final ListMyOrdersOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;

    public ListMyOrdersUseCaseControl(
            ListMyOrdersOutputBoundary outputBoundary,
            OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
    }

    public void execute(ListMyOrdersInputData inputData) {
        ListMyOrdersOutputData outputData = null;
        Exception errorException = null;

        try {
            if (inputData == null || inputData.getUserId() == null) {
                throw ValidationException.invalidUserId();
            }
        } catch (Exception e) {
            errorException = e;
        }

        List<DonHang> userOrders = null;
        if (errorException == null) {
            try {
                userOrders = orderRepository.findByUserId(inputData.getUserId());
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException == null && userOrders != null) {
            try {
                List<DonHang> sortedOrders = userOrders.stream()
                        .sorted(Comparator.comparing(DonHang::getNgayDat).reversed())
                        .collect(Collectors.toList());

                List<ListMyOrdersOutputData.OrderItemData> orderItems = sortedOrders.stream()
                        .map(donHang -> new ListMyOrdersOutputData.OrderItemData(
                                donHang.getMaDonHang(),
                                donHang.getMaTaiKhoan(),
                                donHang.getTenNguoiNhan(),
                                donHang.getSoDienThoai(),
                                donHang.getDiaChiGiaoHang(),
                                donHang.getTrangThai().name(),
                                donHang.getTongTien(),
                                donHang.getDanhSachSanPham().size(),
                                donHang.getDanhSachSanPham().stream()
                                        .mapToInt(item -> item.getSoLuong())
                                        .sum(),
                                donHang.getNgayDat(),
                                donHang.getGhiChu()
                        ))
                        .collect(Collectors.toList());

                outputData = ListMyOrdersOutputData.forSuccess(orderItems);
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();

            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            }

            outputData = ListMyOrdersOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}

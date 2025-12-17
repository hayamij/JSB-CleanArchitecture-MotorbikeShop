package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.dto.listallorders.ListAllOrdersOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;
import com.motorbike.business.dto.order.CalculateOrderTotalsInputData;
import com.motorbike.business.dto.order.FormatOrderForDisplayInputData;
import com.motorbike.business.usecase.input.CalculateOrderTotalsInputBoundary;
import com.motorbike.business.usecase.input.FormatOrderForDisplayInputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.business.usecase.input.ListAllOrdersInputBoundary;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListAllOrdersUseCaseControl implements ListAllOrdersInputBoundary {
    
    private final ListAllOrdersOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;
    private final CalculateOrderTotalsInputBoundary calculateOrderTotalsUseCase;
    private final FormatOrderForDisplayInputBoundary formatOrderForDisplayUseCase;

    public ListAllOrdersUseCaseControl(
            ListAllOrdersOutputBoundary outputBoundary,
            OrderRepository orderRepository,
            CalculateOrderTotalsInputBoundary calculateOrderTotalsUseCase,
            FormatOrderForDisplayInputBoundary formatOrderForDisplayUseCase) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.calculateOrderTotalsUseCase = calculateOrderTotalsUseCase;
        this.formatOrderForDisplayUseCase = formatOrderForDisplayUseCase;
    }

    public ListAllOrdersUseCaseControl(
            ListAllOrdersOutputBoundary outputBoundary,
            OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.calculateOrderTotalsUseCase = null;
        this.formatOrderForDisplayUseCase = null;
    }

    public void execute(ListAllOrdersInputData inputData) {
        ListAllOrdersOutputData outputData = null;
        Exception errorException = null;

        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }

        List<DonHang> allOrders = null;
        if (errorException == null) {
            try {
                allOrders = orderRepository.findAll();
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException == null && allOrders != null) {
            try {
                List<DonHang> sortedOrders = allOrders.stream()
                        .sorted(Comparator.comparing(DonHang::getNgayDat).reversed())
                        .collect(Collectors.toList());

                BigDecimal totalRevenue = sortedOrders.stream()
                        .map(DonHang::getTongTien)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                List<ListAllOrdersOutputData.OrderItemData> orderItems = sortedOrders.stream()
                        .map(donHang -> new ListAllOrdersOutputData.OrderItemData(
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
                                donHang.getGhiChu(),
                                donHang.getPhuongThucThanhToan().name()
                        ))
                        .collect(Collectors.toList());

                outputData = ListAllOrdersOutputData.forSuccess(orderItems);
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

            outputData = ListAllOrdersOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}

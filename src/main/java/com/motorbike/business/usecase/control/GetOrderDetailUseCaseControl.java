package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.order.GetOrderDetailInputData;
import com.motorbike.business.dto.order.GetOrderDetailOutputData;
import com.motorbike.business.dto.order.GetOrderDetailOutputData.OrderDetailInfo;
import com.motorbike.business.dto.order.GetOrderDetailOutputData.OrderItemInfo;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.input.GetOrderDetailInputBoundary;
import com.motorbike.business.usecase.output.GetOrderDetailOutputBoundary;
import com.motorbike.business.dto.order.CalculateOrderTotalsInputData;
import com.motorbike.business.dto.order.FormatOrderForDisplayInputData;
import com.motorbike.business.usecase.input.CalculateOrderTotalsInputBoundary;
import com.motorbike.business.usecase.input.FormatOrderForDisplayInputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GetOrderDetailUseCaseControl implements GetOrderDetailInputBoundary {

    private final GetOrderDetailOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;
    private final CalculateOrderTotalsInputBoundary calculateOrderTotalsUseCase;
    private final FormatOrderForDisplayInputBoundary formatOrderForDisplayUseCase;

    public GetOrderDetailUseCaseControl(
            GetOrderDetailOutputBoundary outputBoundary,
            OrderRepository orderRepository,
            CalculateOrderTotalsInputBoundary calculateOrderTotalsUseCase,
            FormatOrderForDisplayInputBoundary formatOrderForDisplayUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.calculateOrderTotalsUseCase = calculateOrderTotalsUseCase;
        this.formatOrderForDisplayUseCase = formatOrderForDisplayUseCase;
    }

    public GetOrderDetailUseCaseControl(
            GetOrderDetailOutputBoundary outputBoundary,
            OrderRepository orderRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.calculateOrderTotalsUseCase = null;
        this.formatOrderForDisplayUseCase = null;
    }

    @Override
    public void execute(GetOrderDetailInputData inputData) {
        GetOrderDetailOutputData outputData = null;
        Exception errorException = null;

        // Validation
        try {
            if (inputData == null || inputData.getOrderId() == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Business logic
        if (errorException == null) {
            try {
                Optional<DonHang> orderOpt = orderRepository.findById(inputData.getOrderId());
                
                if (!orderOpt.isPresent()) {
                    throw new ValidationException("ORDER_NOT_FOUND", "Không tìm thấy đơn hàng");
                }
                
                DonHang order = orderOpt.get();
                
                // UC-49: Calculate order totals
                CalculateOrderTotalsInputData totalsInput = new CalculateOrderTotalsInputData(
                    order.getDanhSachSanPham()
                );
                var totalsResult = ((CalculateOrderTotalsUseCaseControl) calculateOrderTotalsUseCase)
                    .calculateInternal(totalsInput);
                
                // UC-50: Format order for display
                FormatOrderForDisplayInputData formatInput = new FormatOrderForDisplayInputData(order);
                var formatResult = ((FormatOrderForDisplayUseCaseControl) formatOrderForDisplayUseCase)
                    .formatInternal(formatInput);
                
                // Map order items
                List<OrderItemInfo> items = order.getDanhSachSanPham().stream()
                        .map(item -> new OrderItemInfo(
                                item.getMaSanPham(),
                                item.getTenSanPham(),
                                item.getSoLuong(),
                                item.getGiaBan(),
                                item.getThanhTien()
                        ))
                        .collect(Collectors.toList());
                
                // Create OrderDetailInfo with formatted data
                OrderDetailInfo orderDetail = new OrderDetailInfo(
                        order.getMaDonHang(),
                        order.getMaTaiKhoan(),
                        order.getTenNguoiNhan(),
                        order.getSoDienThoai(),
                        order.getDiaChiGiaoHang(),
                        formatResult.getFormattedStatus(),
                        order.getTrangThai().name(),
                        totalsResult.getTotalAmount(),
                        formatResult.getFormattedOrderDate(),
                        order.getGhiChu(),
                        formatResult.getFormattedPaymentMethod(),
                        items
                );
                
                outputData = GetOrderDetailOutputData.forSuccess(orderDetail);
            } catch (Exception e) {
                errorException = e;
            }
        }

        // Error handling
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException 
                    ? ((ValidationException) errorException).getErrorCode() 
                    : "GET_ORDER_DETAIL_ERROR";
            outputData = GetOrderDetailOutputData.forError(errorCode, errorException.getMessage());
        }

        // Present
        outputBoundary.present(outputData);
    }
}

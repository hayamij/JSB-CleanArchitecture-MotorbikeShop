package com.motorbike.business.usecase.control;

import java.util.List;
import java.util.stream.Collectors;

import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.dto.listallorders.ListAllOrdersOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;
import com.motorbike.domain.entities.DonHang;

public class ListAllOrdersUseCaseControl
        extends AbstractUseCaseControl<ListAllOrdersInputData, ListAllOrdersOutputBoundary> {
    
    private final OrderRepository orderRepository;

    public ListAllOrdersUseCaseControl(
            ListAllOrdersOutputBoundary outputBoundary,
            OrderRepository orderRepository) {
        super(outputBoundary);
        this.orderRepository = orderRepository;
    }

    @Override
    protected void executeBusinessLogic(ListAllOrdersInputData inputData) throws Exception {
        try {
           // ❗ Lấy tất cả đơn hàng, không lọc, không sort, không phân trang
        List<DonHang> allOrders = orderRepository.findAll();

        // ❗ Convert domain → output data
        List<ListAllOrdersOutputData.OrderItemData> orderItems = allOrders.stream()
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
                        donHang.getGhiChu()
                ))
                .collect(Collectors.toList());

        ListAllOrdersOutputData outputData = ListAllOrdersOutputData.forSuccess(orderItems);

        outputBoundary.present(outputData);

        } catch (Exception e) {
            throw e;
        }
    }

    

    @Override
    protected void validateInput(ListAllOrdersInputData inputData) {
        checkInputNotNull(inputData);
    }

    @Override
    protected void handleValidationError(IllegalArgumentException e) {
        ListAllOrdersOutputData outputData = ListAllOrdersOutputData.forError(
                "INVALID_INPUT",
                e.getMessage()
        );
        outputBoundary.present(outputData);
    }

    @Override
    protected void handleSystemError(Exception e) {
        ListAllOrdersOutputData outputData = ListAllOrdersOutputData.forError(
                "SYSTEM_ERROR",
                "Lỗi hệ thống: " + e.getMessage()
        );
        outputBoundary.present(outputData);
    }
}

package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.dto.listallorders.ListAllOrdersOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

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
            List<DonHang> allOrders;

            if (inputData.hasStatusFilter()) {
                TrangThaiDonHang status = TrangThaiDonHang.valueOf(inputData.getFilterStatus());
                allOrders = orderRepository.findByStatus(status);
            } else {
                allOrders = orderRepository.findAll();
            }

            allOrders = applySorting(allOrders, inputData.getSortBy());

            BigDecimal totalRevenue = calculateTotalRevenue(allOrders);

            int totalOrders = allOrders.size();
            int offset = inputData.getOffset();
            int pageSize = inputData.getPageSize();

            List<DonHang> paginatedOrders = allOrders.stream()
                    .skip(offset)
                    .limit(pageSize)
                    .collect(Collectors.toList());

            List<ListAllOrdersOutputData.OrderItemData> orderItems = paginatedOrders.stream()
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

            ListAllOrdersOutputData outputData = ListAllOrdersOutputData.forSuccess(
                    orderItems,
                    totalOrders,
                    inputData.getPage(),
                    pageSize,
                    totalRevenue
            );

            outputBoundary.present(outputData);

        } catch (Exception e) {
            throw e;
        }
    }

    
    private List<DonHang> applySorting(List<DonHang> orders, String sortBy) {
        if (sortBy == null) sortBy = "date_desc";

        switch (sortBy) {
            case "date_asc":
                return orders.stream()
                        .sorted(Comparator.comparing(DonHang::getNgayDat))
                        .collect(Collectors.toList());
            case "date_desc":
                return orders.stream()
                        .sorted(Comparator.comparing(DonHang::getNgayDat).reversed())
                        .collect(Collectors.toList());
            case "amount_asc":
                return orders.stream()
                        .sorted(Comparator.comparing(DonHang::getTongTien))
                        .collect(Collectors.toList());
            case "amount_desc":
                return orders.stream()
                        .sorted(Comparator.comparing(DonHang::getTongTien).reversed())
                        .collect(Collectors.toList());
            default:
                return orders;
        }
    }

    
    private BigDecimal calculateTotalRevenue(List<DonHang> orders) {
        return orders.stream()
                .map(DonHang::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    protected void validateInput(ListAllOrdersInputData inputData) {
        checkInputNotNull(inputData);

        if (inputData.getPage() < 0) {
            throw new IllegalArgumentException("Page phải >= 0");
        }

        if (inputData.getPageSize() <= 0 || inputData.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size phải từ 1 đến 100");
        }
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

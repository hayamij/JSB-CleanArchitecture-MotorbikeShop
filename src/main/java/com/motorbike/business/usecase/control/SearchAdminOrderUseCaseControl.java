package com.motorbike.business.usecase.control;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.motorbike.business.dto.searchadminorder.SearchAdminOrderInputData;
import com.motorbike.business.dto.searchadminorder.SearchAdminOrderOutputData;
import com.motorbike.business.dto.searchadminorder.SearchAdminOrderOutputData.OrderItemData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.SearchAdminOrderOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.exceptions.ValidationException;

public class SearchAdminOrderUseCaseControl {
    
    private final SearchAdminOrderOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;

    public SearchAdminOrderUseCaseControl(SearchAdminOrderOutputBoundary outputBoundary,
                                         OrderRepository orderRepository) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
    }

    public void execute(SearchAdminOrderInputData inputData) {
        SearchAdminOrderOutputData outputData = null;
        Exception errorException = null;

        try {
            if (inputData == null || !inputData.isAdmin()) {
                throw ValidationException.invalidInput();
            }
            if (inputData.getSearchQuery() == null || inputData.getSearchQuery().trim().isEmpty()) {
                throw new ValidationException("Vui lòng nhập từ khóa tìm kiếm", "INVALID_SEARCH_QUERY");
            }
        } catch (Exception e) {
            errorException = e;
        }

        List<DonHang> matchedOrders = null;
        if (errorException == null) {
            try {
                String searchQuery = inputData.getSearchQuery().trim();
                matchedOrders = orderRepository.searchForAdmin(searchQuery);
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException == null && matchedOrders != null) {
            try {
                List<OrderItemData> orderItems = matchedOrders.stream()
                    .sorted(Comparator.comparing(DonHang::getNgayDat).reversed())
                    .map(donHang -> new OrderItemData(
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

                outputData = SearchAdminOrderOutputData.forSuccess(orderItems);
            } catch (Exception e) {
                errorException = e;
            }
        }

        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();

            if (errorException instanceof ValidationException) {
                ValidationException ve = (ValidationException) errorException;
                errorCode = ve.getErrorCode();
                message = ve.getMessage();
            }

            outputData = SearchAdminOrderOutputData.forError(errorCode, message);
        }

        outputBoundary.present(outputData);
    }
}

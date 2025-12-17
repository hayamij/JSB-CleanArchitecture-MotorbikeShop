package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.order.SearchOrdersInputData;
import com.motorbike.business.dto.order.SearchOrdersOutputData;
import com.motorbike.business.dto.order.SearchOrdersOutputData.OrderItem;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.SearchOrdersOutputBoundary;
import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaInputData;
import com.motorbike.business.dto.applysearchfilters.ApplySearchFiltersInputData;
import com.motorbike.business.dto.sortsearchresults.SortSearchResultsInputData;
import com.motorbike.business.usecase.input.BuildSearchCriteriaInputBoundary;
import com.motorbike.business.usecase.input.ApplySearchFiltersInputBoundary;
import com.motorbike.business.usecase.input.SortSearchResultsInputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TaiKhoan;

import java.util.List;
import java.util.stream.Collectors;

public class SearchOrdersUseCaseControl {

    private final SearchOrdersOutputBoundary outputBoundary;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BuildSearchCriteriaInputBoundary buildSearchCriteriaUseCase;
    private final ApplySearchFiltersInputBoundary applySearchFiltersUseCase;
    private final SortSearchResultsInputBoundary sortSearchResultsUseCase;

    public SearchOrdersUseCaseControl(
            SearchOrdersOutputBoundary outputBoundary,
            OrderRepository orderRepository,
            UserRepository userRepository,
            BuildSearchCriteriaInputBoundary buildSearchCriteriaUseCase,
            ApplySearchFiltersInputBoundary applySearchFiltersUseCase,
            SortSearchResultsInputBoundary sortSearchResultsUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.buildSearchCriteriaUseCase = buildSearchCriteriaUseCase;
        this.applySearchFiltersUseCase = applySearchFiltersUseCase;
        this.sortSearchResultsUseCase = sortSearchResultsUseCase;
    }

    // Constructor with 3 parameters (for backward compatibility)
    public SearchOrdersUseCaseControl(
            SearchOrdersOutputBoundary outputBoundary,
            OrderRepository orderRepository,
            UserRepository userRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.buildSearchCriteriaUseCase = new BuildSearchCriteriaUseCaseControl(null);
        this.applySearchFiltersUseCase = new ApplySearchFiltersUseCaseControl(null);
        this.sortSearchResultsUseCase = new SortSearchResultsUseCaseControl(null);
    }

    public void execute(SearchOrdersInputData inputData) {
        SearchOrdersOutputData outputData = null;
        Exception errorException = null;

        try {
            List<DonHang> orders = orderRepository.searchOrders(inputData.getKeyword());

            // Filter by user ID if specified
            if (inputData.getMaTaiKhoan() != null) {
                orders = orders.stream()
                        .filter(dh -> dh.getMaTaiKhoan().equals(inputData.getMaTaiKhoan()))
                        .collect(Collectors.toList());
            }

            // Filter by status if specified
            if (inputData.getTrangThai() != null && !inputData.getTrangThai().isEmpty()) {
                orders = orders.stream()
                        .filter(dh -> dh.getTrangThai().name().equalsIgnoreCase(inputData.getTrangThai()))
                        .collect(Collectors.toList());
            }

            List<OrderItem> items = orders.stream()
                    .map(dh -> {
                        String emailKhachHang = userRepository.findById(dh.getMaTaiKhoan())
                                .map(TaiKhoan::getEmail)
                                .orElse("N/A");
                        
                        Integer soMatHang = dh.getDanhSachSanPham() != null 
                                ? dh.getDanhSachSanPham().size() 
                                : 0;
                        
                        List<SearchOrdersOutputData.ProductItem> sanPham = dh.getDanhSachSanPham().stream()
                                .map(ct -> new SearchOrdersOutputData.ProductItem(
                                        ct.getMaSanPham(),
                                        ct.getTenSanPham(),
                                        ct.getGiaBan(),
                                        ct.getSoLuong(),
                                        ct.getThanhTien()
                                ))
                                .collect(Collectors.toList());
                        
                        return new OrderItem(
                                dh.getMaDonHang(),
                                dh.getMaTaiKhoan(),
                                emailKhachHang,
                                dh.getTenNguoiNhan(),
                                dh.getSoDienThoai(),
                                dh.getDiaChiGiaoHang(),
                                dh.getTongTien(),
                                dh.getTrangThai().name(),
                                dh.getNgayDat(),
                                dh.getNgayCapNhat(),
                                soMatHang,
                                sanPham
                        );
                    })
                    .collect(Collectors.toList());

            outputData = new SearchOrdersOutputData(items);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new SearchOrdersOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}

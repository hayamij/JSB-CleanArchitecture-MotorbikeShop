package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.sortsearchresults.SortSearchResultsInputData;
import com.motorbike.business.dto.sortsearchresults.SortSearchResultsOutputData;
import com.motorbike.business.usecase.input.SortSearchResultsInputBoundary;
import com.motorbike.business.usecase.output.SortSearchResultsOutputBoundary;
import com.motorbike.domain.entities.*;
import com.motorbike.domain.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortSearchResultsUseCaseControl implements SortSearchResultsInputBoundary {
    private final SortSearchResultsOutputBoundary outputBoundary;

    public SortSearchResultsUseCaseControl(SortSearchResultsOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(SortSearchResultsInputData inputData) {
        SortSearchResultsOutputData outputData = sortInternal(inputData);
        outputBoundary.present(outputData);
    }

    public SortSearchResultsOutputData sortInternal(SortSearchResultsInputData inputData) {
        SortSearchResultsOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("SortSearchResults");
            }
            if (inputData.getResults() == null) {
                throw ValidationException.invalidInput("Results are required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - sort search results
        if (errorException == null) {
            try {
                List<?> results = inputData.getResults();
                String sortBy = inputData.getSortBy();
                SortSearchResultsInputData.SortDirection direction = inputData.getDirection();

                // If no sortBy specified, return results as-is
                if (sortBy == null || sortBy.trim().isEmpty() || results.isEmpty()) {
                    outputData = new SortSearchResultsOutputData(results, sortBy, direction.name());
                    return outputData;
                }

                // Sort based on entity type
                List<?> sortedResults;
                
                if (results.get(0) instanceof SanPham) {
                    sortedResults = sortProducts((List<SanPham>) results, sortBy, direction);
                } else if (results.get(0) instanceof NguoiDung) {
                    sortedResults = sortUsers((List<NguoiDung>) results, sortBy, direction);
                } else if (results.get(0) instanceof DonHang) {
                    sortedResults = sortOrders((List<DonHang>) results, sortBy, direction);
                } else {
                    // No specific sorting for unknown types
                    sortedResults = new ArrayList<>(results);
                }

                outputData = new SortSearchResultsOutputData(sortedResults, sortBy, direction.name());

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "SORT_ERROR";
            outputData = SortSearchResultsOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }

    private List<SanPham> sortProducts(List<SanPham> products, String sortBy, SortSearchResultsInputData.SortDirection direction) {
        Comparator<SanPham> comparator;

        switch (sortBy.toLowerCase()) {
            case "name":
            case "tensanpham":
                comparator = Comparator.comparing(SanPham::getTenSanPham);
                break;
            case "price":
            case "giaban":
                comparator = Comparator.comparing(SanPham::getGiaBan);
                break;
            case "stock":
            case "soluongtonkho":
                comparator = Comparator.comparing(SanPham::getSoLuongTonKho);
                break;
            case "discount":
            case "phantramgiamgia":
                comparator = Comparator.comparing((SanPham product) -> 
                    product.getPhanTramGiamGia() != null ? product.getPhanTramGiamGia() : java.math.BigDecimal.ZERO
                );
                break;
            case "category":
            case "loaisanpham":
                comparator = Comparator.comparing(SanPham::getLoaiSanPham);
                break;
            default:
                // Default: sort by ID
                comparator = Comparator.comparing(SanPham::getMaSanPham);
                break;
        }

        if (direction == SortSearchResultsInputData.SortDirection.DESC) {
            comparator = comparator.reversed();
        }

        return products.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private List<NguoiDung> sortUsers(List<NguoiDung> users, String sortBy, SortSearchResultsInputData.SortDirection direction) {
        Comparator<NguoiDung> comparator;

        switch (sortBy.toLowerCase()) {
            case "username":
            case "tendangnhap":
                comparator = Comparator.comparing(NguoiDung::getTenDangNhap);
                break;
            case "email":
                comparator = Comparator.comparing(NguoiDung::getEmail);
                break;
            case "fullname":
            case "hoten":
                comparator = Comparator.comparing(NguoiDung::getHoTen);
                break;
            case "role":
            case "vaitro":
                comparator = Comparator.comparing(user -> user.getVaiTro().name());
                break;
            case "createdate":
            case "ngaytao":
                comparator = Comparator.comparing(user -> 
                    user.getNgayTao() != null ? user.getNgayTao() : java.time.LocalDateTime.MIN
                );
                break;
            default:
                // Default: sort by ID
                comparator = Comparator.comparing(NguoiDung::getMaNguoiDung);
                break;
        }

        if (direction == SortSearchResultsInputData.SortDirection.DESC) {
            comparator = comparator.reversed();
        }

        return users.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private List<DonHang> sortOrders(List<DonHang> orders, String sortBy, SortSearchResultsInputData.SortDirection direction) {
        Comparator<DonHang> comparator;

        switch (sortBy.toLowerCase()) {
            case "date":
            case "ngaydathang":
                comparator = Comparator.comparing(DonHang::getNgayDatHang);
                break;
            case "total":
            case "tongtien":
                comparator = Comparator.comparing(DonHang::getTongTien);
                break;
            case "status":
            case "trangthai":
                comparator = Comparator.comparing(order -> order.getTrangThai().name());
                break;
            case "customer":
            case "nguoidung":
                comparator = Comparator.comparing(order -> order.getNguoiDung().getHoTen());
                break;
            default:
                // Default: sort by order ID (most recent first)
                comparator = Comparator.comparing(DonHang::getMaDonHang).reversed();
                break;
        }

        if (direction == SortSearchResultsInputData.SortDirection.DESC) {
            comparator = comparator.reversed();
        }

        return orders.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}

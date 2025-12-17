package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.applysearchfilters.ApplySearchFiltersInputData;
import com.motorbike.business.dto.applysearchfilters.ApplySearchFiltersOutputData;
import com.motorbike.business.usecase.input.ApplySearchFiltersInputBoundary;
import com.motorbike.business.usecase.output.ApplySearchFiltersOutputBoundary;
import com.motorbike.domain.entities.*;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplySearchFiltersUseCaseControl implements ApplySearchFiltersInputBoundary {
    private final ApplySearchFiltersOutputBoundary outputBoundary;

    public ApplySearchFiltersUseCaseControl(ApplySearchFiltersOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ApplySearchFiltersInputData inputData) {
        ApplySearchFiltersOutputData outputData = applyInternal(inputData);
        outputBoundary.present(outputData);
    }

    public ApplySearchFiltersOutputData applyInternal(ApplySearchFiltersInputData inputData) {
        ApplySearchFiltersOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("ApplySearchFilters");
            }
            if (inputData.getResults() == null) {
                throw ValidationException.invalidInput("Results are required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - apply search filters
        if (errorException == null) {
            try {
                List<?> results = inputData.getResults();
                Map<String, Object> filters = inputData.getFilters();
                int originalCount = results.size();

                // If no filters, return all results
                if (filters == null || filters.isEmpty()) {
                    outputData = new ApplySearchFiltersOutputData(results, originalCount);
                    return outputData;
                }

                // Filter based on entity type
                List<?> filteredResults;
                
                if (!results.isEmpty() && results.get(0) instanceof SanPham) {
                    filteredResults = filterProducts((List<SanPham>) results, filters);
                } else if (!results.isEmpty() && results.get(0) instanceof NguoiDung) {
                    filteredResults = filterUsers((List<NguoiDung>) results, filters);
                } else if (!results.isEmpty() && results.get(0) instanceof DonHang) {
                    filteredResults = filterOrders((List<DonHang>) results, filters);
                } else {
                    // No specific filtering for unknown types
                    filteredResults = results;
                }

                outputData = new ApplySearchFiltersOutputData(filteredResults, originalCount);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "FILTER_ERROR";
            outputData = ApplySearchFiltersOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }

    private List<SanPham> filterProducts(List<SanPham> products, Map<String, Object> filters) {
        return products.stream()
                .filter(product -> {
                    // Filter by category
                    if (filters.containsKey("category") || filters.containsKey("loaiSanPham")) {
                        String category = (String) filters.getOrDefault("category", filters.get("loaiSanPham"));
                        if (category != null && !product.getLoaiSanPham().toLowerCase().contains(category.toLowerCase())) {
                            return false;
                        }
                    }

                    // Filter by price range
                    if (filters.containsKey("minPrice")) {
                        BigDecimal minPrice = new BigDecimal(filters.get("minPrice").toString());
                        if (product.getGiaBan().compareTo(minPrice) < 0) {
                            return false;
                        }
                    }
                    if (filters.containsKey("maxPrice")) {
                        BigDecimal maxPrice = new BigDecimal(filters.get("maxPrice").toString());
                        if (product.getGiaBan().compareTo(maxPrice) > 0) {
                            return false;
                        }
                    }

                    // Filter by stock availability
                    if (filters.containsKey("inStock")) {
                        Boolean inStock = (Boolean) filters.get("inStock");
                        if (inStock && product.getSoLuongTonKho() <= 0) {
                            return false;
                        }
                    }

                    // Filter by discount
                    if (filters.containsKey("hasDiscount")) {
                        Boolean hasDiscount = (Boolean) filters.get("hasDiscount");
                        if (hasDiscount && (product.getPhanTramGiamGia() == null || product.getPhanTramGiamGia().compareTo(BigDecimal.ZERO) == 0)) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    private List<NguoiDung> filterUsers(List<NguoiDung> users, Map<String, Object> filters) {
        return users.stream()
                .filter(user -> {
                    // Filter by role
                    if (filters.containsKey("role") || filters.containsKey("vaiTro")) {
                        String roleStr = (String) filters.getOrDefault("role", filters.get("vaiTro"));
                        if (roleStr != null) {
                            try {
                                VaiTro role = VaiTro.valueOf(roleStr.toUpperCase());
                                if (user.getVaiTro() != role) {
                                    return false;
                                }
                            } catch (IllegalArgumentException ignored) {}
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    private List<DonHang> filterOrders(List<DonHang> orders, Map<String, Object> filters) {
        return orders.stream()
                .filter(order -> {
                    // Filter by status
                    if (filters.containsKey("status") || filters.containsKey("trangThai")) {
                        String statusStr = (String) filters.getOrDefault("status", filters.get("trangThai"));
                        if (statusStr != null) {
                            try {
                                TrangThaiDonHang status = TrangThaiDonHang.valueOf(statusStr.toUpperCase());
                                if (order.getTrangThai() != status) {
                                    return false;
                                }
                            } catch (IllegalArgumentException ignored) {}
                        }
                    }

                    // Filter by user ID
                    if (filters.containsKey("userId") || filters.containsKey("maNguoiDung")) {
                        Long userId = Long.valueOf(filters.getOrDefault("userId", filters.get("maNguoiDung")).toString());
                        if (!order.getNguoiDung().getMaNguoiDung().equals(userId)) {
                            return false;
                        }
                    }

                    // Filter by total amount range
                    if (filters.containsKey("minTotal")) {
                        BigDecimal minTotal = new BigDecimal(filters.get("minTotal").toString());
                        if (order.getTongTien().compareTo(minTotal) < 0) {
                            return false;
                        }
                    }
                    if (filters.containsKey("maxTotal")) {
                        BigDecimal maxTotal = new BigDecimal(filters.get("maxTotal").toString());
                        if (order.getTongTien().compareTo(maxTotal) > 0) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }
}

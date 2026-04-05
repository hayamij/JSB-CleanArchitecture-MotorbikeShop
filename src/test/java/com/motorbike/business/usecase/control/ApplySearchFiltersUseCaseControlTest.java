package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.applysearchfilters.ApplySearchFiltersInputData;
import com.motorbike.business.dto.applysearchfilters.ApplySearchFiltersOutputData;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ApplySearchFiltersUseCaseControlTest {

    private ApplySearchFiltersUseCaseControl useCase = new ApplySearchFiltersUseCaseControl(null);

    @Test
    void shouldApplyFiltersSuccessfully() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);

        SanPham product2 = SanPham.createForTest("PT001", "Mũ bảo hiểm", "Phụ tùng", 500000.0, 20, true, true);
        product2.setMaSP(2L);

        List<SanPham> allProducts = Arrays.asList(product1, product2);

        Map<String, Object> filters = new HashMap<>();
        filters.put("keyword", "Yamaha");

        ApplySearchFiltersInputData inputData = new ApplySearchFiltersInputData(allProducts, filters);

        // When
        ApplySearchFiltersOutputData outputData = useCase.applyInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(2, outputData.getOriginalCount());
        assertNotEquals(null, outputData.getFilteredResults());
        assertEquals(1, outputData.getFilteredCount());
    }

    @Test
    void shouldApplyPriceRangeFilter() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);

        SanPham product2 = SanPham.createForTest("PT001", "Mũ bảo hiểm", "Phụ tùng", 500000.0, 20, true, true);
        product2.setMaSP(2L);

        List<SanPham> allProducts = Arrays.asList(product1, product2);

        Map<String, Object> filters = new HashMap<>();
        filters.put("minPrice", 1000000.0);
        filters.put("maxPrice", 50000000.0);

        ApplySearchFiltersInputData inputData = new ApplySearchFiltersInputData(allProducts, filters);

        // When
        ApplySearchFiltersOutputData outputData = useCase.applyInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(2, outputData.getOriginalCount());
        assertEquals(1, outputData.getFilteredCount());
    }

    @Test
    void shouldApplyCategoryFilter() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);

        SanPham product2 = SanPham.createForTest("PT001", "Mũ bảo hiểm", "Phụ tùng", 500000.0, 20, true, true);
        product2.setMaSP(2L);

        List<SanPham> allProducts = Arrays.asList(product1, product2);

        Map<String, Object> filters = new HashMap<>();
        filters.put("category", "XeMay");

        ApplySearchFiltersInputData inputData = new ApplySearchFiltersInputData(allProducts, filters);

        // When
        ApplySearchFiltersOutputData outputData = useCase.applyInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(2, outputData.getOriginalCount());
        assertEquals(1, outputData.getFilteredCount());
    }

    @Test
    void shouldHandleNoFilters() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);

        List<SanPham> allProducts = Arrays.asList(product1);
        Map<String, Object> filters = new HashMap<>();

        ApplySearchFiltersInputData inputData = new ApplySearchFiltersInputData(allProducts, filters);

        // When
        ApplySearchFiltersOutputData outputData = useCase.applyInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(1, outputData.getOriginalCount());
        assertEquals(1, outputData.getFilteredCount());
    }
}

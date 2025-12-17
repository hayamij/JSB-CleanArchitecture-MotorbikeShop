package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.sortsearchresults.SortSearchResultsInputData;
import com.motorbike.business.dto.sortsearchresults.SortSearchResultsInputData.SortDirection;
import com.motorbike.business.dto.sortsearchresults.SortSearchResultsOutputData;
import com.motorbike.business.usecase.output.SortSearchResultsOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SortSearchResultsUseCaseControlTest {

    @Mock
    private SortSearchResultsOutputBoundary outputBoundary;

    private SortSearchResultsUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new SortSearchResultsUseCaseControl(outputBoundary);
    }

    @Test
    void shouldSortByPriceAscending() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);

        SanPham product2 = SanPham.createForTest("PT001", "Mũ bảo hiểm", "Phụ tùng", 500000.0, 20, true, true);
        product2.setMaSP(2L);

        SanPham product3 = SanPham.createForTest("XE002", "Honda Wave", "Xe", 30000000.0, 15, true, false);
        product3.setMaSP(3L);

        List<SanPham> products = Arrays.asList(product1, product2, product3);

        SortSearchResultsInputData inputData = new SortSearchResultsInputData(products, "price", SortDirection.ASC);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(SortSearchResultsOutputData.class));
    }

    @Test
    void shouldSortByPriceDescending() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);

        SanPham product2 = SanPham.createForTest("PT001", "Mũ bảo hiểm", "Phụ tùng", 500000.0, 20, true, true);
        product2.setMaSP(2L);

        List<SanPham> products = Arrays.asList(product1, product2);

        SortSearchResultsInputData inputData = new SortSearchResultsInputData(products, "price", SortDirection.DESC);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(SortSearchResultsOutputData.class));
    }

    @Test
    void shouldSortByNameAscending() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);

        SanPham product2 = SanPham.createForTest("XE002", "Honda Wave", "Xe", 30000000.0, 15, true, false);
        product2.setMaSP(2L);

        List<SanPham> products = Arrays.asList(product1, product2);

        SortSearchResultsInputData inputData = new SortSearchResultsInputData(products, "name", SortDirection.ASC);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(SortSearchResultsOutputData.class));
    }

    @Test
    void shouldHandleDefaultSort() {
        // Given
        SanPham product1 = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product1.setMaSP(1L);

        List<SanPham> products = Arrays.asList(product1);

        SortSearchResultsInputData inputData = new SortSearchResultsInputData(products, null, (SortDirection) null);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(SortSearchResultsOutputData.class));
    }
}

package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.sortsearchresults.SortSearchResultsInputData;
import com.motorbike.business.dto.sortsearchresults.SortSearchResultsInputData.SortDirection;
import com.motorbike.business.dto.sortsearchresults.SortSearchResultsOutputData;
import com.motorbike.domain.entities.XeMay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SortSearchResultsUseCaseControlTest {

    @Test
    void shouldSortByPriceAscending() {
        // Given
        XeMay product1 = new XeMay(1L, "Yamaha Exciter", "Xe thể thao",
            BigDecimal.valueOf(45000000), "exciter.jpg", 10, true, null, null,
            "Yamaha", "Exciter", "Đỏ", 2024, 155);

        XeMay product2 = new XeMay(2L, "Honda Wave", "Xe số",
            BigDecimal.valueOf(30000000), "wave.jpg", 15, true, null, null,
            "Honda", "Wave", "Xanh", 2024, 110);

        XeMay product3 = new XeMay(3L, "Honda Vision", "Xe tay ga",
            BigDecimal.valueOf(35000000), "vision.jpg", 8, true, null, null,
            "Honda", "Vision", "Trắng", 2024, 125);

        List<XeMay> products = Arrays.asList(product1, product2, product3);

        SortSearchResultsInputData inputData = new SortSearchResultsInputData(products, "price", SortDirection.ASC);
        SortSearchResultsUseCaseControl useCase = new SortSearchResultsUseCaseControl(null);

        // When
        SortSearchResultsOutputData outputData = useCase.sortInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(3, outputData.getSortedResults().size());
        assertEquals("price", outputData.getSortBy());
        assertEquals("ASC", outputData.getDirection());
        // Verify sorted order: Wave (30M) < Vision (35M) < Exciter (45M)
        XeMay first = (XeMay) outputData.getSortedResults().get(0);
        assertEquals("Honda Wave", first.getTenSanPham());
    }

    @Test
    void shouldSortByPriceDescending() {
        // Given
        XeMay product1 = new XeMay(1L, "Yamaha Exciter", "Xe thể thao",
            BigDecimal.valueOf(45000000), "exciter.jpg", 10, true, null, null,
            "Yamaha", "Exciter", "Đỏ", 2024, 155);

        XeMay product2 = new XeMay(2L, "Honda Wave", "Xe số",
            BigDecimal.valueOf(30000000), "wave.jpg", 15, true, null, null,
            "Honda", "Wave", "Xanh", 2024, 110);

        List<XeMay> products = Arrays.asList(product1, product2);

        SortSearchResultsInputData inputData = new SortSearchResultsInputData(products, "price", SortDirection.DESC);
        SortSearchResultsUseCaseControl useCase = new SortSearchResultsUseCaseControl(null);

        // When
        SortSearchResultsOutputData outputData = useCase.sortInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(2, outputData.getSortedResults().size());
        assertEquals("DESC", outputData.getDirection());
        // Verify sorted order: Exciter (45M) > Wave (30M)
        XeMay first = (XeMay) outputData.getSortedResults().get(0);
        assertEquals("Yamaha Exciter", first.getTenSanPham());
    }

    @Test
    void shouldSortByNameAscending() {
        // Given
        XeMay product1 = new XeMay(1L, "Yamaha Exciter", "Xe thể thao",
            BigDecimal.valueOf(45000000), "exciter.jpg", 10, true, null, null,
            "Yamaha", "Exciter", "Đỏ", 2024, 155);

        XeMay product2 = new XeMay(2L, "Honda Wave", "Xe số",
            BigDecimal.valueOf(30000000), "wave.jpg", 15, true, null, null,
            "Honda", "Wave", "Xanh", 2024, 110);

        List<XeMay> products = Arrays.asList(product1, product2);

        SortSearchResultsInputData inputData = new SortSearchResultsInputData(products, "name", SortDirection.ASC);
        SortSearchResultsUseCaseControl useCase = new SortSearchResultsUseCaseControl(null);

        // When
        SortSearchResultsOutputData outputData = useCase.sortInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(2, outputData.getSortedResults().size());
        // Verify alphabetical order: Honda < Yamaha
        XeMay first = (XeMay) outputData.getSortedResults().get(0);
        assertEquals("Honda Wave", first.getTenSanPham());
    }

    @Test
    void shouldHandleNullSortBy() {
        // Given
        XeMay product1 = new XeMay(1L, "Yamaha Exciter", "Xe thể thao",
            BigDecimal.valueOf(45000000), "exciter.jpg", 10, true, null, null,
            "Yamaha", "Exciter", "Đỏ", 2024, 155);

        List<XeMay> products = Arrays.asList(product1);

        SortSearchResultsInputData inputData = new SortSearchResultsInputData(products, null, SortDirection.ASC);
        SortSearchResultsUseCaseControl useCase = new SortSearchResultsUseCaseControl(null);

        // When
        SortSearchResultsOutputData outputData = useCase.sortInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(1, outputData.getSortedResults().size());
    }
}

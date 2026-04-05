package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaInputData;
import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaOutputData;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BuildSearchCriteriaUseCaseControlTest {

    private BuildSearchCriteriaUseCaseControl useCase = new BuildSearchCriteriaUseCaseControl(null);

    @Test
    void shouldBuildCriteriaWithKeywordOnly() {
        // Given
        BuildSearchCriteriaInputData inputData = new BuildSearchCriteriaInputData(
            "Yamaha", null, null, null
        );

        // When
        BuildSearchCriteriaOutputData outputData = useCase.buildInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotEquals(null, outputData.getCriteria());
        assertTrue(outputData.getCriteria().containsKey("keyword"));
        assertEquals("yamaha", outputData.getCriteria().get("keyword"));
    }

    @Test
    void shouldBuildCriteriaWithPriceRange() {
        // Given
        Map<String, Object> filters = new HashMap<>();
        filters.put("minPrice", 1000000.0);
        filters.put("maxPrice", 5000000.0);

        BuildSearchCriteriaInputData inputData = new BuildSearchCriteriaInputData(
            null, filters, null
        );

        // When
        BuildSearchCriteriaOutputData outputData = useCase.buildInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotEquals(null, outputData.getCriteria());
        assertTrue(outputData.getCriteria().containsKey("minPrice"));
        assertTrue(outputData.getCriteria().containsKey("maxPrice"));
    }

    @Test
    void shouldBuildCriteriaWithCategory() {
        // Given
        BuildSearchCriteriaInputData inputData = new BuildSearchCriteriaInputData(
            null, null, "motorbike"
        );

        // When
        BuildSearchCriteriaOutputData outputData = useCase.buildInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotEquals(null, outputData.getCriteria());
        assertEquals("motorbike", outputData.getSearchType());
    }

    @Test
    void shouldBuildCriteriaWithAllParameters() {
        // Given
        Map<String, Object> filters = new HashMap<>();
        filters.put("minPrice", 10000000.0);
        filters.put("maxPrice", 50000000.0);

        BuildSearchCriteriaInputData inputData = new BuildSearchCriteriaInputData(
            "Yamaha", filters, "motorbike"
        );

        // When
        BuildSearchCriteriaOutputData outputData = useCase.buildInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotEquals(null, outputData.getCriteria());
        assertTrue(outputData.getCriteria().containsKey("keyword"));
        assertTrue(outputData.getCriteria().containsKey("minPrice"));
        assertTrue(outputData.getCriteria().containsKey("maxPrice"));
        assertEquals("motorbike", outputData.getSearchType());
    }

    @Test
    void shouldHandleEmptyCriteria() {
        // Given
        BuildSearchCriteriaInputData inputData = new BuildSearchCriteriaInputData(
            null, null, null
        );

        // When
        BuildSearchCriteriaOutputData outputData = useCase.buildInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotEquals(null, outputData.getCriteria());
    }
}

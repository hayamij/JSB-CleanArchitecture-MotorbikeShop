package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaInputData;
import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaOutputData;
import com.motorbike.business.usecase.output.BuildSearchCriteriaOutputBoundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuildSearchCriteriaUseCaseControlTest {

    @Mock
    private BuildSearchCriteriaOutputBoundary outputBoundary;

    private BuildSearchCriteriaUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuildSearchCriteriaUseCaseControl(outputBoundary);
    }

    @Test
    void shouldBuildCriteriaWithKeywordOnly() {
        // Given
        BuildSearchCriteriaInputData inputData = new BuildSearchCriteriaInputData(
            "Yamaha", null, null, null
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(BuildSearchCriteriaOutputData.class));
    }

    @Test
    void shouldBuildCriteriaWithPriceRange() {
        // Given
        BuildSearchCriteriaInputData inputData = new BuildSearchCriteriaInputData(
            null, 1000000.0, 5000000.0, null
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(BuildSearchCriteriaOutputData.class));
    }

    @Test
    void shouldBuildCriteriaWithCategory() {
        // Given
        BuildSearchCriteriaInputData inputData = new BuildSearchCriteriaInputData(
            null, null, null, "Xe máy"
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(BuildSearchCriteriaOutputData.class));
    }

    @Test
    void shouldBuildCriteriaWithAllParameters() {
        // Given
        BuildSearchCriteriaInputData inputData = new BuildSearchCriteriaInputData(
            "Yamaha", 10000000.0, 50000000.0, "Xe máy"
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(BuildSearchCriteriaOutputData.class));
    }

    @Test
    void shouldHandleEmptyCriteria() {
        // Given
        BuildSearchCriteriaInputData inputData = new BuildSearchCriteriaInputData(
            null, null, null, null
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(BuildSearchCriteriaOutputData.class));
    }
}

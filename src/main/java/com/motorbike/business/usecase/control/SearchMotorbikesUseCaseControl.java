package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.SearchMotorbikesInputData;
import com.motorbike.business.dto.motorbike.SearchMotorbikesOutputData;
import com.motorbike.business.dto.motorbike.SearchMotorbikesOutputData.MotorbikeItem;
import com.motorbike.business.dto.motorbike.FormatMotorbikesForDisplayInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.SearchMotorbikesOutputBoundary;
import com.motorbike.business.dto.buildsearchcriteria.BuildSearchCriteriaInputData;
import com.motorbike.business.dto.applysearchfilters.ApplySearchFiltersInputData;
import com.motorbike.business.dto.sortsearchresults.SortSearchResultsInputData;
import com.motorbike.business.usecase.input.BuildSearchCriteriaInputBoundary;
import com.motorbike.business.usecase.input.ApplySearchFiltersInputBoundary;
import com.motorbike.business.usecase.input.SortSearchResultsInputBoundary;
import com.motorbike.business.usecase.input.FormatMotorbikesForDisplayInputBoundary;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.exceptions.SystemException;
import com.motorbike.business.usecase.input.SearchMotorbikesInputBoundary;

import java.util.List;
import java.util.stream.Collectors;

public class SearchMotorbikesUseCaseControl implements SearchMotorbikesInputBoundary{

    private final SearchMotorbikesOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final BuildSearchCriteriaInputBoundary buildSearchCriteriaUseCase;
    private final ApplySearchFiltersInputBoundary applySearchFiltersUseCase;
    private final SortSearchResultsInputBoundary sortSearchResultsUseCase;
    private final FormatMotorbikesForDisplayInputBoundary formatMotorbikesUseCase;

    public SearchMotorbikesUseCaseControl(
            SearchMotorbikesOutputBoundary outputBoundary,
            ProductRepository productRepository,
            BuildSearchCriteriaInputBoundary buildSearchCriteriaUseCase,
            ApplySearchFiltersInputBoundary applySearchFiltersUseCase,
            SortSearchResultsInputBoundary sortSearchResultsUseCase,
            FormatMotorbikesForDisplayInputBoundary formatMotorbikesUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.buildSearchCriteriaUseCase = buildSearchCriteriaUseCase;
        this.applySearchFiltersUseCase = applySearchFiltersUseCase;
        this.sortSearchResultsUseCase = sortSearchResultsUseCase;
        this.formatMotorbikesUseCase = formatMotorbikesUseCase;
    }

    // Constructor with 2 parameters (for backward compatibility)
    public SearchMotorbikesUseCaseControl(
            SearchMotorbikesOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.buildSearchCriteriaUseCase = new BuildSearchCriteriaUseCaseControl(null);
        this.applySearchFiltersUseCase = new ApplySearchFiltersUseCaseControl(null);
        this.sortSearchResultsUseCase = new SortSearchResultsUseCaseControl(null);
        this.formatMotorbikesUseCase = new FormatMotorbikesForDisplayUseCaseControl(null);
    }

    @Override
    public void execute(SearchMotorbikesInputData input) {
        SearchMotorbikesOutputData outputData = null;
        Exception errorException = null;

        try {
            // Step 1: Get all motorbikes
            List<XeMay> all = productRepository.findAllMotorbikes();

            // Step 2: UC-68 - Build search criteria
            BuildSearchCriteriaInputData criteriaInput = new BuildSearchCriteriaInputData(
                input.keyword,
                java.util.Map.of(
                    "brand", input.brand != null ? input.brand : "",
                    "model", input.model != null ? input.model : "",
                    "color", input.color != null ? input.color : "",
                    "minCC", input.minCC != null ? input.minCC.toString() : "",
                    "maxCC", input.maxCC != null ? input.maxCC.toString() : ""
                )
            ); 
            var criteriaResult = ((BuildSearchCriteriaUseCaseControl) buildSearchCriteriaUseCase)
                .buildInternal(criteriaInput);

            // Step 3: UC-69 - Apply search filters
            ApplySearchFiltersInputData filtersInput = new ApplySearchFiltersInputData(
                all,
                criteriaResult.getCriteria()
            );
            var filtersResult = ((ApplySearchFiltersUseCaseControl) applySearchFiltersUseCase)
                .applyInternal(filtersInput);

            // Step 4: UC-74 - Format motorbikes for display
            List<XeMay> filteredMotorbikes = (List<XeMay>) filtersResult.getFilteredResults();
            FormatMotorbikesForDisplayInputData formatInput = new FormatMotorbikesForDisplayInputData(filteredMotorbikes);
            var formatResult = ((FormatMotorbikesForDisplayUseCaseControl) formatMotorbikesUseCase)
                .formatInternal(formatInput);
            
            if (!formatResult.isSuccess()) {
                throw new SystemException(formatResult.getErrorMessage(), formatResult.getErrorCode());
            }
            
            // Convert from GetAllMotorbikesOutputData.MotorbikeItem to SearchMotorbikesOutputData.MotorbikeItem
            List<MotorbikeItem> mapped = formatResult.getMotorbikeItems().stream()
                .map(item -> new MotorbikeItem(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    item.getStock(),
                    item.getImageUrl(),
                    item.getBrand(),
                    item.getModel(),
                    item.getColor(),
                    item.getYear(),
                    item.getDisplacement()
                ))
                .collect(Collectors.toList());

            // Step 5: UC-70 - Sort results (if sortBy specified)
            if (input.sortBy != null && !input.sortBy.isEmpty()) {
                SortSearchResultsInputData.SortDirection direction = 
                    (input.sortDirection != null && "DESC".equalsIgnoreCase(input.sortDirection)) 
                    ? SortSearchResultsInputData.SortDirection.DESC 
                    : SortSearchResultsInputData.SortDirection.ASC;
                
                SortSearchResultsInputData sortInput = new SortSearchResultsInputData(
                    (List) mapped,
                    input.sortBy,
                    direction
                );
                var sortResult = ((SortSearchResultsUseCaseControl) sortSearchResultsUseCase)
                    .sortInternal(sortInput);
                mapped = (List<MotorbikeItem>) sortResult.getSortedResults();
            }

            outputData = new SearchMotorbikesOutputData(mapped);
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new SearchMotorbikesOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}

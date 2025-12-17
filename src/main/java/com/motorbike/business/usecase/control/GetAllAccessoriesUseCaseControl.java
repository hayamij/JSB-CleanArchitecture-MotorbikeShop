package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData;
import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData.AccessoryItem;
import com.motorbike.business.dto.accessory.FormatAccessoriesForDisplayInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.GetAllAccessoriesInputBoundary;
import com.motorbike.business.usecase.input.FormatAccessoriesForDisplayInputBoundary;
import com.motorbike.business.usecase.output.GetAllAccessoriesOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.exceptions.SystemException;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllAccessoriesUseCaseControl implements GetAllAccessoriesInputBoundary {

    private final GetAllAccessoriesOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final FormatAccessoriesForDisplayInputBoundary formatAccessoriesUseCase;

    public GetAllAccessoriesUseCaseControl(
            GetAllAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository,
            FormatAccessoriesForDisplayInputBoundary formatAccessoriesUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.formatAccessoriesUseCase = formatAccessoriesUseCase;
    }

    // Backward compatibility constructor
    public GetAllAccessoriesUseCaseControl(
            GetAllAccessoriesOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.formatAccessoriesUseCase = new FormatAccessoriesForDisplayUseCaseControl(null);
    }

    @Override
    public void execute() {
        GetAllAccessoriesOutputData outputData = null;
        Exception errorException = null;

        try {
            // Step 1: Get all accessories from repository
            List<PhuKienXeMay> all = productRepository.findAllAccessories();

            // Step 2: UC-76 Format accessories for display
            FormatAccessoriesForDisplayInputData formatInput = new FormatAccessoriesForDisplayInputData(all);
            var formatResult = ((FormatAccessoriesForDisplayUseCaseControl) formatAccessoriesUseCase).formatInternal(formatInput);
            
            if (!formatResult.isSuccess()) {
                throw new SystemException(formatResult.getErrorMessage(), formatResult.getErrorCode());
            }

            outputData = new GetAllAccessoriesOutputData(formatResult.getAccessoryItems());
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new GetAllAccessoriesOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}

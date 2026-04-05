package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData;
import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData.MotorbikeItem;
import com.motorbike.business.dto.motorbike.FormatMotorbikesForDisplayInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.GetAllMotorbikesInputBoundary;
import com.motorbike.business.usecase.input.FormatMotorbikesForDisplayInputBoundary;
import com.motorbike.business.usecase.output.GetAllMotorbikesOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.exceptions.SystemException;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllMotorbikesUseCaseControl implements GetAllMotorbikesInputBoundary {

    private final GetAllMotorbikesOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final FormatMotorbikesForDisplayInputBoundary formatMotorbikesUseCase;

    public GetAllMotorbikesUseCaseControl(
            GetAllMotorbikesOutputBoundary outputBoundary,
            ProductRepository productRepository,
            FormatMotorbikesForDisplayInputBoundary formatMotorbikesUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.formatMotorbikesUseCase = formatMotorbikesUseCase;
    }

    // Backward compatibility constructor
    public GetAllMotorbikesUseCaseControl(
            GetAllMotorbikesOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.formatMotorbikesUseCase = new FormatMotorbikesForDisplayUseCaseControl(null);
    }

    @Override
    public void execute() {
        GetAllMotorbikesOutputData outputData = null;
        Exception errorException = null;

        try {
            // Step 1: Get all products from repository
            List<SanPham> allProducts = productRepository.findAll();

            // Step 2: Filter to get only motorbikes
            List<XeMay> motorbikes = allProducts.stream()
                    .filter(p -> p instanceof XeMay)
                    .map(p -> (XeMay) p)
                    .collect(Collectors.toList());

            // Step 3: UC-74 Format motorbikes for display
            FormatMotorbikesForDisplayInputData formatInput = new FormatMotorbikesForDisplayInputData(motorbikes);
            var formatResult = ((FormatMotorbikesForDisplayUseCaseControl) formatMotorbikesUseCase).formatInternal(formatInput);
            
            if (!formatResult.isSuccess()) {
                throw new SystemException(formatResult.getErrorMessage(), formatResult.getErrorCode());
            }

            outputData = new GetAllMotorbikesOutputData(formatResult.getMotorbikeItems());
        } catch (Exception e) {
            errorException = e;
        }

        if (errorException != null) {
            outputData = new GetAllMotorbikesOutputData("SYSTEM_ERROR", errorException.getMessage());
        }

        outputBoundary.present(outputData);
    }
}

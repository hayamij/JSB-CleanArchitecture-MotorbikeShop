package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.calculateproductprice.CalculateProductPriceInputData;
import com.motorbike.business.dto.calculateproductprice.CalculateProductPriceOutputData;
import com.motorbike.business.usecase.input.CalculateProductPriceInputBoundary;
import com.motorbike.business.usecase.output.CalculateProductPriceOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculateProductPriceUseCaseControl implements CalculateProductPriceInputBoundary {
    private final CalculateProductPriceOutputBoundary outputBoundary;

    public CalculateProductPriceUseCaseControl(CalculateProductPriceOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(CalculateProductPriceInputData inputData) {
        CalculateProductPriceOutputData outputData = calculateInternal(inputData);
        outputBoundary.present(outputData);
    }

    public CalculateProductPriceOutputData calculateInternal(CalculateProductPriceInputData inputData) {
        CalculateProductPriceOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("CalculateProductPrice");
            }
            if (inputData.getBasePrice() == null) {
                throw ValidationException.invalidInput("Base price is required");
            }
            if (inputData.getBasePrice().compareTo(BigDecimal.ZERO) < 0) {
                throw ValidationException.invalidInput("Base price cannot be negative");
            }
            if (inputData.getDiscountPercent() != null) {
                if (inputData.getDiscountPercent() < 0 || inputData.getDiscountPercent() > 100) {
                    throw ValidationException.invalidInput("Discount percent must be between 0 and 100");
                }
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - calculate final price
        if (errorException == null) {
            try {
                BigDecimal basePrice = inputData.getBasePrice();
                Integer discountPercent = inputData.getDiscountPercent() != null ? inputData.getDiscountPercent() : 0;

                // Calculate discount amount
                BigDecimal discountAmount = basePrice
                        .multiply(BigDecimal.valueOf(discountPercent))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                // Calculate final price
                BigDecimal finalPrice = basePrice.subtract(discountAmount);

                outputData = new CalculateProductPriceOutputData(
                        basePrice,
                        discountPercent,
                        discountAmount,
                        finalPrice
                );

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "CALCULATION_ERROR";
            outputData = CalculateProductPriceOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
}

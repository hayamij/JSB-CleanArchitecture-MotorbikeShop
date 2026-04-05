package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validatestockoperation.ValidateStockOperationInputData;
import com.motorbike.business.dto.validatestockoperation.ValidateStockOperationOutputData;
import com.motorbike.business.usecase.input.ValidateStockOperationInputBoundary;
import com.motorbike.business.usecase.output.ValidateStockOperationOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

public class ValidateStockOperationUseCaseControl implements ValidateStockOperationInputBoundary {
    private final ValidateStockOperationOutputBoundary outputBoundary;

    public ValidateStockOperationUseCaseControl(ValidateStockOperationOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ValidateStockOperationInputData inputData) {
        ValidateStockOperationOutputData outputData = validateInternal(inputData);
        outputBoundary.present(outputData);
    }

    public ValidateStockOperationOutputData validateInternal(ValidateStockOperationInputData inputData) {
        ValidateStockOperationOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("ValidateStockOperation");
            }
            if (inputData.getProductId() == null) {
                throw ValidationException.invalidInput("Product ID is required");
            }
            if (inputData.getOperation() == null) {
                throw ValidationException.invalidInput("Operation type is required");
            }
            if (inputData.getQuantity() <= 0) {
                throw ValidationException.invalidInput("Quantity must be positive");
            }
            if (inputData.getCurrentStock() < 0) {
                throw ValidationException.invalidInput("Current stock cannot be negative");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - validate stock operation
        if (errorException == null) {
            try {
                int currentStock = inputData.getCurrentStock();
                int quantity = inputData.getQuantity();
                ValidateStockOperationInputData.StockOperation operation = inputData.getOperation();

                boolean isValid = true;
                String reason = null;
                int resultingStock = currentStock;

                switch (operation) {
                    case INCREASE:
                        resultingStock = currentStock + quantity;
                        
                        // Check if resulting stock exceeds maximum limit
                        if (resultingStock > 999999) {
                            isValid = false;
                            reason = "Số lượng tồn kho sau khi tăng vượt quá giới hạn (999,999)";
                        } else {
                            reason = "Hợp lệ - tồn kho sau khi tăng: " + resultingStock;
                        }
                        break;

                    case DECREASE:
                        resultingStock = currentStock - quantity;
                        
                        // Check if resulting stock is negative
                        if (resultingStock < 0) {
                            isValid = false;
                            reason = "Không đủ hàng trong kho. Tồn kho hiện tại: " + currentStock + 
                                    ", yêu cầu giảm: " + quantity;
                        } else if (resultingStock == 0) {
                            reason = "Hợp lệ - sản phẩm sẽ hết hàng sau khi giảm";
                        } else {
                            reason = "Hợp lệ - tồn kho sau khi giảm: " + resultingStock;
                        }
                        break;

                    default:
                        isValid = false;
                        reason = "Loại thao tác không hợp lệ";
                        break;
                }

                outputData = new ValidateStockOperationOutputData(isValid, reason, resultingStock);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "VALIDATION_ERROR";
            outputData = ValidateStockOperationOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
}

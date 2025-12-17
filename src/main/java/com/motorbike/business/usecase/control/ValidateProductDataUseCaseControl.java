package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateproductdata.ValidateProductDataInputData;
import com.motorbike.business.dto.validateproductdata.ValidateProductDataOutputData;
import com.motorbike.business.usecase.input.ValidateProductDataInputBoundary;
import com.motorbike.business.usecase.output.ValidateProductDataOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ValidateProductDataUseCaseControl implements ValidateProductDataInputBoundary {
    private final ValidateProductDataOutputBoundary outputBoundary;

    public ValidateProductDataUseCaseControl(ValidateProductDataOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ValidateProductDataInputData inputData) {
        ValidateProductDataOutputData outputData = validateInternal(inputData);
        outputBoundary.present(outputData);
    }

    public ValidateProductDataOutputData validateInternal(ValidateProductDataInputData inputData) {
        ValidateProductDataOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("ValidateProductData");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - validate product data
        if (errorException == null) {
            try {
                List<String> errors = new ArrayList<>();

                // Validate product name
                if (inputData.getProductName() == null || inputData.getProductName().trim().isEmpty()) {
                    errors.add("Tên sản phẩm không được để trống");
                } else if (inputData.getProductName().trim().length() < 3) {
                    errors.add("Tên sản phẩm phải có ít nhất 3 ký tự");
                } else if (inputData.getProductName().trim().length() > 200) {
                    errors.add("Tên sản phẩm không được vượt quá 200 ký tự");
                }

                // Validate product code
                if (inputData.getProductCode() == null || inputData.getProductCode().trim().isEmpty()) {
                    errors.add("Mã sản phẩm không được để trống");
                } else if (inputData.getProductCode().trim().length() < 3) {
                    errors.add("Mã sản phẩm phải có ít nhất 3 ký tự");
                } else if (inputData.getProductCode().trim().length() > 50) {
                    errors.add("Mã sản phẩm không được vượt quá 50 ký tự");
                }

                // Validate price
                if (inputData.getPrice() == null) {
                    errors.add("Giá sản phẩm không được để trống");
                } else if (inputData.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    errors.add("Giá sản phẩm phải lớn hơn 0");
                } else if (inputData.getPrice().compareTo(new BigDecimal("999999999")) > 0) {
                    errors.add("Giá sản phẩm không được vượt quá 999,999,999");
                }

                // Validate stock
                if (inputData.getStock() == null) {
                    errors.add("Số lượng tồn kho không được để trống");
                } else if (inputData.getStock() < 0) {
                    errors.add("Số lượng tồn kho không được âm");
                } else if (inputData.getStock() > 999999) {
                    errors.add("Số lượng tồn kho không được vượt quá 999,999");
                }

                // Validate category
                if (inputData.getCategory() == null || inputData.getCategory().trim().isEmpty()) {
                    errors.add("Danh mục sản phẩm không được để trống");
                }

                boolean isValid = errors.isEmpty();
                outputData = new ValidateProductDataOutputData(isValid, errors);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "VALIDATION_ERROR";
            outputData = ValidateProductDataOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
}

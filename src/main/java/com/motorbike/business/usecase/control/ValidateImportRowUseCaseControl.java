package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateimportrow.ValidateImportRowInputData;
import com.motorbike.business.dto.validateimportrow.ValidateImportRowOutputData;
import com.motorbike.business.usecase.input.ValidateImportRowInputBoundary;
import com.motorbike.business.usecase.output.ValidateImportRowOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidateImportRowUseCaseControl implements ValidateImportRowInputBoundary {
    private final ValidateImportRowOutputBoundary outputBoundary;

    public ValidateImportRowUseCaseControl(ValidateImportRowOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ValidateImportRowInputData inputData) {
        ValidateImportRowOutputData outputData = validateInternal(inputData);
        outputBoundary.present(outputData);
    }

    public ValidateImportRowOutputData validateInternal(ValidateImportRowInputData inputData) {
        ValidateImportRowOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("ValidateImportRow");
            }
            if (inputData.getRowData() == null) {
                throw ValidationException.invalidInput("Row data is required");
            }
            if (inputData.getRowNumber() <= 0) {
                throw ValidationException.invalidInput("Row number must be positive");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - validate import row
        if (errorException == null) {
            try {
                Map<String, String> rowData = inputData.getRowData();
                int rowNumber = inputData.getRowNumber();
                String importType = inputData.getImportType();
                List<String> errors = new ArrayList<>();

                // Validate based on import type
                if ("motorbike".equalsIgnoreCase(importType) || "xe_may".equalsIgnoreCase(importType)) {
                    validateMotorbikeRow(rowData, errors);
                } else if ("accessory".equalsIgnoreCase(importType) || "phu_tung".equalsIgnoreCase(importType)) {
                    validateAccessoryRow(rowData, errors);
                } else {
                    // Generic product validation
                    validateGenericProductRow(rowData, errors);
                }

                boolean isValid = errors.isEmpty();
                outputData = new ValidateImportRowOutputData(isValid, rowNumber, errors);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "VALIDATION_ERROR";
            outputData = ValidateImportRowOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }

    private void validateMotorbikeRow(Map<String, String> rowData, List<String> errors) {
        // Validate required fields for motorbike
        validateRequiredField(rowData, "Tên xe", errors);
        validateRequiredField(rowData, "Mã sản phẩm", errors);
        validateRequiredField(rowData, "Giá bán", errors);
        validateRequiredField(rowData, "Số lượng", errors);

        // Validate numeric fields
        validateNumericField(rowData, "Giá bán", errors);
        validateIntegerField(rowData, "Số lượng", errors);

        // Validate optional numeric fields if present
        if (rowData.containsKey("Phần trăm giảm giá") && !rowData.get("Phần trăm giảm giá").trim().isEmpty()) {
            validateIntegerField(rowData, "Phần trăm giảm giá", errors);
            
            try {
                int discount = Integer.parseInt(rowData.get("Phần trăm giảm giá"));
                if (discount < 0 || discount > 100) {
                    errors.add("Phần trăm giảm giá phải từ 0 đến 100");
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    private void validateAccessoryRow(Map<String, String> rowData, List<String> errors) {
        // Similar to motorbike but may have different fields
        validateRequiredField(rowData, "Tên phụ tùng", errors);
        validateRequiredField(rowData, "Mã sản phẩm", errors);
        validateRequiredField(rowData, "Giá bán", errors);
        validateRequiredField(rowData, "Số lượng", errors);

        validateNumericField(rowData, "Giá bán", errors);
        validateIntegerField(rowData, "Số lượng", errors);
    }

    private void validateGenericProductRow(Map<String, String> rowData, List<String> errors) {
        // Generic validation for any product type
        validateRequiredField(rowData, "Tên sản phẩm", errors);
        validateRequiredField(rowData, "Giá bán", errors);
        validateRequiredField(rowData, "Số lượng", errors);

        validateNumericField(rowData, "Giá bán", errors);
        validateIntegerField(rowData, "Số lượng", errors);
    }

    private void validateRequiredField(Map<String, String> rowData, String fieldName, List<String> errors) {
        if (!rowData.containsKey(fieldName) || rowData.get(fieldName) == null || rowData.get(fieldName).trim().isEmpty()) {
            errors.add(fieldName + " không được để trống");
        }
    }

    private void validateNumericField(Map<String, String> rowData, String fieldName, List<String> errors) {
        if (!rowData.containsKey(fieldName)) {
            return;
        }

        String value = rowData.get(fieldName);
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        try {
            BigDecimal numValue = new BigDecimal(value.trim());
            if (numValue.compareTo(BigDecimal.ZERO) < 0) {
                errors.add(fieldName + " không được âm");
            }
        } catch (NumberFormatException e) {
            errors.add(fieldName + " phải là số hợp lệ");
        }
    }

    private void validateIntegerField(Map<String, String> rowData, String fieldName, List<String> errors) {
        if (!rowData.containsKey(fieldName)) {
            return;
        }

        String value = rowData.get(fieldName);
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        try {
            int intValue = Integer.parseInt(value.trim());
            if (intValue < 0) {
                errors.add(fieldName + " không được âm");
            }
        } catch (NumberFormatException e) {
            errors.add(fieldName + " phải là số nguyên hợp lệ");
        }
    }
}

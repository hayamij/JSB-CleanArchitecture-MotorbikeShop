package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.order.GetValidOrderStatusesInputData;
import com.motorbike.business.dto.order.GetValidOrderStatusesOutputData;
import com.motorbike.business.usecase.input.GetValidOrderStatusesInputBoundary;
import com.motorbike.business.usecase.output.GetValidOrderStatusesOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetValidOrderStatusesUseCaseControl implements GetValidOrderStatusesInputBoundary {

    private final GetValidOrderStatusesOutputBoundary outputBoundary;

    public GetValidOrderStatusesUseCaseControl(GetValidOrderStatusesOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(GetValidOrderStatusesInputData inputData) {
        GetValidOrderStatusesOutputData outputData = null;
        Exception errorException = null;

        // Validation
        try {
            if (inputData == null || inputData.getCurrentStatus() == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Business logic: Determine valid next statuses based on current status
        if (errorException == null) {
            try {
                String currentStatus = inputData.getCurrentStatus();
                List<String> validStatuses = getValidNextStatuses(currentStatus);
                outputData = GetValidOrderStatusesOutputData.forSuccess(validStatuses);
            } catch (Exception e) {
                errorException = e;
            }
        }

        // Error handling
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException 
                    ? ((ValidationException) errorException).getErrorCode() 
                    : "GET_VALID_STATUSES_ERROR";
            outputData = GetValidOrderStatusesOutputData.forError(errorCode, errorException.getMessage());
        }

        // Present
        outputBoundary.present(outputData);
    }

    /**
     * Business rule: State machine for order status transitions
     * This is the SINGLE SOURCE OF TRUTH for valid order status transitions
     */
    private List<String> getValidNextStatuses(String currentStatus) {
        switch (currentStatus) {
            case "CHO_XAC_NHAN":
                return Arrays.asList("DA_XAC_NHAN", "DA_HUY");
            case "DA_XAC_NHAN":
                return Arrays.asList("DANG_GIAO", "DA_HUY");
            case "DANG_GIAO":
                return Arrays.asList("DA_GIAO", "DA_HUY");
            case "DA_GIAO":
            case "DA_HUY":
                // Cannot change from these terminal statuses
                return new ArrayList<>();
            default:
                return new ArrayList<>();
        }
    }
}

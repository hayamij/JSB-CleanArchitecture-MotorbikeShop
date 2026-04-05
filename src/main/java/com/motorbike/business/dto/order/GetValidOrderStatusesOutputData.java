package com.motorbike.business.dto.order;

import java.util.List;

public class GetValidOrderStatusesOutputData {
    private final boolean success;
    private final List<String> validStatuses;
    private final String errorCode;
    private final String errorMessage;

    private GetValidOrderStatusesOutputData(boolean success, List<String> validStatuses,
                                           String errorCode, String errorMessage) {
        this.success = success;
        this.validStatuses = validStatuses;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static GetValidOrderStatusesOutputData forSuccess(List<String> validStatuses) {
        return new GetValidOrderStatusesOutputData(true, validStatuses, null, null);
    }

    public static GetValidOrderStatusesOutputData forError(String errorCode, String errorMessage) {
        return new GetValidOrderStatusesOutputData(false, null, errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getValidStatuses() {
        return validStatuses;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

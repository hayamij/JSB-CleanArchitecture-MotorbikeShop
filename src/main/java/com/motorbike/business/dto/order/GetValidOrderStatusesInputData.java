package com.motorbike.business.dto.order;

public class GetValidOrderStatusesInputData {
    private final String currentStatus;

    public GetValidOrderStatusesInputData(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }
}

package com.motorbike.business.dto.archiveproduct;

import java.time.LocalDateTime;

public class ArchiveProductOutputData {
    private final boolean success;
    private final Long productId;
    private final String productName;
    private final LocalDateTime archivedAt;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public ArchiveProductOutputData(Long productId, String productName, LocalDateTime archivedAt) {
        this.success = true;
        this.productId = productId;
        this.productName = productName;
        this.archivedAt = archivedAt;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    private ArchiveProductOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.productId = null;
        this.productName = null;
        this.archivedAt = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ArchiveProductOutputData forError(String errorCode, String errorMessage) {
        return new ArchiveProductOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

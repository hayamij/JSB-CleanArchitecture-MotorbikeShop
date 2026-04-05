package com.motorbike.business.dto.validatestockoperation;

public class ValidateStockOperationInputData {
    private final Long productId;
    private final String productName;
    private final int currentStock;
    private final StockOperation operation;
    private final int quantity;

    public enum StockOperation {
        INCREASE,
        DECREASE
    }

    public ValidateStockOperationInputData(
            Long productId,
            String productName,
            int currentStock,
            StockOperation operation,
            int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.currentStock = currentStock;
        this.operation = operation;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public StockOperation getOperation() {
        return operation;
    }

    public int getQuantity() {
        return quantity;
    }
}

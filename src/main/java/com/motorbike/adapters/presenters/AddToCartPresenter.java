package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.AddToCartViewModel;
import com.motorbike.business.dto.addtocart.AddToCartOutputData;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Presenter for AddToCart Feature
 * Transforms AddToCartOutputData → AddToCartViewModel
 * Contains presentation logic (formatting, display rules)
 * NO business logic
 */
public class AddToCartPresenter implements AddToCartOutputBoundary {
    
    private final AddToCartViewModel viewModel;
    private static final NumberFormat CURRENCY_FORMATTER = 
        NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    /**
     * Constructor
     * @param viewModel View model to be updated
     */
    public AddToCartPresenter(AddToCartViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AddToCartOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }

    /**
     * Present success case
     * @param outputData Output data from use case
     */
    private void presentSuccess(AddToCartOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        
        // Cart summary
        viewModel.cartId = outputData.getCartId();
        viewModel.totalItems = outputData.getTotalItems();
        viewModel.totalQuantity = outputData.getTotalQuantity();
        viewModel.formattedTotalAmount = formatCurrency(outputData.getTotalAmount());
        
        // Added item information
        viewModel.productId = outputData.getProductId();
        viewModel.productName = outputData.getProductName();
        viewModel.addedQuantity = outputData.getAddedQuantity();
        viewModel.newItemQuantity = outputData.getNewItemQuantity();
        viewModel.itemAlreadyInCart = outputData.isItemAlreadyInCart();
        
        // Generate added item message
        if (outputData.isItemAlreadyInCart()) {
            viewModel.addedItemMessage = String.format(
                "Đã cập nhật số lượng '%s' thành %d sản phẩm",
                outputData.getProductName(),
                outputData.getNewItemQuantity()
            );
        } else {
            viewModel.addedItemMessage = String.format(
                "Đã thêm %d '%s' vào giỏ hàng",
                outputData.getAddedQuantity(),
                outputData.getProductName()
            );
        }
        
        // Product information
        viewModel.formattedProductPrice = formatCurrency(outputData.getProductPrice());
        viewModel.productStock = outputData.getProductStock();
        viewModel.stockStatus = formatStockStatus(
            outputData.getProductStock(), 
            outputData.getNewItemQuantity()
        );
        
        // Success message
        viewModel.message = String.format(
            "Thêm vào giỏ hàng thành công! Giỏ hàng có %d sản phẩm (%s)",
            viewModel.totalItems,
            viewModel.formattedTotalAmount
        );
        
        // UI actions
        viewModel.showCartPopup = true; // Show cart summary
        viewModel.redirectUrl = null; // Stay on current page
        
        // Clear error fields
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.errorColor = null;
    }

    /**
     * Present error case
     * @param outputData Output data from use case
     */
    private void presentError(AddToCartOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        
        // Error information
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(
            outputData.getErrorCode(), 
            outputData.getErrorMessage()
        );
        viewModel.errorColor = determineErrorColor(outputData.getErrorCode());
        
        // Main message
        viewModel.message = "Không thể thêm vào giỏ hàng";
        
        // UI actions
        viewModel.showCartPopup = false;
        viewModel.redirectUrl = null;
        
        // Clear success fields
        viewModel.cartId = null;
        viewModel.totalItems = 0;
        viewModel.totalQuantity = 0;
        viewModel.formattedTotalAmount = null;
        viewModel.productId = null;
        viewModel.productName = null;
        viewModel.addedQuantity = 0;
        viewModel.newItemQuantity = 0;
        viewModel.itemAlreadyInCart = false;
        viewModel.addedItemMessage = null;
        viewModel.formattedProductPrice = null;
        viewModel.productStock = 0;
        viewModel.stockStatus = null;
    }

    /**
     * Format currency to VND
     * @param amount Amount to format
     * @return Formatted currency string
     */
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "₫0";
        }
        return CURRENCY_FORMATTER.format(amount);
    }

    /**
     * Format stock status message
     * @param stockQuantity Available stock
     * @param currentCartQuantity Current quantity in cart
     * @return Formatted stock status
     */
    private String formatStockStatus(int stockQuantity, int currentCartQuantity) {
        int remaining = stockQuantity - currentCartQuantity;
        
        if (remaining <= 0) {
            return "Đã đặt hết số lượng có sẵn";
        } else if (remaining < 5) {
            return String.format("Còn %d sản phẩm có thể thêm", remaining);
        } else {
            return String.format("Còn %d sản phẩm trong kho", stockQuantity);
        }
    }

    /**
     * Determine error color based on error type
     * @param errorCode Error code
     * @return Color for UI (RED, ORANGE, YELLOW)
     */
    private String determineErrorColor(String errorCode) {
        if (errorCode == null) {
            return "RED";
        }
        
        switch (errorCode) {
            case "PRODUCT_OUT_OF_STOCK":
            case "INSUFFICIENT_STOCK":
                return "ORANGE"; // Warning color
            
            case "PRODUCT_NOT_FOUND":
            case "INVALID_QUANTITY":
                return "RED"; // Error color
            
            default:
                return "RED";
        }
    }

    /**
     * Format error message with user-friendly text
     * @param errorCode Error code from use case
     * @param errorMessage Original error message
     * @return Formatted error message
     */
    private String formatErrorMessage(String errorCode, String errorMessage) {
        // Use message directly from domain exception
        return errorMessage != null ? errorMessage : "Lỗi không xác định";
    }
}

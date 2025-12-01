package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.AddToCartViewModel;
import com.motorbike.business.dto.addtocart.AddToCartOutputData;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class AddToCartPresenter implements AddToCartOutputBoundary {
    
    private final AddToCartViewModel viewModel;
    private static final NumberFormat CURRENCY_FORMATTER =
        NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

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

    
    private void presentSuccess(AddToCartOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        
        viewModel.cartId = outputData.getCartId();
        viewModel.totalItems = outputData.getTotalItems();
        viewModel.totalQuantity = outputData.getTotalQuantity();
        viewModel.formattedTotalAmount = formatCurrency(outputData.getTotalAmount());
        
        viewModel.productId = outputData.getProductId();
        viewModel.productName = outputData.getProductName();
        viewModel.addedQuantity = outputData.getAddedQuantity();
        viewModel.newItemQuantity = outputData.getNewItemQuantity();
        viewModel.itemAlreadyInCart = outputData.isItemAlreadyInCart();
        
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
        
        viewModel.formattedProductPrice = formatCurrency(outputData.getProductPrice());
        viewModel.productStock = outputData.getProductStock();
        viewModel.stockStatus = formatStockStatus(
            outputData.getProductStock(),
            outputData.getNewItemQuantity()
        );
        
        viewModel.message = String.format(
            "Thêm vào giỏ hàng thành công! Giỏ hàng có %d sản phẩm (%s)",
            viewModel.totalItems,
            viewModel.formattedTotalAmount
        );
        
        viewModel.showCartPopup = true;
        viewModel.redirectUrl = null;
        
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.errorColor = null;
    }

    
    private void presentError(AddToCartOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(
            outputData.getErrorCode(),
            outputData.getErrorMessage()
        );
        viewModel.errorColor = determineErrorColor(outputData.getErrorCode());
        
        viewModel.message = "Không thể thêm vào giỏ hàng";
        
        viewModel.showCartPopup = false;
        viewModel.redirectUrl = null;
        
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

    
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "₫0";
        }
        return CURRENCY_FORMATTER.format(amount);
    }

    
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

    
    private String determineErrorColor(String errorCode) {
        if (errorCode == null) {
            return "RED";
        }
        
        switch (errorCode) {
            case "PRODUCT_OUT_OF_STOCK":
            case "INSUFFICIENT_STOCK":
                return "ORANGE";
            
            case "PRODUCT_NOT_FOUND":
            case "INVALID_QUANTITY":
                return "RED";
            
            default:
                return "RED";
        }
    }

    
    private String formatErrorMessage(String errorCode, String errorMessage) {
        return errorMessage != null ? errorMessage : "Lỗi không xác định";
    }
}

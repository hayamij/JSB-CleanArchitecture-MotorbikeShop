package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.RemoveItemFromCartViewModel;
import com.motorbike.business.dto.removeitemfromcart.RemoveItemFromCartOutputData;
import com.motorbike.business.usecase.output.RemoveItemFromCartOutputBoundary;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class RemoveItemFromCartPresenter implements RemoveItemFromCartOutputBoundary {
    
    private final RemoveItemFromCartViewModel viewModel;
    private static final NumberFormat VND_FORMAT;
    
    static {
        VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }
    
    public RemoveItemFromCartPresenter(RemoveItemFromCartViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(RemoveItemFromCartOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(RemoveItemFromCartOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.cartId = outputData.getCartId();
        viewModel.productId = outputData.getProductId();
        viewModel.productName = outputData.getProductName();
        viewModel.remainingItems = outputData.getRemainingItems();
        viewModel.rawNewTotal = outputData.getNewTotal();
        viewModel.formattedNewTotal = formatPrice(outputData.getNewTotal());
        
        viewModel.message = String.format("Đã xóa sản phẩm '%s' khỏi giỏ hàng. Còn %d sản phẩm.",
            outputData.getProductName(), outputData.getRemainingItems());
        
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
    }
    
    private void presentError(RemoveItemFromCartOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.cartId = null;
        viewModel.productId = null;
        viewModel.productName = null;
        viewModel.remainingItems = 0;
        viewModel.rawNewTotal = BigDecimal.ZERO;
        viewModel.formattedNewTotal = formatPrice(BigDecimal.ZERO);
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = translateErrorMessage(outputData.getErrorCode(), outputData.getErrorMessage());
        viewModel.message = viewModel.errorMessage;
    }
    
    private String translateErrorMessage(String errorCode, String originalMessage) {
        if (errorCode == null) return originalMessage;
        
        switch (errorCode) {
            case "INVALID_CART_ID":
                return "Mã giỏ hàng không hợp lệ";
            case "INVALID_PRODUCT_ID":
                return "Mã sản phẩm không hợp lệ";
            case "CART_NOT_FOUND":
                return "Không tìm thấy giỏ hàng";
            case "PRODUCT_NOT_IN_CART":
                return "Sản phẩm không có trong giỏ hàng";
            case "INVALID_INPUT":
                return "Dữ liệu đầu vào không hợp lệ";
            default:
                return "Lỗi hệ thống: " + originalMessage;
        }
    }
    
    private String formatPrice(BigDecimal price) {
        if (price == null) {
            return VND_FORMAT.format(0);
        }
        return VND_FORMAT.format(price);
    }
    
    public RemoveItemFromCartViewModel getViewModel() {
        return viewModel;
    }
}

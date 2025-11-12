package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.UpdateCartQuantityViewModel;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.usecase.UpdateCartQuantityOutputBoundary;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Presenter for Update Cart Quantity use case.
 * Transforms business output data into view-ready format.
 */
public class UpdateCartQuantityPresenter implements UpdateCartQuantityOutputBoundary {
    
    private UpdateCartQuantityViewModel viewModel;
    private static final NumberFormat VND_FORMAT;
    
    static {
        VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }

    @Override
    public void present(UpdateCartQuantityOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }

    private void presentSuccess(UpdateCartQuantityOutputData outputData) {
        // Format prices
        String formattedItemSubtotal = outputData.getItemSubtotal() != null 
            ? formatPrice(outputData.getItemSubtotal())
            : null;
        String formattedTotalAmount = formatPrice(outputData.getTotalAmount());

        // Build cart items
        List<UpdateCartQuantityViewModel.CartItemViewModel> itemViewModels = new ArrayList<>();
        for (UpdateCartQuantityOutputData.CartItemData item : outputData.getAllItems()) {
            itemViewModels.add(new UpdateCartQuantityViewModel.CartItemViewModel(
                item.getProductId(),
                item.getProductName(),
                formatPrice(item.getUnitPrice()),
                item.getQuantity(),
                formatPrice(item.getSubtotal())
            ));
        }

        // Generate success message
        String message;
        if (outputData.isItemRemoved()) {
            message = String.format("Đã xóa '%s' khỏi giỏ hàng", outputData.getProductName());
        } else {
            message = String.format("Đã cập nhật số lượng '%s' từ %d thành %d", 
                outputData.getProductName(), 
                outputData.getOldQuantity(), 
                outputData.getNewQuantity());
        }

        // Build cart summary message
        String cartSummary = String.format("Giỏ hàng có %d sản phẩm (%d món)", 
            outputData.getTotalItems(), 
            outputData.getTotalQuantity());

        viewModel = new UpdateCartQuantityViewModel(
            true,
            message,
            null,
            "#28a745", // Green for success
            outputData.getCartId(),
            outputData.getUserId(),
            outputData.getProductId(),
            outputData.getProductName(),
            outputData.getOldQuantity(),
            outputData.getNewQuantity(),
            outputData.isItemRemoved(),
            outputData.getTotalItems(),
            outputData.getTotalQuantity(),
            formattedTotalAmount,
            formattedItemSubtotal,
            itemViewModels,
            cartSummary
        );
    }

    private void presentError(UpdateCartQuantityOutputData outputData) {
        String errorMessage = translateErrorMessage(
            outputData.getErrorCode(), 
            outputData.getErrorMessage()
        );

        viewModel = new UpdateCartQuantityViewModel(
            false,
            errorMessage,
            outputData.getErrorCode(),
            "#dc3545", // Red for error
            null, null, null, null, 0, 0, false, 0, 0, null, null, null, null
        );
    }

    private String translateErrorMessage(String errorCode, String originalMessage) {
        switch (errorCode) {
            case "INVALID_INPUT":
                return "Dữ liệu đầu vào không hợp lệ";
            case "CART_NOT_FOUND":
                return "Không tìm thấy giỏ hàng";
            case "INVALID_QUANTITY":
                return "Số lượng không hợp lệ (phải >= 0)";
            case "PRODUCT_NOT_IN_CART":
                return "Sản phẩm không có trong giỏ hàng";
            case "PRODUCT_NOT_FOUND":
                return "Không tìm thấy sản phẩm";
            case "PRODUCT_NOT_AVAILABLE":
                return "Sản phẩm hiện không khả dụng";
            case "INSUFFICIENT_STOCK":
                return "Không đủ hàng trong kho. " + originalMessage.substring(originalMessage.indexOf("Available:"));
            default:
                return originalMessage;
        }
    }

    private String formatPrice(BigDecimal price) {
        if (price == null) {
            return "0 ₫";
        }
        return VND_FORMAT.format(price).replace("₫", "").trim() + " ₫";
    }

    public UpdateCartQuantityViewModel getViewModel() {
        return viewModel;
    }
}

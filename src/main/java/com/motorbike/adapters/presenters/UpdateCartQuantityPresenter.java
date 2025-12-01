package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.UpdateCartQuantityViewModel;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpdateCartQuantityPresenter implements UpdateCartQuantityOutputBoundary {
    
    private final UpdateCartQuantityViewModel viewModel;
    private static final NumberFormat VND_FORMAT;
    
    static {
        VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    }

    public UpdateCartQuantityPresenter(UpdateCartQuantityViewModel viewModel) {
        this.viewModel = viewModel;
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
        String formattedItemSubtotal = outputData.getItemSubtotal() != null
            ? formatPrice(outputData.getItemSubtotal())
            : null;
        String formattedTotalAmount = formatPrice(outputData.getTotalAmount());

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

        String message;
        if (outputData.isItemRemoved()) {
            message = String.format("Đã xóa '%s' khỏi giỏ hàng", outputData.getProductName());
        } else {
            message = String.format("Đã cập nhật số lượng '%s' từ %d thành %d",
                outputData.getProductName(),
                outputData.getOldQuantity(),
                outputData.getNewQuantity());
        }

        String cartSummary = String.format("Giỏ hàng có %d sản phẩm (%d món)",
            outputData.getTotalItems(),
            outputData.getTotalQuantity());

        viewModel.success = true;
        viewModel.message = message;
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.messageColor = "#28a745";
        viewModel.cartId = outputData.getCartId();
        viewModel.userId = outputData.getUserId();
        viewModel.productId = outputData.getProductId();
        viewModel.productName = outputData.getProductName();
        viewModel.oldQuantity = outputData.getOldQuantity();
        viewModel.newQuantity = outputData.getNewQuantity();
        viewModel.itemRemoved = outputData.isItemRemoved();
        viewModel.totalItems = outputData.getTotalItems();
        viewModel.totalQuantity = outputData.getTotalQuantity();
        viewModel.totalAmount = formattedTotalAmount;
        viewModel.itemSubtotal = formattedItemSubtotal;
        viewModel.allItems = itemViewModels;
        viewModel.cartSummary = cartSummary;
    }

    private void presentError(UpdateCartQuantityOutputData outputData) {
        String errorMessage = translateErrorMessage(
            outputData.getErrorCode(),
            outputData.getErrorMessage()
        );

        viewModel.success = false;
        viewModel.message = errorMessage;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = errorMessage;
        viewModel.messageColor = "#dc3545";
        viewModel.cartId = null;
        viewModel.userId = null;
        viewModel.productId = null;
        viewModel.productName = null;
        viewModel.oldQuantity = 0;
        viewModel.newQuantity = 0;
        viewModel.itemRemoved = false;
        viewModel.totalItems = 0;
        viewModel.totalQuantity = 0;
        viewModel.totalAmount = null;
        viewModel.itemSubtotal = null;
        viewModel.allItems = null;
        viewModel.cartSummary = null;
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

    public UpdateCartQuantityViewModel getViewModel() {return viewModel;}
}

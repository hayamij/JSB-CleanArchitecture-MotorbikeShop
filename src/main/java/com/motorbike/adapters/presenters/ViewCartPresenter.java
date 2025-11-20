package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.ViewCartViewModel;
import com.motorbike.adapters.viewmodels.ViewCartViewModel.CartItemViewModel;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.usecase.output.ViewCartOutputBoundary;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Presenter: ViewCartPresenter
 * Converts ViewCartOutputData to ViewCartViewModel
 * Formats data for UI presentation
 */
public class ViewCartPresenter implements ViewCartOutputBoundary {
    private final ViewCartViewModel viewModel;
    private static final NumberFormat VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public ViewCartPresenter(ViewCartViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ViewCartOutputData outputData) {
        if (!outputData.isSuccess()) {
            // Error case
            viewModel.success = false;
            viewModel.cartId = null;
            viewModel.userId = null;
            viewModel.totalItems = 0;
            viewModel.totalQuantity = 0;
            viewModel.formattedTotalAmount = "0 ₫";
            viewModel.items = null;
            viewModel.isEmpty = true;
            viewModel.hasStockWarnings = false;
            viewModel.message = null;
            viewModel.errorMessage = outputData.getErrorMessage();
            return;
        }

        // Empty cart case
        if (outputData.isEmpty()) {
            viewModel.success = true;
            viewModel.cartId = outputData.getCartId();
            viewModel.userId = outputData.getUserId();
            viewModel.totalItems = 0;
            viewModel.totalQuantity = 0;
            viewModel.formattedTotalAmount = "0 ₫";
            viewModel.items = new ArrayList<>();
            viewModel.isEmpty = true;
            viewModel.hasStockWarnings = false;
            viewModel.message = "Giỏ hàng của bạn đang trống";
            viewModel.errorMessage = null;
            return;
        }

        // Format cart items
        List<CartItemViewModel> itemViewModels = new ArrayList<>();
        if (outputData.getItems() != null) {
            for (ViewCartOutputData.CartItemData item : outputData.getItems()) {
                String stockStatus = getStockStatus(item);
                String stockWarningColor = item.hasStockWarning() ? "#dc3545" : null; // Red for warnings

                itemViewModels.add(new CartItemViewModel(
                    item.getProductId(),
                    item.getProductName(),
                    item.getProductImageUrl(),
                    VND_FORMAT.format(item.getUnitPrice()),
                    item.getUnitPrice(), // raw value
                    item.getQuantity(),
                    VND_FORMAT.format(item.getSubtotal()),
                    item.getSubtotal(), // raw value
                    item.getAvailableStock(),
                    stockStatus,
                    item.hasStockWarning(),
                    item.getStockWarningMessage(),
                    stockWarningColor
                ));
            }
        }

        // Format total amount
        String formattedTotalAmount = VND_FORMAT.format(outputData.getTotalAmount());

        // Build cart summary message
        String cartSummary = String.format(
            "Giỏ hàng có %d sản phẩm (%d món)",
            outputData.getTotalItems(),
            outputData.getTotalQuantity()
        );

        // Stock warning message
        String stockWarningMessage = outputData.hasStockWarnings() ?
            "Một số sản phẩm trong giỏ hàng có cảnh báo về số lượng tồn kho" : null;

        viewModel.success = true;
        viewModel.cartId = outputData.getCartId();
        viewModel.userId = outputData.getUserId();
        viewModel.totalItems = outputData.getTotalItems();
        viewModel.totalQuantity = outputData.getTotalQuantity();
        viewModel.formattedTotalAmount = formattedTotalAmount;
        viewModel.rawTotalAmount = outputData.getTotalAmount(); // raw value
        viewModel.items = itemViewModels;
        viewModel.isEmpty = false;
        viewModel.hasStockWarnings = outputData.hasStockWarnings();
        viewModel.message = cartSummary;
        viewModel.errorMessage = stockWarningMessage;
    }

    public ViewCartViewModel getViewModel() {
        return viewModel;
    }

    private String getStockStatus(ViewCartOutputData.CartItemData item) {
        int stock = item.getAvailableStock();
        
        // Check stock level first for specific statuses
        if (stock == 0) {
            return "out_of_stock";
        } else if (item.hasStockWarning()) {
            return "warning";
        } else if (stock < 10) {
            return "low_stock";
        } else {
            return "in_stock";
        }
    }
}

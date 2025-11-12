package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.ProductDetailViewModel;
import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;
import com.motorbike.business.usecase.output.GetProductDetailOutputBoundary;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Presenter for Product Detail
 * Transforms OutputData (use case output) → ViewModel (UI-ready data)
 * Contains PRESENTATION LOGIC (formatting, colors, display rules)
 * NO business logic
 */
public class ProductDetailPresenter implements GetProductDetailOutputBoundary {
    
    private final ProductDetailViewModel viewModel;
    private static final NumberFormat VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    
    public ProductDetailPresenter(ProductDetailViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(GetProductDetailOutputData outputData) {
        if (!outputData.success) {
            // Error case - presentation logic for errors
            viewModel.hasError = true;
            viewModel.errorMessage = formatErrorMessage(outputData.errorCode, outputData.errorMessage);
            viewModel.errorColor = "RED";
            return;
        }
        
        // Success case - presentation logic for product display
        viewModel.hasError = false;
        viewModel.productId = String.valueOf(outputData.productId);
        viewModel.name = outputData.name;
        viewModel.description = outputData.description != null ? outputData.description : "No description available";
        viewModel.formattedPrice = formatPrice(outputData.price);
        viewModel.imageUrl = outputData.imageUrl != null ? outputData.imageUrl : "/images/no-image.jpg";
        viewModel.specifications = outputData.specifications != null ? outputData.specifications : "{}";
        viewModel.categoryDisplay = formatCategory(outputData.category);
        viewModel.stockQuantity = formatStockQuantity(outputData.stockQuantity);
        
        // Presentation logic: Determine availability status and color
        if (outputData.inStock) {
            viewModel.availabilityStatus = "Còn hàng";
            viewModel.stockStatusColor = "GREEN";
        } else if (outputData.available && outputData.stockQuantity == 0) {
            viewModel.availabilityStatus = "Hết hàng";
            viewModel.stockStatusColor = "ORANGE";
        } else {
            viewModel.availabilityStatus = "Không có sẵn";
            viewModel.stockStatusColor = "RED";
        }
    }
    
    /**
     * Presentation Logic: Format price to Vietnamese currency
     */
    private String formatPrice(BigDecimal price) {
        if (price == null) {
            return "Liên hệ";
        }
        return VND_FORMAT.format(price);
    }
    
    /**
     * Presentation Logic: Format category for display
     */
    private String formatCategory(String category) {
        if (category == null) {
            return "Chưa phân loại";
        }
        switch (category) {
            case "MOTORCYCLE":
                return "Xe máy";
            case "ACCESSORY":
                return "Phụ kiện";
            default:
                return category;
        }
    }
    
    /**
     * Presentation Logic: Format stock quantity
     */
    private String formatStockQuantity(int quantity) {
        if (quantity == 0) {
            return "Hết hàng";
        } else if (quantity < 5) {
            return quantity + " sản phẩm (Sắp hết)";
        } else {
            return quantity + " sản phẩm";
        }
    }
    
    /**
     * Presentation Logic: Format error message for user-friendly display
     */
    private String formatErrorMessage(String errorCode, String errorMessage) {
        switch (errorCode) {
            case "INVALID_INPUT":
                return "Lỗi: Thông tin không hợp lệ";
            case "PRODUCT_NOT_FOUND":
                return "Lỗi: Không tìm thấy sản phẩm";
            case "INTERNAL_ERROR":
                return "Lỗi hệ thống: " + errorMessage;
            default:
                return "Lỗi: " + errorMessage;
        }
    }
    
    public ProductDetailViewModel getViewModel() {
        return viewModel;
    }
}

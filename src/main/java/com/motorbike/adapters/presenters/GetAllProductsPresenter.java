package com.motorbike.adapters.presenters;

import com.motorbike.business.dto.product.GetAllProductsOutputData;
import com.motorbike.business.dto.product.GetAllProductsOutputData.ProductInfo;
import com.motorbike.business.usecase.output.GetAllProductsOutputBoundary;
import com.motorbike.adapters.viewmodels.GetAllProductsViewModel;
import com.motorbike.adapters.viewmodels.GetAllProductsViewModel.ProductItemViewModel;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GetAllProductsPresenter implements GetAllProductsOutputBoundary {

    private final GetAllProductsViewModel viewModel;
    private final NumberFormat currencyFormat;
    private final DateTimeFormatter dateFormatter;

    public GetAllProductsPresenter(GetAllProductsViewModel viewModel) {
        this.viewModel = viewModel;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @Override
    public void present(GetAllProductsOutputData outputData) {
        if (outputData.isSuccess()) {
            viewModel.success = true;
            viewModel.products = outputData.getProducts().stream()
                    .map(this::mapToViewModel)
                    .collect(Collectors.toList());
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
        } else {
            viewModel.success = false;
            viewModel.products = null;
            viewModel.errorCode = outputData.getErrorCode();
            viewModel.errorMessage = outputData.getErrorMessage();
        }
    }

    private ProductItemViewModel mapToViewModel(ProductInfo productInfo) {
        ProductItemViewModel item = new ProductItemViewModel();
        item.id = productInfo.getProductId();
        item.name = productInfo.getName();
        item.description = productInfo.getDescription();
        item.formattedPrice = currencyFormat.format(productInfo.getPrice());
        item.stock = productInfo.getStockQuantity();
        item.imageUrl = productInfo.getImageUrl();
        item.formattedCreatedDate = productInfo.getCreatedDate() != null 
                ? productInfo.getCreatedDate().format(dateFormatter) 
                : "";
        item.available = productInfo.isAvailable();
        item.category = productInfo.getCategory();
        
        // Motorbike fields
        item.brand = productInfo.getBrand();
        item.model = productInfo.getModel();
        item.color = productInfo.getColor();
        item.year = productInfo.getYear();
        item.engineCapacity = productInfo.getEngineCapacity();
        
        // Accessory fields
        item.type = productInfo.getType();
        item.material = productInfo.getMaterial();
        item.size = productInfo.getSize();
        
        return item;
    }
}

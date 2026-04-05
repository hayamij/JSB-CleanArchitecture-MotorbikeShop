package com.motorbike.adapters.presenters;

import com.motorbike.business.dto.product.GetAllProductsOutputData;
import com.motorbike.business.dto.product.GetAllProductsOutputData.ProductInfo;
import com.motorbike.business.usecase.output.GetAllProductsOutputBoundary;
import com.motorbike.adapters.viewmodels.GetAllProductsViewModel;
import com.motorbike.adapters.viewmodels.GetAllProductsViewModel.ProductItemViewModel;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GetAllProductsPresenter implements GetAllProductsOutputBoundary {

    private final GetAllProductsViewModel viewModel;
    private final DateTimeFormatter dateFormatter;

    public GetAllProductsPresenter(GetAllProductsViewModel viewModel) {
        this.viewModel = viewModel;
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
        item.id = productInfo.getProductId();
        item.name = productInfo.getName();
        item.description = productInfo.getDescription();
        item.price = productInfo.getPrice();
        item.stock = productInfo.getStockQuantity();
        item.imageUrl = productInfo.getImageUrl();
        item.formattedCreatedDate = productInfo.getCreatedDate() != null 
                ? productInfo.getCreatedDate().format(dateFormatter) 
                : "";
        item.available = productInfo.isAvailable();
        item.category = productInfo.getCategory();
        
        // Motorbike fields
        if ("XE_MAY".equals(productInfo.getCategory())) {
            item.brand = productInfo.getBrand();
            item.model = productInfo.getModel();
            item.color = productInfo.getColor();
            item.year = productInfo.getYear();
            item.engineCapacity = productInfo.getEngineCapacity();
        }
        
        // Accessory fields
        if ("PHU_KIEN".equals(productInfo.getCategory())) {
            item.type = productInfo.getType();
            item.brand = productInfo.getBrand(); // thuongHieu for accessories
            item.material = productInfo.getMaterial();
            item.size = productInfo.getSize();
        }
        
        return item;
    }
}

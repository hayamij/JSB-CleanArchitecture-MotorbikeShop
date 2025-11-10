package com.motorbike.interfaceadapters.mapper;

import com.motorbike.business.usecase.GetProductDetailUseCase.ProductDetailResponse;
import com.motorbike.interfaceadapters.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper to convert between Use Case Response and Presentation DTO
 */
@Component
public class ProductMapper {
    
    /**
     * Convert from Use Case Response to Presentation DTO
     * @param response Use case response
     * @return ProductDTO for API response
     */
    public ProductDTO toDTO(ProductDetailResponse response) {
        if (response == null) {
            return null;
        }
        
        ProductDTO dto = new ProductDTO();
        dto.setId(response.getId());
        dto.setName(response.getName());
        dto.setDescription(response.getDescription());
        dto.setPrice(new BigDecimal(response.getPrice()));
        dto.setImageUrl(response.getImageUrl());
        dto.setSpecifications(response.getSpecifications());
        dto.setCategory(response.getCategory());
        dto.setStockQuantity(response.getStockQuantity());
        dto.setAvailable(response.isAvailable());
        dto.setInStock(response.isInStock());
        
        return dto;
    }
}

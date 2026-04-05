package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.updateproductstock.UpdateProductStockOutputData;

public interface UpdateProductStockOutputBoundary {
    
    void present(UpdateProductStockOutputData outputData);
}

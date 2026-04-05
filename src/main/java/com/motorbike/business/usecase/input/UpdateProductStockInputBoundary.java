package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.updateproductstock.UpdateProductStockInputData;

public interface UpdateProductStockInputBoundary {
    
    void execute(UpdateProductStockInputData inputData);
}

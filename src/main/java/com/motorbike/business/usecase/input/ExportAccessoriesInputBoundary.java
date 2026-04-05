package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.accessory.ExportAccessoriesInputData;

/**
 * Input Boundary (Interface) cho Use Case: Export phụ kiện ra Excel
 */
public interface ExportAccessoriesInputBoundary {
    
    /**
     * Thực thi use case export phụ kiện
     * 
     * @param inputData Dữ liệu đầu vào (filters nếu có)
     */
    void execute(ExportAccessoriesInputData inputData);
}

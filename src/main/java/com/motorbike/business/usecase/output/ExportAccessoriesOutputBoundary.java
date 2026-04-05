package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.accessory.ExportAccessoriesOutputData;

/**
 * Output Boundary (Interface) cho Use Case: Export phụ kiện ra Excel
 */
public interface ExportAccessoriesOutputBoundary {
    
    /**
     * Present kết quả export thành công
     */
    void presentSuccess(ExportAccessoriesOutputData outputData);
    
    /**
     * Present lỗi khi export
     */
    void presentError(String errorMessage);
}

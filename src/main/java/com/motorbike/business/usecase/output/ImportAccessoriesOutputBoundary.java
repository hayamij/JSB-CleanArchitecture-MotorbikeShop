package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.accessory.ImportAccessoriesOutputData;

/**
 * Output Boundary (Port) cho use case Import phụ kiện từ Excel.
 * 
 * Interface này định nghĩa contract mà Presenter phải implement.
 */
public interface ImportAccessoriesOutputBoundary {
    void present(ImportAccessoriesOutputData outputData);
}

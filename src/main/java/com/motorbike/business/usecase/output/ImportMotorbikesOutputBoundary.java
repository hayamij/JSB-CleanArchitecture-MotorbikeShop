package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.motorbike.ImportMotorbikesOutputData;

/**
 * Output Boundary (Port) cho use case Import xe máy từ Excel.
 * 
 * Interface này định nghĩa contract mà Presenter phải implement.
 */
public interface ImportMotorbikesOutputBoundary {
    void present(ImportMotorbikesOutputData outputData);
}

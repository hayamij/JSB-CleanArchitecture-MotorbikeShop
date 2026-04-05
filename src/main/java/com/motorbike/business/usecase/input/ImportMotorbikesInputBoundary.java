package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.motorbike.ImportMotorbikesInputData;

/**
 * Input Boundary (Port) cho use case Import xe máy từ Excel.
 * 
 * Interface này định nghĩa contract mà Controller sẽ gọi.
 */
public interface ImportMotorbikesInputBoundary {
    void execute(ImportMotorbikesInputData inputData);
}

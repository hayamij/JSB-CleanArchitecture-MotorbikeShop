package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.accessory.ImportAccessoriesInputData;

/**
 * Input Boundary (Port) cho use case Import phụ kiện từ Excel.
 * 
 * Interface này định nghĩa contract mà Use Case Control phải implement.
 */
public interface ImportAccessoriesInputBoundary {
    void execute(ImportAccessoriesInputData inputData);
}

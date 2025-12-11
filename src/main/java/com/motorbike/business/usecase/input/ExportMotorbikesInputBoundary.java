package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.motorbike.ExportMotorbikesInputData;

/**
 * Input Boundary (Interface) cho Use Case: Export xe máy ra Excel
 * 
 * Định nghĩa contract mà Controller sẽ gọi để thực thi use case
 */
public interface ExportMotorbikesInputBoundary {
    
    /**
     * Thực thi use case export xe máy
     * 
     * @param inputData Dữ liệu đầu vào (filters nếu có)
     */
    void execute(ExportMotorbikesInputData inputData);
}

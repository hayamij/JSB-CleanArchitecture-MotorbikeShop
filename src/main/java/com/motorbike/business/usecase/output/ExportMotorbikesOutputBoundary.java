package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.motorbike.ExportMotorbikesOutputData;

/**
 * Output Boundary (Interface) cho Use Case: Export xe máy ra Excel
 * 
 * Presenter sẽ implement interface này để nhận kết quả
 */
public interface ExportMotorbikesOutputBoundary {
    
    /**
     * Present kết quả export thành công
     * 
     * @param outputData Dữ liệu output chứa file Excel
     */
    void presentSuccess(ExportMotorbikesOutputData outputData);
    
    /**
     * Present lỗi khi export
     * 
     * @param errorMessage Thông báo lỗi
     */
    void presentError(String errorMessage);
}

package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.motorbike.ExportMotorbikesOutputData;

/**
 * ViewModel cho Export Motorbikes.
 * 
 * Chứa trạng thái presentation sau khi use case thực thi.
 */
public class ExportMotorbikesViewModel {
    public boolean hasError;
    public String errorMessage;
    public ExportMotorbikesOutputData exportResult;
}

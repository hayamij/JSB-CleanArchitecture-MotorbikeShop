package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.motorbike.ImportMotorbikesOutputData;

/**
 * ViewModel cho Import Motorbikes.
 * 
 * Chứa trạng thái presentation sau khi use case thực thi.
 */
public class ImportMotorbikesViewModel {
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public ImportMotorbikesOutputData importResult;
}

package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.accessory.ExportAccessoriesOutputData;

/**
 * ViewModel cho Export Accessories.
 * 
 * Chứa trạng thái presentation sau khi use case thực thi.
 */
public class ExportAccessoriesViewModel {
    public boolean hasError;
    public String errorMessage;
    public ExportAccessoriesOutputData exportResult;
}

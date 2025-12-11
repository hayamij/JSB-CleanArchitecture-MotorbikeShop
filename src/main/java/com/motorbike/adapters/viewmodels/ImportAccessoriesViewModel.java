package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.accessory.ImportAccessoriesOutputData;

/**
 * ViewModel cho Import Accessories.
 * 
 * Chứa trạng thái presentation sau khi use case thực thi.
 */
public class ImportAccessoriesViewModel {
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public ImportAccessoriesOutputData importResult;
}

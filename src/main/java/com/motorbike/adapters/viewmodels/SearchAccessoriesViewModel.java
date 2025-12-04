package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData.AccessoryItem;
import java.util.List;

public class SearchAccessoriesViewModel {
    public boolean hasError = false;
    public String errorCode;
    public String errorMessage;
    public List<AccessoryItem> accessories;
}

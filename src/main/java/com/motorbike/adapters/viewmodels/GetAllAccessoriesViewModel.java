package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData.AccessoryItem;
import java.util.List;

public class GetAllAccessoriesViewModel {
    public boolean hasError = false;
    public String errorCode;
    public String errorMessage;
    public List<AccessoryItem> accessories;
}

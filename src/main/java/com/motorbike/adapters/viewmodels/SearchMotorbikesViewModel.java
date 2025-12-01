package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.motorbike.SearchMotorbikesOutputData.MotorbikeItem;
import java.util.List;

public class SearchMotorbikesViewModel {
    public boolean hasError = false;
    public String errorCode;
    public String errorMessage;
    public List<MotorbikeItem> motorbikes;
}

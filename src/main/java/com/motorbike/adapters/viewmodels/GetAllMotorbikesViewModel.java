package com.motorbike.adapters.viewmodels;

import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData.MotorbikeItem;

import java.util.List;

public class GetAllMotorbikesViewModel {
    public boolean hasError = false;
    public String errorCode;
    public String errorMessage;
    public List<MotorbikeItem> motorbikes;
}

package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.updateorderinfor.UpdateOrderInforInputData;

public interface UpdateOrderInforInputBoundary {
    void execute(UpdateOrderInforInputData inputData);
}

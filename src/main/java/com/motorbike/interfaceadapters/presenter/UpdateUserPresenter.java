package com.motorbike.interfaceadapters.presenter;

import com.motorbike.business.usecase.user.UpdateUserOutputData;
import com.motorbike.business.usecase.user.update.UpdateUserOutputBoundary;


public class UpdateUserPresenter implements UpdateUserOutputBoundary {

    private UpdateUserOutputData outputData;

    public void present(UpdateUserOutputData outputData) {
        this.outputData = outputData;
    }

    public UpdateUserOutputData getOutputData() {
        return outputData;
    }
}

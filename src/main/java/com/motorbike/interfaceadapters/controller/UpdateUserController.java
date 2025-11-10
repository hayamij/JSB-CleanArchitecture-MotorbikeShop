package com.motorbike.interfaceadapters.controller;

import com.motorbike.business.usecase.user.update.*;

public class UpdateUserController {

    private final UpdateUserInputBoundary interactor;

    public UpdateUserController(UpdateUserInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void updateUser(int id, String username, String password,
                           String email, String fullName, String phone, String role) {
        UpdateUserInputData data = new UpdateUserInputData(id, username, password, email, fullName, phone, role);
        interactor.execute(data);
    }
}

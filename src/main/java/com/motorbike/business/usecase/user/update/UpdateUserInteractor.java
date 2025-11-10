package com.motorbike.business.usecase.user.update;

import com.motorbike.business.entity.User;
import com.motorbike.business.repository.UserRepository;
import com.motorbike.business.usecase.user.UpdateUserOutputData;

public class UpdateUserInteractor extends UpdateUserInputBoundary {

    private final UserRepository repository;
    private final UpdateUserOutputBoundary presenter;

    public UpdateUserInteractor(UserRepository repository, UpdateUserOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    public void execute(UpdateUserInputData inputData) {
        User user = new User(
        );

        boolean updated = repository.update(user);
        String message = updated ? "Cập nhật người dùng thành công!" : "Cập nhật thất bại!";
        presenter.present(new UpdateUserOutputData(message, updated));
    }
}

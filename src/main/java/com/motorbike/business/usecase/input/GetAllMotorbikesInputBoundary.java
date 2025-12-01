package com.motorbike.business.usecase.input;

public interface GetAllMotorbikesInputBoundary {

    // Dùng Void làm input, trùng với AbstractUseCaseControl<Void, ...>
    void execute(Void inputData);
}

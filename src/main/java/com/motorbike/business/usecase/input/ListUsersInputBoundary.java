package com.motorbike.business.usecase.input;
import com.motorbike.business.dto.listusers.ListUsersInputData;
public interface  ListUsersInputBoundary {
    void execute(ListUsersInputData inputData);
}

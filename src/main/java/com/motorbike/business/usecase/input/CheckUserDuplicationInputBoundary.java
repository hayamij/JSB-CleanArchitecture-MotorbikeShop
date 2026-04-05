package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.checkuserduplication.CheckUserDuplicationInputData;

public interface CheckUserDuplicationInputBoundary {
    void execute(CheckUserDuplicationInputData inputData);
}

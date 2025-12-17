package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.checkuserduplication.CheckUserDuplicationOutputData;

public interface CheckUserDuplicationOutputBoundary {
    void present(CheckUserDuplicationOutputData outputData);
}

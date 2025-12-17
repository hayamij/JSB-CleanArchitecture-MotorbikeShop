package com.motorbike.business.usecase.output;

import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationOutputData;

public interface CheckProductDuplicationOutputBoundary {
    void present(CheckProductDuplicationOutputData outputData);
}

package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationInputData;

public interface CheckProductDuplicationInputBoundary {
    void execute(CheckProductDuplicationInputData inputData);
}

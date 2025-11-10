package com.motorbike.main.business.usecase;


import com.motorbike.main.shared.ListOrdersResponseData;

public interface ListOrdersOutputBoundary {
    void present(ListOrdersResponseData response);
}

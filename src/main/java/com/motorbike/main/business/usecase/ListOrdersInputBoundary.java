package com.motorbike.main.business.usecase;


import com.motorbike.main.shared.ListOrdersRequestData;

public interface ListOrdersInputBoundary {
    void execute(ListOrdersRequestData request);
}


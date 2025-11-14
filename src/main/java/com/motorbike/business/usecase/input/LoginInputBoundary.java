package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.login.LoginInputData;

/**
 * Input Boundary (Use Case Interface) for Login
 * Defines the contract for the Login use case
 * Adapter layer depends on this interface
 */
public interface LoginInputBoundary {
    /**
     * Execute the login use case
     * @param inputData Contains email and password
     */
    void execute(LoginInputData inputData);
}

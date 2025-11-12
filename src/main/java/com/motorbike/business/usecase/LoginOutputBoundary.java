package com.motorbike.business.usecase;

import com.motorbike.business.dto.login.LoginOutputData;

/**
 * Output Boundary (Presenter Interface) for Login
 * Defines the contract for presenting login results
 * Use case depends on this interface (Dependency Inversion)
 */
public interface LoginOutputBoundary {
    /**
     * Present the login result
     * @param outputData Contains user data or error information
     */
    void present(LoginOutputData outputData);
}

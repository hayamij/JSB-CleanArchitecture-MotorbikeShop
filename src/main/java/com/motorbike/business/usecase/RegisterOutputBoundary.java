package com.motorbike.business.usecase;

import com.motorbike.business.dto.register.RegisterOutputData;

/**
 * Output Boundary (Presenter Interface) for Register
 * Defines the contract for presenting registration results
 * Use case depends on this interface (Dependency Inversion)
 */
public interface RegisterOutputBoundary {
    /**
     * Present the registration result
     * @param outputData Contains user data or error information
     */
    void present(RegisterOutputData outputData);
}

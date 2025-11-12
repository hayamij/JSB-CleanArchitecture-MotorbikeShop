package com.motorbike.business.usecase.input;

import com.motorbike.business.dto.register.RegisterInputData;

/**
 * Input Boundary (Use Case Interface) for Register
 * Defines the contract for the Register use case
 * Adapter layer depends on this interface
 */
public interface RegisterInputBoundary {
    /**
     * Execute the registration use case
     * @param inputData Contains user registration information
     */
    void execute(RegisterInputData inputData);
}

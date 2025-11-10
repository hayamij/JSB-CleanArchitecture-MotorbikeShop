package com.motorbike.business.usecase.impl;

import com.motorbike.business.usecase.RegisterUseCase;
import com.motorbike.business.entity.User;
import com.motorbike.business.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of Register Use Case
 * Contains the business logic for user registration
 */
@Service
public class RegisterUseCaseImpl implements RegisterUseCase {
    
    private final UserRepository userRepository;
    
    public RegisterUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public RegisterResponse execute(RegisterRequest request) {
        // Step 1: Validate input
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        // Step 2: Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                "Email already registered: " + request.getEmail()
            );
        }
        
        // Step 3: Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(
                "Username already taken: " + request.getUsername()
            );
        }
        
        // Step 4: Create new user entity
        User newUser = new User.Builder()
            .email(request.getEmail())
            .username(request.getUsername())
            .password(hashPassword(request.getPassword())) // In production, use BCrypt
            .phoneNumber(request.getPhoneNumber())
            .role("CUSTOMER") // Default role
            .active(true)
            .build();
        
        // Step 5: Save user to database
        User savedUser = userRepository.save(newUser);
        
        // Step 6: Return response
        return new RegisterResponse(savedUser);
    }
    
    /**
     * Hash password
     * TODO: In production, use BCrypt or similar password hashing
     */
    private String hashPassword(String plainPassword) {
        // For demo purposes, we'll store plain text
        // In production, use: BCrypt.hashpw(plainPassword, BCrypt.gensalt())
        return plainPassword;
    }
}
